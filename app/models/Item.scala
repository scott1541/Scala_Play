package models

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ ExecutionContext, Future }
import reactivemongo.api.{ DefaultDB, MongoConnection, MongoDriver }
import reactivemongo.bson.{
BSONDocumentWriter, BSONDocumentReader, Macros, document
}


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


object JsonFormats {
  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val feedFormat = Json.format[Item]
  implicit val userFormat = Json.format[ItemDelete]
}

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
    Item("Fan", "12in desk fan", 19.99, "12 months", "10%", "5dayshop"),
    Item("Mug", "Small mug", 5.99, "6 months", "10%", "Fine Mug Co"),
    Item("Headphones", "Big headphones", 29.99, "12 months", "10%", "Acme Electronics"),
    Item("Keyboard", "Full size Kb", 15.99, "12 months", "10%", "Acme Electronics"),
    Item("Mouse", "USB mouse", 8.50, "12 months", "10%", "Acme Electronics"),
    Item("Cutlery Set", "6x of each", 17.95, "n/a", "5%", "Fine Mug Co"),
    Item("16GB USB Drive", "16GB Flash drive", 25.95, "24 months", "10%", "5dayshop"),
    Item("HDMI Cable", "2m HDMI Cable", 9.99, "12 months", "7%", "Acme Electronics")
  )
}

object ItemDelete {
  val deleteItemForm = Form(
    mapping(
      "name" -> nonEmptyText
    )(ItemDelete.apply)(ItemDelete.unapply)
  )
}
