package best.azura.irc.server.packets.base;

import com.google.gson.JsonObject;

public interface IPacket {

    void read(String data);

    default Object getData() {
        return new JsonObject();
    }
}
