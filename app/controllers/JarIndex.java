package controllers;

import play.data.Form;
import play.mvc.*;

import views.html.*;
import models.ClassQuery;

public class JarIndex extends Controller {

  static Form<ClassQuery> classQueryForm = form(ClassQuery.class);
  
  public static Result index() {
    String[] classes = {
    };
    String[][] jars = {
    };
    return ok(jarIndex.render(false, classes, jars, classQueryForm));
  }

  public static Result search() {
    classQueryForm = classQueryForm.bindFromRequest();
    if(classQueryForm.hasErrors()) {
      return badRequest(jarIndex.render(true, null, null, classQueryForm));
    } else {
      ClassQuery query = classQueryForm.get();
      System.out.println(query);
    }
    return redirect(routes.JarIndex.index());
  }
  
}
