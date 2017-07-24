package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def testpage = Action {
    Ok(views.html.index("Hello World!"))
  }

  def list = Action {
    Ok(views.html.test("List"))
  }

  def show = Action {
    Ok(views.html.test("show"))
  }

}