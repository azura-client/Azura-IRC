package best.azura.irc.channels.impl;

import best.azura.irc.channels.IChannel;

public class VertexChannel implements IChannel {
    @Override
    public int getId() {
        return 3;
    }

    @Override
    public String getName() {
        return "Vertex";
    }

    @Override
    public String getDescription() {
        return "Channel for only Vertex Users.";
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
