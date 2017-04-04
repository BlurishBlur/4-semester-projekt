package rpg.currency;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.services.IEntityProcessingService;
import rpg.common.world.World;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class CurrencySystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
