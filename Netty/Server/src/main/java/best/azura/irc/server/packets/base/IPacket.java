package best.azura.irc.server.packets.base;

import best.azura.irc.server.session.Session;

public interface IPacket {

    void process(Session session);
}
