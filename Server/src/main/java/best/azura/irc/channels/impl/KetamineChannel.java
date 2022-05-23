package best.azura.irc.channels.impl;

import best.azura.irc.channels.IChannel;

public class KetamineChannel implements IChannel {

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public String getName() {
        return "Ketamine";
    }

    @Override
    public String getDescription() {
        return "Channel for only Ketamine Users.";
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
