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

object GetStarted {
  // My settings (see available connection options)
  val mongoUri = "mongodb://localhost:27017/" //mydb?authMode=scram-sha1"

  import ExecutionContext.Implicits.global // use any appropriate context

  // Connect to the database: Must be done only once per application
  val driver = MongoDriver()
  val parsedUri = MongoConnection.parseURI(mongoUri)
  val connection = parsedUri.map(driver.connection(_))

  // Database and collections: Get references
  val futureConnection = Future.fromTry(connection)
  def db1: Future[DefaultDB] = futureConnection.flatMap(_.database("itemsdb"))
  //def db2: Future[DefaultDB] = futureConnection.flatMap(_.database("anotherdb"))
  def itemCollection = db1.map(_.collection("items"))

  // Write Documents: insert or update

  implicit def itemWriter: BSONDocumentWriter[Item] = Macros.writer[Item]
  // or provide a custom one

  def createItem(item: Item): Future[Unit] =
    itemCollection.flatMap(_.insert(item).map(_ => {})) // use itemWriter

  def updateItem(item: Item): Future[Int] = {
    val selector = document(
      "name" -> item.name,
      "desc" -> item.desc,
      "price" -> item.price,
      "warranty" -> item.warranty,
      "discount" -> item.discount,
      "seller" -> item.seller
    )

    // Update the matching person
    itemCollection.flatMap(_.update(selector, item).map(_.n))
  }

  implicit def itemReader: BSONDocumentReader[Item] = Macros.reader[Item]
  // or provide a custom one

  def findItemByName(name: String): Future[List[Item]] =
    itemCollection.flatMap(_.find(document("name" -> name)). // query builder
      cursor[Item]().collect[List]()) // collect using the result cursor
  // ... deserializes the document using personReader

  // Custom persistent types
  case class Item (
 name: String,
 desc: String,
 price: Double,
 warranty: String,
 discount: String,
 seller: String
                  )

}
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
