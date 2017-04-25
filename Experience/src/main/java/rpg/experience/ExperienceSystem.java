package rpg.experience;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
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

    private int currentExperience = 0;
    private int level = 1;
    private int experienceToNextLevel = calculateExperienceToNextLevel();

    @Override
    public void process(GameData gameData, World world) {
        if (gameData.getKeys().isPressed(GameKeys.K)) {
            currentExperience = calculateExperienceToNextLevel() + 10;
            //world.getPlayer().addExperience(world.getPlayer().getLevel() * 110);
        }
        for (Event event : gameData.getEvents(EventType.ENEMY_DIED)) {
            createRandomExperienceOrbs(event.getEntity(), world);
        }
        for (Entity experience : world.getCurrentRoom().getEntities(Experience.class)) {
            experience.increaseFrame(gameData.getDeltaTime() * 5);
            if (pickup(experience, world.getPlayer())) {
                currentExperience += ((Experience) experience).getValue();
                //world.getPlayer().addExperience(((Experience) experience).getValue());
                sendPickupMessage((Experience) experience);
                world.getCurrentRoom().removeEntity(experience);
                gameData.addEvent(new Event(EventType.XP_PICKUP, experience));
            }
        }
        checkExperience(world, gameData);
        sendMessages(world);
    }

    private void sendPickupMessage(Experience experience) {
        MessageHandler.addMessage(new Message("+" + experience.getValue() + " exp", 3, experience));
    }

    private void checkExperience(World world, GameData gameData) {
        if (currentExperience > experienceToNextLevel) {
            level++;
            if (world.getPlayer() != null) {
                MessageHandler.addMessage(new Message("Level up!", 5, world.getPlayer()));
            }
            else {
                MessageHandler.addMessage(new Message("Level up!", 5, 1100, world.getCurrentRoom().getHeight() - 45));
            }
            experienceToNextLevel = calculateExperienceToNextLevel();
            gameData.addEvent(new Event(EventType.LEVEL_UP, world.getPlayer())); //Det her skal lige kigges på - hvorfor skal player med
        }
    }

    private int calculateExperienceToNextLevel() {
        return level * 100;
    }

    private void sendMessages(World world) {
        MessageHandler.addMessage(new Message("Level: " + level,
                0, 1150, world.getCurrentRoom().getHeight() - 25));
        MessageHandler.addMessage(new Message("Exp: " + currentExperience + "/" + experienceToNextLevel,
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
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < (Math.random() * 10) + 1; i++) {
            pool.execute(() -> {
                world.getCurrentRoom().addEntity(createExperienceOrb(entity));
            });
        }
    }

    private Experience createExperienceOrb(Entity parent) {
        Experience experience = new Experience();
        experience.getRoomPosition().set(parent.getRoomPosition().getX() + (int) (Math.random() * 50) - 25, parent.getRoomPosition().getY() + (int) (Math.random() * 50) - 25);
        experience.getWorldPosition().set(parent.getWorldPosition());
        experience.setSize(15, 15);
        experience.setSpritePath("rpg/gameengine/xp.atlas");
        experience.setValue(1);
        experience.setCurrentFrame(1);
        experience.setMaxFrames(5);

        //experience.getSounds().put("GRASS", "rpg/gameengine/Footstep Grass 2.wav");
        return experience;
    }

}
