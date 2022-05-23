package best.azura.irc.channels;

public interface IChannel {

    int getId();

    String getName();

    String getDescription();

    boolean isLocked();

}
