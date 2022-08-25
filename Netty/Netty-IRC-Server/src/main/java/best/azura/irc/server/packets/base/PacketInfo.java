package best.azura.irc.server.packets.base;

public @interface PacketInfo {

    int id();
    PacketTyp typ();
}
