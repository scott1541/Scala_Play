package controllers

import javax.inject.Inject

import models.{Item, ItemDelete}
import play.api._
import play.api.mvc._
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
    formValidationResult.fold({formWithErrors =>
      BadRequest(views.html.showItems(Item.items, formWithErrors))
    }, {item => Item.items.append(item)
    Redirect(routes.ItemApp.listItems)
    })
  }

  def deleteItem = Action { implicit request =>

      val formValidationResult = ItemDelete.deleteItemForm.bindFromRequest()
      formValidationResult.fold({formWithErrors =>
        BadRequest(views.html.deleteItems(Item.items, Item.createItemForm, formWithErrors))
      }, { item =>
        Item.items = Item.items.filter( x => x.name.toLowerCase != item.name)
        Redirect(routes.ItemApp.deleteItem)})
      }


  def editItem = Action { implicit request =>

    val formValidationResult = Item.createItemForm.bindFromRequest
    formValidationResult.fold({formWithErrors =>
      BadRequest(views.html.editItems(Item.items, formWithErrors))
    }, {item =>
      Item.items = Item.items.filter( x => x.name.toLowerCase != item.name);
      Item.items += item
      Redirect(routes.ItemApp.editItem)})
  }


}
