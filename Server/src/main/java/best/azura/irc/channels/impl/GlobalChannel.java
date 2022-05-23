package best.azura.irc.channels.impl;

import best.azura.irc.channels.IChannel;

public class GlobalChannel implements IChannel {

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return "Global";
    }

    @Override
    public String getDescription() {
        return "Global IRC Chat.";
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
