package best.azura.irc.server.session;

import java.util.UUID;

public interface ISessionRepository {

    void add(Session session);

    Session get(UUID uuid);

    void remove(UUID uuid);

    boolean contains(UUID uuid);
}