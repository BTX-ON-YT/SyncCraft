package systems.btx.Classes;

import java.util.List;

public class ServerData {
    private Version version;
    private Players players;
    private Description description;
    private String favicon;
    private boolean enforcesSecureChat;
    private boolean previewsChat;

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Players getPlayers() {
        return players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public boolean isEnforcesSecureChat() {
        return enforcesSecureChat;
    }

    public void setEnforcesSecureChat(boolean enforcesSecureChat) {
        this.enforcesSecureChat = enforcesSecureChat;
    }

    public boolean isPreviewsChat() {
        return previewsChat;
    }

    public void setPreviewsChat(boolean previewsChat) {
        this.previewsChat = previewsChat;
    }
}

class Version {
    private String name;
    private int protocol;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }
}

class Players {
    private int max;
    private int online;
    private List<Player> sample;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public List<Player> getSample() {
        return sample;
    }

    public void setSample(List<Player> sample) {
        this.sample = sample;
    }
}

class Player {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

class Description {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
