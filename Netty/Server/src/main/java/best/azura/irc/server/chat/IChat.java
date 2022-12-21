package best.azura.irc.server.chat;

import best.azura.irc.server.packets.client.ChatMessagePacket;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IChat {

    void subscribe(String channelName, UUID userUUID);

    void unsubscribe(String channelName, UUID userUUID);

    Collection<UUID> getSubscribers(String channelName);

    void addToHistory(String channelName, ChatMessagePacket msg);

    List<ChatMessagePacket> getHistory(String channelName);
}
