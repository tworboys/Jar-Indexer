import play.*;
import indexing.JarIndexer;
import java.io.File;

public class Global extends GlobalSettings {
  public static int test = 1337;

  @Override
  public void onStart(Application app) {
    Logger.info("Application has started");
    Logger.info("Indexing Jars...");
    JarIndexer.getIndexer().indexRepository(new File("/tmp/local-repo/"));
    Logger.info("Done Indexing Jars...");
    Logger.info(JarIndexer.getIndexer().getNumJarsIndexed() + " jars indexed and " + JarIndexer.getIndexer().getNumClassesIndexed() + " classes indexed");
  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }  
    
}
