package indexing;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import indexing.Pair;
import indexing.QueryResult;
import play.*;

/**
 * Indexes the jars in a repository
 * @author sshankar
 *
 */
public class JarIndexer {
  int numJarsIndexed = 0;
  int numClassesIndexed = 0;

  public int getNumJarsIndexed() {
    return numJarsIndexed;
  }

  public int getNumClassesIndexed() {
    return numClassesIndexed;
  }

  static JarIndexer indexer = null;

  public static JarIndexer getIndexer() {
    if(indexer == null)
        indexer = new JarIndexer();

    return indexer;
  }

  public QueryResult search(String query) {
    return idxMgr.search(query);
  }
  

  public void indexRepository(File path) {
    if(path.isDirectory()) {
      File[] nodes = path.listFiles();
      for(int i = 0; i<nodes.length; i++) {
        indexRepository(nodes[i]);
      }
    }
    else if (path.isFile()){
      indexJar(path);
    }
    else {
      // TODO: Crap throw an exception - invalid file system object
    }
  }

  void indexJar(File node) {
    // Ignore if it is not a jar file
    if(!ptrnJar.matcher(node.getName()).find()) return; 
    numJarsIndexed++;

    JarFile jar;
    try {
      jar = new JarFile(node);
    }
    catch(IOException ioe) {
      //TODO: Log error to file - and silently continue
      return;
    }
    catch(SecurityException se) {
      //TODO: Log error to file - and silently continue
      return;
    }
    
    try {
      _indexJar(jar);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace(); // Log it
    }
  }
  
  void _indexJar(JarFile jar) throws IOException {
    
    Enumeration enm = jar.entries();
    while (enm.hasMoreElements()) {
      JarEntry file = (JarEntry) enm.nextElement();
      if(ptrnCls.matcher(file.getName()).find()) {
	String[] jarFileNameSplit = jar.getName().split("/");
	String[] classNameSplit = file.getName().split("/");
	StringBuilder className = new StringBuilder(file.getName().length() + classNameSplit.length);
        for(int i = 0; i < classNameSplit.length; i++) {
	  if(i == classNameSplit.length - 1)
	    className.append(classNameSplit[i]);
	  else 
	    className.append(classNameSplit[i] + ".");
        }
	String fullClassName = className.toString();
        addIndex(fullClassName, jarFileNameSplit[jarFileNameSplit.length - 1]);
      }
    }
  }
  
  private void addIndex(String className, String jarName) {
    // Ignore Anonymous classes
    if(ptrnAnonCls.matcher(className).find()) return;
    Matcher m = ptrnClsName.matcher(className);
    if(m.find()) {
      String clsName = m.group(1);
      numClassesIndexed++;
      idxMgr.addIndex(clsName, new Pair<String, String>(className, jarName));
    }
  }
  
  private IndexManager idxMgr = new IndexManager(); 
  private Pattern ptrnJar = Pattern.compile("\\.jar$");
  private Pattern ptrnCls = Pattern.compile("\\.class$");
  private Pattern ptrnAnonCls = Pattern.compile("\\$\\d+\\.class$");
  private Pattern ptrnClsName = Pattern.compile("(\\w+)\\.class$");
}
