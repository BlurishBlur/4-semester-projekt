package rpg.experience;

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
public class ExperienceSystem implements IEntityProcessingService {
    
    private int experienceToNextLevel = 100;

    @Override
    public void process(GameData gameData, World world) {
        for(Event event : gameData.getEvents(EventType.ENEMY_DIED)) {
            createRandomExperienceOrbs(event.getEntity(), world);
            System.out.println("creating xp");
        }
        for(Entity experience : world.getCurrentRoom().getEntities(Experience.class)) {
            experience.increaseFrame(gameData.getDeltaTime() * 10);
            if(pickup(experience, world.getPlayer())) {
                world.getPlayer().addExperience(((Experience) experience).getValue());
                System.out.println("picked up: " + ((Experience) experience).getValue() + " experience");
                world.getCurrentRoom().removeEntity(experience);
                gameData.addEvent(new Event(EventType.XP_PICKUP, experience));
            }
        }
        checkExperience(world);
    }
    
    private void checkExperience(World world) {
        if(world.getPlayer().getExperience() > experienceToNextLevel) {
            world.getPlayer().levelUp();
            MessageHandler.addMessage(new Message("Level up!", 4, 
                    world.getPlayer().getRoomPosition().getX() - world.getPlayer().getWidth() / 2, world.getPlayer().getRoomPosition().getY() + world.getPlayer().getHeight()));
            experienceToNextLevel += world.getPlayer().getLevel() * 100;
        }
        sendMessages(world);
    }
    
    private void sendMessages(World world) {
        Entity player = world.getPlayer();
        MessageHandler.addMessage(new Message("Level: " + player.getLevel(), 
                0, 1150, world.getCurrentRoom().getHeight() - 25));
        MessageHandler.addMessage(new Message("Exp: " + player.getExperience() + "/" + experienceToNextLevel, 
                0, 1150, world.getCurrentRoom().getHeight() - 65));
    }
    
    private boolean pickup(Entity experience, Entity player) {
        float a = experience.getRoomPosition().getX() - player.getRoomPosition().getX();
        float b = experience.getRoomPosition().getY() - player.getRoomPosition().getY();

        double c = Math.sqrt((double) (a * a) + (double) (b * b));

        return c < experience.getWidth() / 2 + player.getWidth() / 2;

        //return Math.sqrt(Math.pow(entity1.getX() - entity2.getX(), 2) + Math.pow(entity1.getY() - entity2.getY(), 2)) < entity1.getRadius() + entity2.getRadius();
    }
    
    private void createRandomExperienceOrbs(Entity entity, World world) {
        for (int i = 0; i < (Math.random() * 10) + 10; i++) {
            world.getCurrentRoom().addEntity(createExperienceOrb(entity));
        }
    }
    
    private Experience createExperienceOrb(Entity entity) {
        Experience experience = new Experience();
        experience.getRoomPosition().set(entity.getRoomPosition().getX() + (int) (Math.random() * 50) - 25, entity.getRoomPosition().getY() + (int) (Math.random() * 50) - 25);
        experience.getWorldPosition().set(entity.getWorldPosition());
        experience.setSize(15, 15);
        experience.setSpritePath("rpg/gameengine/xp.atlas");
        experience.setValue(10);
        experience.setCurrentFrame(1);
        experience.setMaxFrames(12);
        
        //experience.getSounds().put("GRASS", "rpg/gameengine/Footstep Grass 2.wav");
        return experience;
    }
    
}
