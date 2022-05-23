package best.azura.irc.channels.impl;

import best.azura.irc.channels.IChannel;

public class AzuraChannel implements IChannel {

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public String getName() {
        return "Azura";
    }

    @Override
    public String getDescription() {
        return "Channel for only Azura Users.";
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
