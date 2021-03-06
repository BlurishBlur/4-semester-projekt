package rpg.zoom;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.world.World;
import rpg.common.services.IEntityProcessingService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class ZoomControlSystem implements IEntityProcessingService {
    
    private static final float ZOOM_SPEED = 0.05f;

    @Override
    public void process(GameData gameData, World world) {
        if (gameData.getKeys().isPressed(GameKeys.N)) {
            if (gameData.getCameraZoom() < 10.99f) {
                gameData.setCameraZoom(gameData.getCameraZoom() + ZOOM_SPEED);
            }
        }
        if (gameData.getKeys().isPressed(GameKeys.M)) {
            if (gameData.getCameraZoom() > 1.01f) {
                gameData.setCameraZoom(gameData.getCameraZoom() - ZOOM_SPEED);
            }
        }
    }

}
