package rpg.gameengine.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.util.Vector;
import rpg.common.world.World;

public class Camera {

    private OrthographicCamera camera;
    private Entity target;
    private float panTime;

    public Camera(float viewportWidth, float viewportHeight) {
        camera = new OrthographicCamera(viewportWidth, viewportHeight);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    public Camera(float viewportWidth, float viewportHeight, Entity target) {
        this(viewportWidth, viewportHeight);
        this.target = target;
    }
    
    public Entity getTarget() {
        return target;
    }

    public Matrix4 getProjection() {
        return camera.combined;
    }

    public void setPosition(float x, float y) {
        camera.position.set(x, y, 0);
    }

    public void update(GameData gameData, World world) {
        if (target != null) {
            camera.viewportWidth = gameData.getDisplayWidth() / gameData.getCameraZoom();
            camera.viewportHeight = gameData.getDisplayHeight() / gameData.getCameraZoom();
            if (gameData.isChangingRoom()) {
                changeRoom(gameData, world);
            }
            else {
                followTarget(gameData, world);
            }
        }
        camera.update();
    }

    public void initializeRoomChange(World world) {
        world.setCurrentRoom(world.getPlayer().getWorldPosition());
        Vector worldVelocity = target.getWorldVelocity();
        if (worldVelocity.getX() > 0) {
            target.getRoomPosition().setX(target.getWidth() / 2);
            camera.position.set(-camera.viewportWidth / 2, camera.position.y, 0);
        }
        else if (worldVelocity.getX() < 0) {
            target.getRoomPosition().setX(world.getCurrentRoom().getWidth() - (target.getWidth() / 2));
            camera.position.set(world.getCurrentRoom().getWidth() + camera.viewportWidth / 2, camera.position.y, 0);
        }
        else if (worldVelocity.getY() > 0) {
            target.getRoomPosition().setY(target.getHeight() / 2);
            camera.position.set(camera.position.x, -camera.viewportHeight / 2, 0);
        }
        else if (worldVelocity.getY() < 0) {
            target.getRoomPosition().setY(world.getCurrentRoom().getHeight() - (target.getWidth() / 2));
            camera.position.set(camera.position.x, world.getCurrentRoom().getHeight() + camera.viewportHeight / 2, 0);
        }
        panTime = 1;
        camera.update();
    }

    private void changeRoom(GameData gameData, World world) {
        if (target.getWorldVelocity().getX() > 0) {
            camera.position.interpolate(new Vector3(camera.viewportWidth / 2, camera.position.y, 0), 1.0f * gameData.getDeltaTime(), Interpolation.pow2InInverse);
        }
        else if (target.getWorldVelocity().getX() < 0) {
            camera.position.interpolate(new Vector3(world.getCurrentRoom().getWidth() - camera.viewportWidth / 2, camera.position.y, 0), 1.0f * gameData.getDeltaTime(), Interpolation.pow2InInverse);
        }
        else if (target.getWorldVelocity().getY() > 0) {
            camera.position.interpolate(new Vector3(camera.position.x, camera.viewportHeight / 2, 0), 1.0f * gameData.getDeltaTime(), Interpolation.pow2InInverse);
        }
        else if (target.getWorldVelocity().getY() < 0) {
            camera.position.interpolate(new Vector3(camera.position.x, world.getCurrentRoom().getHeight() - camera.viewportHeight / 2, 0), 1.0f * gameData.getDeltaTime(), Interpolation.pow2InInverse);
        }
        panTime -= gameData.getDeltaTime();
        if (panTime < 0) {
            gameData.setIsChangingRoom(false);
        }
    }

    private void followTarget(GameData gameData, World world) {
        camera.position.lerp(new Vector3(target.getRoomPosition().getX(), target.getRoomPosition().getY(), 0), 2.5f * gameData.getDeltaTime());
        handleEdgeCollision(world);
    }

    private void handleEdgeCollision(World world) {
        if (camera.position.x - camera.viewportWidth / 2 < 0) {
            camera.position.set(0 + camera.viewportWidth / 2, camera.position.y, 0);
        }
        else if (camera.position.x + camera.viewportWidth / 2 > world.getCurrentRoom().getWidth()) {
            camera.position.set(world.getCurrentRoom().getWidth() - camera.viewportWidth / 2, camera.position.y, 0);
        }
        if (camera.position.y - camera.viewportHeight / 2 < 0) {
            camera.position.set(camera.position.x, 0 + camera.viewportHeight / 2, 0);
        }
        else if (camera.position.y + camera.viewportHeight / 2 > world.getCurrentRoom().getHeight()) {
            camera.position.set(camera.position.x, world.getCurrentRoom().getHeight() - camera.viewportHeight / 2, 0);
        }
    }

}
