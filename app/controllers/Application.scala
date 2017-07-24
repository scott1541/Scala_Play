package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def testpage(input: String) = Action {
    input match {
      case "notfound" => NotFound
      case "pagenotfound" => NotFound(<h1>Page Not Found</h1>)
      case "badrequest" => BadRequest
      case "oops" => InternalServerError
      case _ => Ok(views.html.index("ALL OK!"))
    }
  }

  def default(input: String) = Action {
    Ok(views.html.test(input))
  }

  def list = Action {
    Ok(views.html.test("List"))
  }

  def show(id: Int) = Action {
    Ok(views.html.test("ID number: " + id))
  }

  //def redirect = Action {
    //Redirect("/test/all")
  //}

  def redirect = Action {
    Redirect(routes.Application.plannedUpdates())
  }

  def plannedUpdates = Action {
    Ok("Planned updates are in progress!")
  }

}