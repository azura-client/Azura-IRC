package best.azura.irc.server.chat;

import best.azura.irc.server.packets.client.ChatMessagePacket;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryChat implements IChat {
    public static Integer HISTORY_SIZE = 10;

    private volatile Map<String, Room> subscribers = new ConcurrentHashMap<>();
    private Integer historySize;

    public InMemoryChat(Integer historySize) {
        this.historySize = historySize;
    }

    public synchronized void subscribe(String channelName, UUID userUUID) {

        if (!subscribers.containsKey(channelName)) {
            subscribers.put(channelName, new Room(historySize));
        }

        Room room = subscribers.get(channelName);
        if (room.hasUser(userUUID)) {
            return;
        }

        room.addUser(userUUID);
    }

    public synchronized void unsubscribe(String channelName, UUID userUUID) {
        Room room = subscribers.get(channelName);
        if (room == null) {
            return;
        }

        room.removeUser(userUUID);
    }

    public synchronized Collection<UUID> getSubscribers(String channelName) {
        if (!subscribers.containsKey(channelName)) {
            return new ArrayList<>();
        }

        return subscribers.get(channelName).subscribers();
    }

    public void addToHistory(String channelName, ChatMessagePacket msg) {
        Room room = subscribers.get(channelName);
        if (room == null) {
            room = new Room(historySize);
            subscribers.put(channelName, room);
        }

        room.addToHistory(msg);
    }

    public List<ChatMessagePacket> getHistory(String channelName) {
        Room room = subscribers.get(channelName);
        if (room == null) {
            return new ArrayList<>();
        }

        return room.getHistory();
    }

}
