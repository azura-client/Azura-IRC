package best.azura.irc.server.chat;

import best.azura.irc.server.packets.client.ChatMessagePacket;

import java.util.*;

public class Room {
    private Integer historySize;
    private List<ChatMessagePacket> entries = Collections.synchronizedList(new ArrayList<>());
    private Collection<UUID> users = Collections.synchronizedList(new ArrayList<>());

    public Room(Integer historySize) {
        this.historySize = historySize;
    }

    public synchronized void addToHistory(ChatMessagePacket msg) {
        entries.add(msg);

        if (entries.size() > historySize) {
            entries.remove(0);
        }
    }

    public synchronized List<ChatMessagePacket> getHistory() {
        return entries;
    }

    public synchronized void addUser(UUID userUUID) {
        users.add(userUUID);
    }

    public void removeUser(UUID userUUID) {
        users.remove(userUUID);
    }

    public Boolean hasUser(UUID userUUID) {
        return users.contains(userUUID);
    }

    public Collection<UUID> subscribers() {
        return users;
    }
}
