package best.azura.irc.connections.packets;

import best.azura.irc.connections.packets.client.*;
import best.azura.irc.connections.packets.server.*;
import best.azura.irc.entities.HandshakeUser;
import best.azura.irc.entities.User;
import best.azura.irc.server.Server;
import best.azura.irc.utils.AESUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.PrintStream;
import java.util.HashMap;

public class PacketManager {

    private final HashMap<Integer, Packet> REGISTRY = new HashMap<>();

    public PacketManager() {
        REGISTRY.put(1, new C0LoginRequestPacket(1));
        REGISTRY.put(2, new C1ChatSendPacket(2));
        REGISTRY.put(3, new C2NameChangePacket(3));
        REGISTRY.put(4, new C3KeepAlivePacket(4));
        REGISTRY.put(5, new S0LoginResponsePacket(5));
        REGISTRY.put(6, new S1ChatSendPacket(6));
        REGISTRY.put(7, new S2NameChangePacket(7));
        REGISTRY.put(8, new S3FunnyPacket(8));
        REGISTRY.put(9, new S4BanNotifierPacket(9));
        REGISTRY.put(10, new C4BanNotifierPacket(10));
        REGISTRY.put(11, new S5EmotePacket(11));
        REGISTRY.put(12, new C5EmotePacket(12));
        REGISTRY.put(13, new S6BanPacket(13));
        REGISTRY.put(14, new C6ClientInfoPacket(14));
        REGISTRY.put(15, new C7HandshakeRequest(15));
        REGISTRY.put(16, new S7HandshakeResponse(16));
    }

    public Packet getPacketById(int Id) {
        if (REGISTRY.containsKey(Id)) {
            return REGISTRY.get(Id);
        }

        return null;
    }

    public Packet getPacket(JsonObject jsonObject, HandshakeUser user) {

        if (jsonObject.has("id")) {
            Packet packet = getPacketById(jsonObject.get("id").getAsInt());

            if (packet == null) {
                return null;
            }

            if (jsonObject.has("content")) {
                if (packet.isEncrypt() && user != null) {
                    String decrypted = AESUtil.decrypt(jsonObject.get("content").getAsString(), user.getAESKey());

                    if (decrypted != null) {
                        JsonElement jsonElement1 = JsonParser.parseString(decrypted);
                        if (jsonElement1 != null && jsonElement1.isJsonObject())
                            packet.setContent(jsonElement1.getAsJsonObject());
                    }
                } else {
                    packet.setContent(jsonObject.getAsJsonObject("content"));
                }
            }

            return packet;
        }

        return null;
    }

    public void sendPacket(Server server, User user, Packet packet) {
        if (user == null || server == null || packet == null) return;

        if (user.getClientSocket().isConnected() &&
                !user.getClientSocket().isOutputShutdown() &&
                !user.getClientSocket().isInputShutdown()) {
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", packet.getId());
                jsonObject.add("content", packet.getContent());

                if (packet.isEncrypt()) {
                    jsonObject.addProperty("content", AESUtil.encrypt(server.getGson().toJson(packet.getContent()), user.getAESKey()));
                } else {
                    jsonObject.add("content", packet.getContent());
                }

                user.getPrintStream().println(server.getGson().toJson(jsonObject));
            } catch (Exception ignore) {}
        }
    }

    public void sendPacket(Server server, PrintStream printStream, Packet packet) {
        if (printStream == null || server == null || packet == null) return;
        if (!printStream.checkError()) {
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", packet.getId());
                jsonObject.add("content", packet.getContent());
                printStream.println(server.getGson().toJson(jsonObject));
            } catch (Exception ignore) {}
        }
    }

    public void sendPacket(Server server, HandshakeUser user, Packet packet) {
        if (user == null || server == null || packet == null) return;

        if (user.getClientSocket().isConnected() &&
                !user.getClientSocket().isOutputShutdown() &&
                !user.getClientSocket().isInputShutdown()) {
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", packet.getId());
                jsonObject.add("content", packet.getContent());

                if (packet.isEncrypt()) {
                    jsonObject.addProperty("content", AESUtil.encrypt(server.getGson().toJson(packet.getContent()), user.getAESKey()));
                } else {
                    jsonObject.add("content", packet.getContent());
                }

                user.getPrintStream().println(server.getGson().toJson(jsonObject));
            } catch (Exception ignore) {}
        }
    }

}
