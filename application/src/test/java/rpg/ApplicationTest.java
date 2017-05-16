package rpg;

import java.io.IOException;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import junit.framework.Test;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.autoupdate.silentupdate.UpdateHandler;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;

public class ApplicationTest extends NbTestCase {

    private static final String UPDATES_FILE = "C:\\Users\\Niels\\Dropbox\\Java projekter\\4-semester-projekt\\netbeans_site\\updates.xml";
    private static final String UPDATES_FILE_ADD_ENEMY = "C:\\Users\\Niels\\Dropbox\\Java projekter\\4-semester-projekt\\application\\src\\test\\resources\\addenemy\\updates.xml";
    private static final String UPDATES_FILE_REMOVE_ENEMY = "C:\\Users\\Niels\\Dropbox\\Java projekter\\4-semester-projekt\\application\\src\\test\\resources\\removeenemy\\updates.xml";

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

    public void testApplication() throws IOException {
        List<IEntityProcessingService> processors = new CopyOnWriteArrayList<>();
        List<IGamePluginService> plugins = new CopyOnWriteArrayList<>();
        copy(get(UPDATES_FILE_REMOVE_ENEMY), get(UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate(processors, plugins);

        assertEquals("No processors", 0, processors.size());
        assertEquals("No plugins", 0, plugins.size());

        copy(get(UPDATES_FILE_ADD_ENEMY), get(UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate(processors, plugins);

        assertEquals("One processor", 1, processors.size());
        assertEquals("One plugin", 1, plugins.size());

        copy(get(UPDATES_FILE_REMOVE_ENEMY), get(UPDATES_FILE), REPLACE_EXISTING);
        waitForUpdate(processors, plugins);

        assertEquals("No processors", 0, processors.size());
        assertEquals("No plugins", 0, plugins.size());
    }

    private void waitForUpdate(List<IEntityProcessingService> processors, List<IGamePluginService> plugins) {
        try {
            Thread.sleep(10000);
            processors.clear();
            processors.addAll(Lookup.getDefault().lookupAll(IEntityProcessingService.class));

            plugins.clear();
            plugins.addAll(Lookup.getDefault().lookupAll(IGamePluginService.class));
        }
        catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

}
