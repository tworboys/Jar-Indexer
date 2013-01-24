/**
 * 
 */
package indexing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import indexing.QueryResult;
import play.*;

/**
 * Manages the Index 
 * @author sshankar
 */
public class IndexManager implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = -28504496909274609L;
  
  
  public void addIndex(String className, Pair<String, String> entry) {
    if(!index.containsKey(className)) {
      index.put(className, new ArrayList<Pair<String, String>>());
    }
    index.get(className).add(entry);
  }
  
  //TODO: replace void it with an appropriate data structure
  public QueryResult search(String query) {
    
    //TODO: Extract class name and the package filter prefix from "query"
    String[] queryParts = query.split("\\.");
    String className = queryParts[queryParts.length - 1];
    if(className.equals("class") || className.equals("java") || className.equals("scala"))
	className = queryParts[queryParts.length - 2];

    QueryResult result = new QueryResult();
    if (!index.containsKey(className)) return result; //return "nothing"
    ArrayList<Pair<String, String>> res = index.get(className);

      for(Pair<String, String> pair : res) {
  	  result.addJarToResult(pair.getFirst().subSequence(0, pair.getFirst().length() - 6).toString(), pair.getSecond());
      }

    return result;

    
    // TODO: Apply packageFilter on res and return the results 
  }
  
  // The pair consists of FullClassName and the jar file name {Pair<FullClassName, JarFileName>}
  private HashMap<String, ArrayList<Pair<String, String>>> index = new HashMap<String, ArrayList<Pair<String, String>>>();
}
