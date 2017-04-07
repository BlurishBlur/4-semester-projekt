package rpg.experience;

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
public class ExperienceSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for(Event event : gameData.getEvents(EventType.ENEMY_DIED)) {
            createRandomExperience(event.getEntity(), world);
            System.out.println("creating xp");
        }
        for(Entity experience : world.getCurrentRoom().getEntities(Experience.class)) {
            if(pickup(experience, world.getPlayer())) {
                world.getPlayer().addExperience(((Experience) experience).getValue());
                System.out.println("picked up: " + ((Experience) experience).getValue());
                world.getCurrentRoom().removeEntity(experience);
                gameData.addEvent(new Event(EventType.COIN_PICKUP, experience));
            }
        }
    }
    
    private boolean pickup(Entity experience, Entity player) {
        float a = experience.getRoomPosition().getX() - player.getRoomPosition().getX();
        float b = experience.getRoomPosition().getY() - player.getRoomPosition().getY();

        double c = Math.sqrt((double) (a * a) + (double) (b * b));

        return c < experience.getWidth() / 2 + player.getWidth() / 2;

        //return Math.sqrt(Math.pow(entity1.getX() - entity2.getX(), 2) + Math.pow(entity1.getY() - entity2.getY(), 2)) < entity1.getRadius() + entity2.getRadius();
    }
    
    private void createRandomExperience(Entity entity, World world) {
        for (int i = 0; i < (Math.random() * 10) + 1; i++) {
            world.getCurrentRoom().addEntity(createExperience(entity));
        }
    }
    
    private Experience createExperience(Entity entity) {
        Experience experience = new Experience();
        experience.getRoomPosition().set(entity.getRoomPosition().getX() + (int) (Math.random() * 50) - 25, entity.getRoomPosition().getY() + (int) (Math.random() * 50) - 25);
        experience.getWorldPosition().set(entity.getWorldPosition());
        experience.setSize(10, 10);
        experience.setSpritePath("rpg/gameengine/experience.png");
        experience.setValue(1);
        //experience.setCurrentFrame(1);
        //experience.setMaxFrames(3);
        //experience.setHasHpBar(true);
        //experience.getSounds().put("GRASS", "rpg/gameengine/Footstep Grass 2.wav");
        return experience;
    }
    
}
