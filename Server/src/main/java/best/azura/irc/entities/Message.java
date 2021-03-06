package best.azura.irc.entities;

public class Message {

    User user;
    String messageContent;
    int channelId;
    boolean isConsole;

    public Message(User user, String messageContent, int channelId, boolean isConsole) {
        this.user = user;
        this.messageContent = messageContent;
        this.channelId = channelId;
        this.isConsole = isConsole;
    }

    public String getFormattedMessage() {
        return "PREFIX"
                + " " +
                getUser().getUsername()
                + "&8: &7" +
                getMessageContent();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getChannelId() {
        return channelId;
    }

    public boolean isConsole() {
        return isConsole;
    }
}
