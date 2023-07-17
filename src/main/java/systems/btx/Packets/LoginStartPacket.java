package systems.btx.Packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import static systems.btx.Parsers.Strings.readString;
import static systems.btx.Parsers.UUIDParser.readUUID;

public class LoginStartPacket {
    private String name;
    private Boolean hasPlayerUUID;
    private UUID playerUUID;

    public LoginStartPacket(String name, Boolean hasPlayerUUID, UUID playerUUID) {
        this.name = name;
        this.hasPlayerUUID = hasPlayerUUID;
        this.playerUUID = playerUUID;
    }

    public static LoginStartPacket fromBytes(DataInputStream inputStream) throws IOException {
        DataInputStream dataIn = inputStream;

        String name = readString(dataIn);

        Boolean hasPlayerUUID = dataIn.readBoolean();

        UUID playerUUID = null;

        if (hasPlayerUUID) {
            playerUUID = readUUID(dataIn);
        }

        return new LoginStartPacket(name, hasPlayerUUID, playerUUID);
    }

    public String getName() {
        return name;
    }

    public Boolean getHasPlayerUUID() {
        return hasPlayerUUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
