package best.azura.irc.channels;

import best.azura.irc.channels.impl.*;

import java.util.Collection;
import java.util.HashMap;

public class ChannelManager {

    private final HashMap<Integer, IChannel> REGISTRY = new HashMap<>();

    public ChannelManager() {
        REGISTRY.put(0, new GlobalChannel());
        REGISTRY.put(1, new AzuraChannel());
        REGISTRY.put(2, new KetamineChannel());
        REGISTRY.put(3, new VertexChannel());
    }

    public IChannel getChannelById(int Id) {
        if (REGISTRY.containsKey(Id)) {
            return REGISTRY.get(Id);
        }

        return null;
    }

    public Collection<IChannel> getChannels() {
        return REGISTRY.values();
    }

}
