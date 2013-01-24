package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import views.html.*;
import models.ClassQuery;
import global.Global;

import indexing.JarIndexer;
import indexing.QueryResult;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;


public class JarIndex extends Controller {

  static Form<ClassQuery> classQueryForm = form(ClassQuery.class);
  
  public static Result index() {
    return ok(jarIndex.render(false, null, null, classQueryForm));
  }

  public static Result search() {
    ArrayList<String[]> jars = new ArrayList<String[]>();
    ArrayList<String> classes = new ArrayList<String>();
    classQueryForm = classQueryForm.bindFromRequest();
    /*String[] classes = {
      "com.linkedin.network.server.rest.impl.EdgeResource"
    };
    String[][] jars = {
      {"cloud-network-server-1.1.52-SNAPSHOT.jar",
      "cloud-network-server-1.1.53-SNAPSHOT.jar",
      "cloud-network-server-1.1.54-SNAPSHOT.jar",
      "cloud-network-server-1.1.55-SNAPSHOT.jar",
      "cloud-network-server-1.1.56-SNAPSHOT.jar",
      "cloud-network-server-1.1.57-SNAPSHOT.jar",
      "cloud-network-server-1.1.58-SNAPSHOT.jar",
      "cloud-network-server-1.1.59-SNAPSHOT.jar",
      "cloud-network-server-1.1.60-SNAPSHOT.jar",
      "cloud-network-server-1.1.61-SNAPSHOT.jar",
      "cloud-network-server-1.1.64-SNAPSHOT.jar",
      "cloud-network-server-1.1.68-SNAPSHOT.jar",
      "cloud-network-server-1.1.71-SNAPSHOT.jar",
      "cloud-network-server-1.1.72-SNAPSHOT.jar",
      "cloud-network-server-1.2.12-SNAPSHOT.jar",
      "cloud-network-server-1.2.14-SNAPSHOT.jar",
      "cloud-network-server-1.2.3-SNAPSHOT.jar",
      "cloud-network-server-1.2.5-SNAPSHOT.jar"
      }
    }; */

    ClassQuery query = classQueryForm.get();
    if(classQueryForm.hasErrors() && query.getSearchString() != null && !query.getSearchString().trim().equals("")) {
      return badRequest(jarIndex.render(false, null, null, classQueryForm));
    } else {
      Logger.info("Searching for " +  query.getSearchString());
      QueryResult result = JarIndexer.getIndexer().search(query.getSearchString());
      for(Entry<String, ArrayList<String>> entry : result.getMapAsSet()) {
	classes.add((String) entry.getKey());
	String[] values = new String[entry.getValue().size()];
	jars.add(entry.getValue().toArray(values));
      }
      
    }
    String[] classArray = new String[classes.size()];
    classes.toArray(classArray);
    String[][] jarArray = new String[jars.size()][];
    jars.toArray(jarArray);
    return ok(jarIndex.render(true, classArray, jarArray, classQueryForm));
  }
  
}
