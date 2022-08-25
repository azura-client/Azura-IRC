package best.azura.irc.connections.handler;

import best.azura.irc.command.CommandManager;
import best.azura.irc.connections.packets.Packet;
import best.azura.irc.connections.packets.client.C1ChatSendPacket;
import best.azura.irc.connections.packets.client.C2NameChangePacket;
import best.azura.irc.connections.packets.client.C4BanNotifierPacket;
import best.azura.irc.connections.packets.client.C5EmotePacket;
import best.azura.irc.connections.packets.server.S1ChatSendPacket;
import best.azura.irc.connections.packets.server.S2NameChangePacket;
import best.azura.irc.connections.packets.server.S4BanNotifierPacket;
import best.azura.irc.connections.packets.server.S5EmotePacket;
import best.azura.irc.entities.Message;
import best.azura.irc.entities.User;
import best.azura.irc.server.Server;

public class PacketHandler {

    // Instance of the Server class.
    private final Server server;

    /**
     * Constructor for the Packet-Handler
     *
     * @param server Server class instance.
     */
    public PacketHandler(Server server) {
        this.server = server;
    }

    /**
     * Convert the packets and send them.
     *
     * @param packet the Packet that should be sent.
     */
    public void sendPacket(Packet packet) {
        if (server.getServerSocket().isClosed()) return;

        new Thread(() -> {
            if (packet instanceof C1ChatSendPacket chatSendPacket) {
                if (chatSendPacket.getMessage().getMessageContent().startsWith(CommandManager.COMMAND_PREFIX)) {
                    if(!this.server.getCommandManager().handleCommand(server, chatSendPacket.getUser(), chatSendPacket.getMessage().getMessageContent())) {
                        server.getPacketManager().sendPacket(server, chatSendPacket.getUser(),
                                new S1ChatSendPacket(chatSendPacket.getUser(), new Message(chatSendPacket.getUser(),
                                        "We couldn't find any Command matching your Request!", -1, true)));
                    }

                    return;
                }

                if (server.getChannelManager().getChannelById(chatSendPacket.getMessage().getChannelId()) == null) return;

                for (User user : server.getUserManager().getUserList()) {
                    server.getPacketManager().sendPacket(server, user,
                            new S1ChatSendPacket(chatSendPacket.getUser(), chatSendPacket.getMessage()));
                }
            } else if (packet instanceof C2NameChangePacket nameChangePacket) {
                if (nameChangePacket.getName() != null && !nameChangePacket.getName().isEmpty() && !nameChangePacket.getName().equalsIgnoreCase(nameChangePacket.getUser().getUsername())) {
                    if (server.getUserManager().getUserList().stream().noneMatch(user -> user.getMinecraftName().equals(nameChangePacket.getName()) || user.getMinecraftName().startsWith(nameChangePacket.getName()))) {
                        nameChangePacket.getUser().setMinecraftName(nameChangePacket.getName());
                        for (User user : server.getUserManager().getUserList()) {
                            server.getPacketManager().sendPacket(server, user,
                                    new S2NameChangePacket(nameChangePacket.getUser()));
                        }
                    } else {
                        server.getPacketManager().sendPacket(server, nameChangePacket.getUser(),
                                new S1ChatSendPacket(nameChangePacket.getUser(), new Message(nameChangePacket.getUser(),
                                        "There is already a User with that Minecraft Name, so we canceled your Request!", -1, true)));
                    }
                } else {
                    server.getPacketManager().sendPacket(server, nameChangePacket.getUser(),
                            new S1ChatSendPacket(nameChangePacket.getUser(), new Message(nameChangePacket.getUser(),
                                    "You send a invalid Minecraft Name, so we canceled your Request!", -1, true)));
                }
            } else if (packet instanceof C4BanNotifierPacket banNotifierPacket) {
                for (User user : server.getUserManager().getUserList()) {
                    server.getPacketManager().sendPacket(server, user,
                            new S4BanNotifierPacket(banNotifierPacket.getUser(), banNotifierPacket.getServer()));
                }
            } else if (packet instanceof C5EmotePacket emotePacket) {
                for (User user : server.getUserManager().getUserList()) {
                    server.getPacketManager().sendPacket(server, user,
                            new S5EmotePacket(emotePacket.getUser(), emotePacket.getEmote(), emotePacket.getServer()));
                }
            }
        }).start();
    }
}
