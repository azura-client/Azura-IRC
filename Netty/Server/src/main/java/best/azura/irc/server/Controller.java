package best.azura.irc.server;

import best.azura.irc.server.chat.IChat;
import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.session.ISessionRepository;
import best.azura.irc.server.session.Session;
import best.azura.irc.utils.executor.Executor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class Controller {

    private IChat broker;
    private ISessionRepository sessions;
    private Executor executor;

    public Controller(IChat broker, Executor executor, ISessionRepository sessions) {
        this.broker = broker;
        this.executor = executor;
        this.sessions = sessions;
    }

    public void accept(Session session) {
        log.info("Router accept ", session.getUsername());
        sessions.add(session);
    }

    public void close(UUID userUUID) {
        Session session = sessions.get(userUUID);
        if (session == null) {
            log.error("Session not found on close ", userUUID);
            return;
        }

        sessions.remove(userUUID);
    }

    public void receiveMessage(UUID userUUID, Object message) {
        Session session = sessions.get(userUUID);
        if (session == null) {
            log.error("Session not found on userName %s", userUUID);
            return;
        }

        log.info("Received message from user %s", userUUID);

        if (message instanceof IPacket packet) {
            packet.process(session);
        }
    }
}
