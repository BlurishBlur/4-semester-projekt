package rpg;

import java.io.IOException;
import java.util.logging.Level;
import junit.framework.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;
import org.openide.util.Lookup;

public class ApplicationTest extends NbTestCase {

    
    private static final String ENEMY_FILE = "C:\\Users\\Antonio\\OneDrive\\Java Projekter\\4-semester-projekt\\application\\target\\with_enemy.xml";
    private static final String NO_ENEMY_FILE = "C:\\Users\\Antonio\\OneDrive\\Java Projekter\\4-semester-projekt\\application\\target\\without_enemy.xml";
    private static final String UPDATES_FILE = "C:\\Users\\Antonio\\OneDrive\\Java Projekter\\4-semester-projekt\\netbeans_site\\updates.xml";
    
    List<IEntityProcessingService> processors;
    List<IGamePluginService> plugins;
    
    public static Test suite() {
        return NbModuleSuite.createConfiguration(ApplicationTest.class).
                gui(false).
                failOnMessage(Level.WARNING). // works at least in RELEASE71
                failOnException(Level.INFO).
                enableClasspathModules(false). 
                clusters(".*").
                suite(); // RELEASE71+, else use NbModuleSuite.create(NbModuleSuite.createConfiguration(...))
    }

    public ApplicationTest(String n) {
        super(n);
    }

    public void testApplication() throws IOException, InterruptedException {
      /*  copy(get(NO_ENEMY_FILE), get(UPDATES_FILE), REPLACE_EXISTING);
        
        processors = new CopyOnWriteArrayList<>();
        plugins = new CopyOnWriteArrayList<>();
        waitForUpdate();
        assertEquals("No processors", 0, processors.size());
        assertEquals("No plugins", 0, plugins.size());
        
        copy(get(ENEMY_FILE), get(UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate();
        
        assertEquals("1 Processor", 1, processors.size());
        assertEquals("1 Plugin", 1, plugins.size());
        
        copy(get(NO_ENEMY_FILE), get(UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate();
        
        assertEquals("No processors", 0, processors.size());
        assertEquals("No plugins", 0, plugins.size());*/
    }
    
    private void waitForUpdate() throws InterruptedException {
        Thread.sleep(10000);
        processors.clear();
        processors.addAll(Lookup.getDefault().lookupAll(IEntityProcessingService.class));
        plugins.clear();
        plugins.addAll(Lookup.getDefault().lookupAll(IGamePluginService.class));
    }
}
