package best.azura.irc.connections.packets.server;

import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.Message;
import best.azura.irc.entities.User;
import com.google.gson.JsonObject;

public class S1ChatSendPacket extends Packet {

    Message message;

    public static int packetId = 0;

    public S1ChatSendPacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public S1ChatSendPacket(User user, Message message) {
        super(user);
        Id = packetId;

        this.message = message;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();

        jsonObject1.addProperty("user", message.getUser().getUsername());
        jsonObject1.addProperty("content", message.getMessageContent());
        jsonObject1.addProperty("formatted", message.getFormattedMessage());
        jsonObject1.addProperty("channel", message.getChannelId());
        jsonObject1.addProperty("console", message.isConsole());

        jsonObject.add("message", jsonObject1);

        setContent(jsonObject);
    }

    public Message getMessage() {
        return message;
    }
}
