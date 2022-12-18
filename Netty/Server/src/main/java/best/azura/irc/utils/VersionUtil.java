package best.azura.irc.utils;

import best.azura.irc.main.Main;

import java.util.Objects;

public class VersionUtil {
    public static String getVersion() {
        return Objects.requireNonNullElse(Main.class.getPackage().getImplementationVersion(), "1.0.0");
    }
}
