package controllers

import javax.inject.Inject

import scala.concurrent.Future
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import reactivemongo.api.Cursor
import models._
import models.JsonFormats._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import collection._

class MongoApplicationController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents {

  // TODO - keep in mind you need to have mongod.exe running before attempting to play around

  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("items"))

  def create(item: Item): Action[AnyContent] = Action.async {
    //case class User(age: Int, firstName: String, lastName: String, feeds: Seq[Feed])
    //case class Feed(name: String, url: String)

    //val item = Item("Fan", "12in desk fan", 19.99, "12 months", "10%", "5dayshop")
    val futureResult = collection.flatMap(_.insert(item))
    futureResult.map(_ => Ok("Added item " + item.name + " " + item.price))
  }

  def createItem: Action[AnyContent] = Action.async {
    implicit request =>
      val formValidationResult = Item.createItemForm.bindFromRequest
      formValidationResult.fold({ formWithErrors =>
        BadRequest(views.html.showItems(Item.items, formWithErrors))
      }, { item => create(item)
        Redirect(routes.ItemApp.listItems)
      })
  }

  def delByName(name: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Item]] = collection.map {
      _.find(Json.obj("name" -> name))
        //.sort(Json.obj("created" -> -1))
        .cursor[Item]

      val futureResult = collection.flatMap(_.remove(item))
    }
  }

  def findByName(name: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Item]] = collection.map {
      _.find(Json.obj("name" -> name))
        //.sort(Json.obj("created" -> -1))
          .cursor[Item]
    }
    val futureUsersList: Future[List[Item]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { persons =>
      Ok(persons.head.toString)
    }
  }

}