package best.azura.irc.utils;

import best.azura.irc.server.packets.base.IPacket;
import best.azura.irc.server.packets.base.PacketInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.reflections.Reflections;

import java.lang.reflect.Type;
import java.util.Set;

public class PacketUtil {

    public static Gson gson = new GsonBuilder().create();

    private static Set<Class<? extends IPacket>> packets = new Reflections("best.azura.irc.server.packets").getSubTypesOf(IPacket.class);

    public static Class<? extends IPacket> getPacketFromId(int id) {
        return packets.stream().filter(c -> c.getAnnotation(PacketInfo.class).id() == id).findFirst().orElse(null);
    }

    public static <R> IPacket FromJson(R r, String json) {
        return gson.fromJson(json, (Type) r);
    }

    public static String ToJson(IPacket packet) {
        return gson.toJson(packet);
    }
}
