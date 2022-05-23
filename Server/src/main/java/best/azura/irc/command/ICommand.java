package best.azura.irc.command;

import best.azura.irc.entities.User;
import best.azura.irc.server.Server;

public interface ICommand {
    default String[] getAliases() {
        return new String[0];
    }
    String getName();
    default String getPermission() {
        return "command." + getName();
    }
    void handleCommand(final Server server, final User user, final String[] args);
}