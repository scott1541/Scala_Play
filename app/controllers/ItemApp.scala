package controllers

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import javax.inject.Inject

import models.{Item, ItemDelete}
import play.api._
import play.api.mvc._
import akka.stream.scaladsl.Source
import akka.stream.scaladsl.FileIO
import akka.stream.scaladsl.StreamConverters
import akka.util.ByteString
import play.api.http.HttpEntity
import play.api.i18n.{I18nSupport, MessagesApi}

import scala.concurrent.Future

class ItemApp @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index = Action {
    Ok(views.html.index("ready"))
  }

  def listItems = Action {
    implicit request =>
       Ok(views.html.showItems(Item.items, Item.createItemForm))
  }

  def createItem = Action {
    implicit request =>

      val formValidationResult = Item.createItemForm.bindFromRequest
      formValidationResult.fold({ formWithErrors =>
        BadRequest(views.html.showItems(Item.items, formWithErrors))
      }, { item =>
         Item.items.append(item)
         Redirect(routes.ItemApp.listItems)
      })
  }

  def deleteItem = Action { implicit request =>

    val formValidationResult = ItemDelete.deleteItemForm.bindFromRequest()
    formValidationResult.fold({ formWithErrors =>
      BadRequest(views.html.deleteItems(Item.items, Item.createItemForm, formWithErrors))
    }, { item =>
       Item.items = Item.items.filter(x => x.name.toLowerCase != item.name)
       Redirect(routes.ItemApp.deleteItem)
    })
  }


  def editItem = Action { implicit request =>

    val formValidationResult = Item.createItemForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      BadRequest(views.html.editItems(Item.items, formWithErrors))
    }, { item =>
      Item.items = Item.items.filter(x => x.name.toLowerCase != item.name);
      Item.items += item
      Redirect(routes.ItemApp.editItem)
    })
  }

  def fileRename = Action {
    Ok.sendFile(
      content = new java.io.File("C:/Users/Administrator/Documents//GitHub/ScalaPlay/public/temp/elliott.pdf"),
      fileName = _ => "elot.pdf"
    )
  }

  def fileServeCL = Action {
    val file = new java.io.File("C:/Users/Administrator/Documents//GitHub/ScalaPlay/public/temp/elliott.pdf")
    val path: java.nio.file.Path = file.toPath
    val source: Source[ByteString, _] = FileIO.fromPath(path)

    val contentLength = Some(file.length())

    Result(
      header = ResponseHeader(200, Map.empty),
      body = HttpEntity.Streamed(source, contentLength, Some("application/pdf"))
    )
  }

  def fileAttachment = Action {
    Ok.sendFile(
      content = new java.io.File("C:/Users/Administrator/Documents//GitHub/ScalaPlay/public/temp/elliott.pdf"),
      fileName = _ => "elot.pdf",
      inline = false
    )
  }

  def chunkedFromSource = Action {
    val source = Source.apply(List("kiki", "foo", "bar", "coo", "man", "boo"))
    Ok.chunked(source)
  }

  def chunked = Action {
    val data =  new ByteArrayInputStream (serialise("Hello"))
    println(serialise("hello")) // serializing something
    println(deserialise(serialise("hello"))) // deserializing
    val dataContent: Source[ByteString, _] = StreamConverters.fromInputStream(() => data)
    Ok.chunked(dataContent)
  }

  def serialise(value: Any): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(value)
    oos.close
    stream.toByteArray
  }

  def deserialise(bytes: Array[Byte]): Any = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
    val value = ois.readObject
    ois.close
    value
  }
}
