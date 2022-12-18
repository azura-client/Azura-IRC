package best.azura.irc.server.session;

import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
public class InMemoryRepository implements SessionRepository {
    private Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    public Session get(UUID uuid) {
        return sessions.get(uuid);
    }

    public void add(Session session) {
        sessions.put(session.getUuid(), session);
    }

    public void remove(UUID uuid) {
        sessions.remove(uuid);
    }

    public synchronized boolean contains(UUID uuid) {
        return sessions.containsKey(uuid);
    }

    public static Session buildSession(ChannelHandlerContext ctx, UUID uuid, String userName, Rank rank) {
        return new Session(ctx, uuid, userName, rank);
    }
}