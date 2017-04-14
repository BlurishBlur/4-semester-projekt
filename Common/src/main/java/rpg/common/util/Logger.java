package rpg.common.util;

public class Logger {
    
    public static void log(String message) {
        System.out.println(message);
    }
    
    public static void log(String message, Exception exception) {
        log(message + "\n");
        exception.printStackTrace();
    }
    
}
