package rpg.common.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler {
    
    private static Set<Message> messages = ConcurrentHashMap.newKeySet();
    
    public static void addMessage(Message message) {
        messages.add(message);
    }
    
    public static Set<Message> getMessages() {
        return messages;
    }
    
}
