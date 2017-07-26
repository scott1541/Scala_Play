package models

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import scala.collection.mutable.ArrayBuffer

case class Item (
  name: String,
  desc: String,
  price: Double,
  warranty: String,
  discount: String,
  seller: String
)

case class ItemDelete (
  name: String
)

object Item {

  val createItemForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "desc" -> of(stringFormat),
      "price" -> of(doubleFormat),
      "warranty" -> of(stringFormat),
      "discount" -> of(stringFormat),
      "seller" -> of(stringFormat)
    )(Item.apply)(Item.unapply)
  )

  var items = ArrayBuffer(
    Item("Fan", "12 inch desk fan", 19.99, "12 months", "10%", "Go To CheapBay Co"),
    Item("Mug", "Small ceramic mug", 5.99, "6 months", "10%", "Fine Mug Co"),
    Item("Headphones", "Big headphones", 29.99, "12 months", "10%", "Acme Electronics"),
    Item("Keyboard", "Full size keyboard", 15.99, "12 months", "10%", "Acme Electronics")
  )
}

object ItemDelete {
  val deleteItemForm = Form(
    mapping(
      "name" -> nonEmptyText
    )(ItemDelete.apply)(ItemDelete.unapply)
  )

}
