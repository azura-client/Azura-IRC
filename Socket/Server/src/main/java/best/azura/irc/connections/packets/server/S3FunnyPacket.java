package best.azura.irc.connections.packets.server;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;
import com.google.gson.JsonObject;

public class S3FunnyPacket extends Packet {

    public static int packetId = 0;

    public S3FunnyPacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public S3FunnyPacket(String funnyAction) {
        super((User) null);
        Id = packetId;

        JsonObject jsonObject = getContent();

        jsonObject.addProperty("funny", funnyAction);

        setContent(jsonObject);
    }
}
