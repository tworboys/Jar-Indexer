package indexing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;

public class QueryResult implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 3590275913967888076L;
  private HashMap<String, ArrayList<String>> map;

  public QueryResult() {
    map = new HashMap<String, ArrayList<String>>();
  }

  public void addJarToResult(String _class, String jar) {
    if(map.containsKey(_class)){
      ((ArrayList<String>) map.get(_class)).add(jar);
    } else {
      ArrayList tmp = new ArrayList<String>();
      tmp.add(jar);
      map.put(_class, tmp);
    }
  }

  public Set<Entry<String, ArrayList<String>>> getMapAsSet() {
    return map.entrySet();
  }

  public int size() {
    return map.size();
  }
}
