package best.azura.irc.server.session;

import java.util.Arrays;

public enum Rank {

    USER(1,"&8[&7User&8]"),
    STAFF(2,"&8[&aStaff&8]"),
    DEVELOPER(3,"&8[&bDeveloper&8]");

    int id;
    String prefix;

    Rank(int id, String prefix) {
        this.id = id;
        this.prefix = prefix;
    }

    public static Rank fromId(int id) {
        return Arrays.stream(values()).filter(r -> r.id == id).findFirst().orElse(Rank.USER);
    }
}
