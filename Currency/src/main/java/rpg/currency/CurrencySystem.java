package rpg.currency;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.events.Event;
import rpg.common.events.EventType;
import rpg.common.services.IEntityProcessingService;
import rpg.common.world.World;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class CurrencySystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for(Event event : gameData.getEvents(EventType.ENEMY_DIED)) {
            createRandomCurrency(event.getEntity(), world);
            gameData.removeEvent(event);
            System.out.println("creating currency");
        }
        for(Entity currency : world.getCurrentRoom().getEntities(Currency.class)) {
            if(pickup(currency, world.getPlayer())) {
                world.getPlayer().addCurrency(((Currency) currency).getValue());
                System.out.println("picked up: " + ((Currency) currency).getValue());
                world.getCurrentRoom().removeEntity(currency);
            }
        }
    }
    
    private boolean pickup(Entity currency, Entity player) {
        float a = currency.getRoomPosition().getX() - player.getRoomPosition().getX();
        float b = currency.getRoomPosition().getY() - player.getRoomPosition().getY();

        double c = Math.sqrt((double) (a * a) + (double) (b * b));

        return c < currency.getWidth() / 2 + player.getWidth() / 2;

        //return Math.sqrt(Math.pow(entity1.getX() - entity2.getX(), 2) + Math.pow(entity1.getY() - entity2.getY(), 2)) < entity1.getRadius() + entity2.getRadius();
    }
    
    private void createRandomCurrency(Entity entity, World world) {
        for (int i = 0; i < (Math.random() * 3) + 1; i++) {
            world.getCurrentRoom().addEntity(createCurrency(entity));
        }
    }
    
    private Currency createCurrency(Entity entity) {
        Currency currency = new Currency();
        currency.getRoomPosition().set(entity.getRoomPosition().getX() + (int) (Math.random() * 10) - 5, entity.getRoomPosition().getY() + (int) (Math.random() * 10) - 5);
        currency.getWorldPosition().set(entity.getWorldPosition());
        currency.setWidth(10);
        currency.setHeight(10);
        currency.setSpritePath("rpg/gameengine/currency.png");
        currency.setValue((int) (Math.random() * 5) + 1);
        //currency.setCurrentFrame(1);
        //currency.setMaxFrames(3);
        //currency.setHasHpBar(true);
        //currency.getSounds().put("GRASS", "rpg/gameengine/Footstep Grass 2.wav");
        return currency;
    }
    
}
