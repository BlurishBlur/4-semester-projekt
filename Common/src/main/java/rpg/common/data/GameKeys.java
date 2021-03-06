package rpg.common.data;

import java.io.Serializable;

public class GameKeys implements Serializable {
    
    private static boolean[] keys;
    private static boolean[] pressedKeys;
    private static final int NUMBER_OF_KEYS = 16;
    public static final int W = 0;
    public static final int A = 1;
    public static final int S = 2;
    public static final int D = 3;
    public static final int UP = 4;
    public static final int LEFT = 5;
    public static final int DOWN = 6;
    public static final int RIGHT = 7;
    public static final int N = 8;
    public static final int M = 9;
    public static final int F1 = 10;
    public static final int SHIFT = 11;
    public static final int H = 12;
    public static final int K = 13;
    public static final int ONE = 14;
    public static final int TWO = 15;
    
    public GameKeys() {
        keys = new boolean[NUMBER_OF_KEYS];
        pressedKeys = new boolean[NUMBER_OF_KEYS];
    }
    
    public void update() {
        for (int i = 0; i < NUMBER_OF_KEYS; i++) {
            pressedKeys[i] = keys[i];
        }
    }
    
    public void setKey(int key, boolean pressed) {
        keys[key] = pressed;
    }
    
    public boolean isDown(int key) {
        return keys[key];
    }
    
    public boolean isPressed(int key) {
        return keys[key] && !pressedKeys[key];
    }
    
}
