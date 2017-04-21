package rpg.currency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.events.Event;
import rpg.common.events.EventType;
import rpg.common.services.IEntityProcessingService;
import rpg.common.util.Message;
import rpg.common.util.MessageHandler;
import rpg.common.world.World;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class CurrencySystem implements IEntityProcessingService {

    private int currentCurrency;

    @Override
    public void process(GameData gameData, World world) {
        for (Event event : gameData.getEvents(EventType.ENEMY_DIED)) {
            createRandomCurrency(event.getEntity(), world);
            System.out.println("creating currency");
        }
        for (Entity currency : world.getCurrentRoom().getEntities(Currency.class)) {
            if (pickup(currency, world.getPlayer())) {
                currentCurrency += ((Currency) currency).getValue();
                //world.getPlayer().addCurrency(((Currency) currency).getValue());
                sendPickupMessage((Currency) currency);
                world.getCurrentRoom().removeEntity(currency);
                gameData.addEvent(new Event(EventType.COIN_PICKUP, currency));
            }
        }
        sendMessage(world);
    }

    private void sendPickupMessage(Currency currency) {
        MessageHandler.addMessage(new Message("+" + currency.getValue() + " gold", 3, currency));
    }

    private void sendMessage(World world) {
        MessageHandler.addMessage(new Message("Currency: " + currentCurrency,
                0, 600, world.getCurrentRoom().getHeight() - 40));
    }

    private boolean pickup(Entity currency, Entity player) {
        float a = currency.getRoomPosition().getX() - player.getRoomPosition().getX();
        float b = currency.getRoomPosition().getY() - player.getRoomPosition().getY();

        double c = Math.sqrt((double) (a * a) + (double) (b * b));

        return c < currency.getWidth() / 2 + player.getWidth() / 2;

        //return Math.sqrt(Math.pow(entity1.getX() - entity2.getX(), 2) + Math.pow(entity1.getY() - entity2.getY(), 2)) < entity1.getRadius() + entity2.getRadius();
    }

    private void createRandomCurrency(Entity entity, World world) {
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < (Math.random() * 10) + 1; i++) {
            pool.execute(() -> {
                world.getCurrentRoom().addEntity(createCurrency(entity));
            });
        }
    }

    private Currency createCurrency(Entity parent) {
        Currency currency = new Currency();
        currency.getRoomPosition().set(parent.getRoomPosition().getX() + (int) (Math.random() * 50) - 25, parent.getRoomPosition().getY() + (int) (Math.random() * 50) - 25);
        currency.getWorldPosition().set(parent.getWorldPosition());
        currency.setSize(10, 10);
        currency.setSpritePath("rpg/gameengine/currency.png");
        currency.setValue(1);
        //currency.setCurrentFrame(1);
        //currency.setMaxFrames(3);
        //currency.setHasHpBar(true);
        //currency.getSounds().put("GRASS", "rpg/gameengine/Footstep Grass 2.wav");
        return currency;
    }

}
