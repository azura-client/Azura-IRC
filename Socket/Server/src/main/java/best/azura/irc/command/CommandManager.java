package best.azura.irc.command;

import best.azura.irc.command.impl.Ban;
import best.azura.irc.command.impl.Funny;
import best.azura.irc.connections.packets.server.S1ChatSendPacket;
import best.azura.irc.entities.Message;
import best.azura.irc.entities.User;
import best.azura.irc.server.Server;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager {

    // ArrayList of all commands
    private final ArrayList<ICommand> commands = new ArrayList<>();

    // Instance of the Server.
    private Server server;

    // Global prefix for commands
    public static final String COMMAND_PREFIX = "/";

    // Object initialisation of the Command Manager
    public CommandManager(Server server) {
        this.server = server;
        // Adding the commands
        this.commands.addAll(Arrays.asList(new Ban(), new Funny()));
    }

    // Returns the commands ArrayList
    public ArrayList<ICommand> getCommands() {
        return commands;
    }

    // Handle an incoming command
    public boolean handleCommand(final Server server, final User user, final String input) {
        try {
            if (input.startsWith(COMMAND_PREFIX)) {
                for (ICommand command : commands) {
                    String[] aliases = command.getAliases();
                    if (aliases == null || aliases.length == 0) aliases = new String[]{command.getName()};
                    for (String ali : aliases) {
                        if (((input.toLowerCase().startsWith(COMMAND_PREFIX + ali.toLowerCase() + " ")) ||
                                input.toLowerCase().equals(COMMAND_PREFIX + ali.toLowerCase()) ||
                                (input.toLowerCase().startsWith(COMMAND_PREFIX + command.getName().toLowerCase())))) {
                            String[] args = input.split(" ");
                            try {
                                command.handleCommand(server, user, Arrays.copyOfRange(args, 1, args.length));
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            return true;
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public void sendMessage(User user, String message, int channel) {
        server.getPacketManager().sendPacket(server, user, new S1ChatSendPacket(user, new Message(user, message, channel, true)));
    }

}