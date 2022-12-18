package best.azura.irc.server.session;

public enum Rank {

    USER("&8[&7User&8]"),
    STAFF("&8[&aStaff&8]"),
    DEVELOPER("&8[&bDeveloper&8]");

    int id;
    String prefix;

    Rank(String prefix) {
        this.id = ordinal();
        this.prefix = prefix;
    }
}
