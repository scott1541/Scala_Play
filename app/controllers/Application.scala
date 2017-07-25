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

  def same = Action {
    Ok(views.html.test("First Route"))
  }
  def sameO = Action {
    Ok(views.html.test("Second Route"))
  }

  def plannedUpdates = Action {
    Ok("Planned updates are in progress!")
  }

  def cookie = Action {
    Ok("Hello John").withSession("connected" -> "john@gmail.com")
  }
  def cookieChange = Action {request =>
    Ok("Change cookie").withSession(request.session + ("saidhello" -> "yes"))
  }

  def connected = Action {
    {request => request.session.get("connected").map {user =>
      Ok("Hello  " + user)}.getOrElse(Unauthorized("not authorised"))}
  }
  def delCookie = Action {
    Ok("Logout").withNewSession
  }
  def dumpCookie = TODO/*Action {
    {request => request.session.get("connected").foreach(item => item) }
  }*/

  def flash = Action {
    Redirect("/misc/fl").flashing("success" -> "Flash created")
  }

  def flashr = Action {
    Ok("Hello John")
  }

  def implement = TODO
}