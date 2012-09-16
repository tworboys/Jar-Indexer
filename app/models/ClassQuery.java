package models;

import play.data.validation.Constraints.*;

public class ClassQuery {

  String filter = null;
  String className = null;
  public String searchString = null;

  public ClassQuery() {
    filter = null;
    className = null;
    searchString = null;
  }

  public void parseUserInput(String searchString) {
    this.searchString = searchString;
    String[] stringParts = searchString.split("\\.");
    className = stringParts[stringParts.length - 1];

    filter = stringParts[0];
    for(int i = 1; i < stringParts.length - 1; ++i)
      filter += "." + stringParts[i];
  }

  public String getSearchString() {
    return searchString;
  }

  public String getFilter() {
    if(filter == null) parseUserInput(searchString);
    return filter;
  }

  public String getClassName() {
    if(className == null) parseUserInput(searchString);
    return className;
  }

  public String toString() {
    return "filter = " + getFilter() + "\nclassName = " + getClassName() + "\nsearchString = " + getSearchString();
  }
}

