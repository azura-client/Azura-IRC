package best.azura.irc.connections.packets.server;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;
import com.google.gson.JsonObject;

public class S7HandshakeResponse extends Packet {

    public static int packetId = 0;

    public S7HandshakeResponse(int Id) {
        super(Id);
        packetId = Id;
        setEncrypt(false);
    }

    public S7HandshakeResponse(String aesKey, boolean success, String reason) {
        super((User) null);
        Id = packetId;
        setEncrypt(false);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", aesKey);
        jsonObject.addProperty("success", success);
        jsonObject.addProperty("reason", reason);

        setContent(jsonObject);

    }
}
