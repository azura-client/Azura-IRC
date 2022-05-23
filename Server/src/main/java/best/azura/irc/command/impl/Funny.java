package best.azura.irc.command.impl;

import best.azura.irc.command.ICommand;
import best.azura.irc.connections.packets.server.S3FunnyPacket;
import best.azura.irc.entities.User;
import best.azura.irc.server.Server;

public class Funny implements ICommand {
    @Override
    public String getName() {
        return "funny";
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public void handleCommand(Server server, User user, String[] args) {
        if (args.length == 2) {
            User target = server.getUserManager().getUserList().stream().filter(user1 -> user1.getUsername().equalsIgnoreCase(args[0])).findAny().orElse(null);

            if (target != null) {
                server.getPacketManager().sendPacket(server, target, new S3FunnyPacket(args[1]));
                server.getCommandManager().sendMessage(user, "The User has been funnied :D", -1);
            } else {
                server.getCommandManager().sendMessage(user, "We couldn't find a User with that Name!", -1);
            }
        } else {
            server.getCommandManager().sendMessage(user, "Not enough Arguments!", -1);
        }
    }
}
