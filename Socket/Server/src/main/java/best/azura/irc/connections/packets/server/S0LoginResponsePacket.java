package best.azura.irc.connections.packets.server;

import best.azura.irc.channels.IChannel;
import best.azura.irc.connections.packets.Packet;
import best.azura.irc.entities.User;
import best.azura.irc.main.Main;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class S0LoginResponsePacket extends Packet {
    public static int packetId = 0;

    public S0LoginResponsePacket(int Id) {
        super(Id);
        packetId = Id;
    }

    public S0LoginResponsePacket(boolean success) {
        super((User) null);
        Id = packetId;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", success);

        if (success) {
            JsonArray userListArray = new JsonArray();

            for (User user : Main.getInstance().getServer().getUserManager().getUserList()) {
                JsonObject userJsonObject = new JsonObject();
                userJsonObject.addProperty("name", user.getUsername());
                userJsonObject.addProperty("ingame", user.getMinecraftName());

                userListArray.add(userJsonObject);
            }

            jsonObject.add("users", userListArray);
        }

        JsonArray jsonArray = new JsonArray();

        for (IChannel channel : Main.getInstance().getServer().getChannelManager().getChannels()) {
            JsonObject jsonObject1 = new JsonObject();

            jsonObject1.addProperty("id", channel.getId());
            jsonObject1.addProperty("name", channel.getName());
            jsonObject1.addProperty("description", channel.getDescription());
            jsonObject1.addProperty("locked", channel.isLocked());

            jsonArray.add(jsonObject1);
        }

        jsonObject.add("channels", jsonArray);

        JsonObject serverDataObject = new JsonObject();

        // TODO add Server info

        jsonObject.add("server", serverDataObject);

        setContent(jsonObject);
    }
}
