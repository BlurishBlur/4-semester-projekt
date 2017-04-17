package org.netbeans.modules.autoupdate.silentupdate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.netbeans.modules.autoupdate.silentupdate.gui.GUILauncher;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

public class UpdateActivator extends ModuleInstall {

    private final ScheduledExecutorService exector = Executors.newScheduledThreadPool(1);

    @Override
    public void restored() {
        exector.scheduleAtFixedRate(doCheck, 5000, 5000, TimeUnit.MILLISECONDS);
        new Thread(() -> {
            while (UpdateHandler.getSilentUpdateProvider().getUpdateUnits().isEmpty() || UpdateHandler.getLocallyInstalled().size() != UpdateHandler.getSilentUpdateProvider().getUpdateUnits().size()) {
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            GUILauncher.initiate(null);
        }).start();
    }

    private static final Runnable doCheck = new Runnable() {
        @Override
        public void run() {
            if (UpdateHandler.timeToCheck()) {
                UpdateHandler.checkAndHandleUpdates();
            }
        }
    };

    @Override
    public void uninstalled() {
        super.uninstalled();
    }

}
