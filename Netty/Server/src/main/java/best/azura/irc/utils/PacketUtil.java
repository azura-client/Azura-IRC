package best.azura.irc.utils;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.packets.base.PacketInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.reflections.Reflections;

import java.lang.reflect.Type;
import java.util.Set;

public class PacketUtil {

    public static Gson gson = new GsonBuilder().create();

    private static Set<Class<? extends IPacket>> packets = new Reflections("best.azura.irc.server.packets").getSubTypesOf(IPacket.class);

    public static Class<? extends IPacket> getPacketFromId(int id) {
        return packets.stream().filter(c -> c.getAnnotation(PacketInfo.class).id() == id).findFirst().orElse(null);
    }

    public static IPacket fromJSON(String json) {
        JsonObject object = JsonParser.parseString(json).getAsJsonObject();
        int id = object.get("id").getAsInt();

        Class<? extends IPacket> packet = getPacketFromId(id);

        return gson.fromJson(object.get("data"), (Type) packet);
    }

    public static <R> IPacket fromJSON(R r, String json) {
        JsonObject object = JsonParser.parseString(json).getAsJsonObject();

        return gson.fromJson(object.get("data"), (Type) r);
    }

    public static String toJSON(IPacket packet) {
        JsonObject object = new JsonObject();

        object.addProperty("id", packet.getClass().getAnnotation(PacketInfo.class).id());
        object.add("data", gson.toJsonTree(packet));

        return gson.toJson(object);
    }
}
