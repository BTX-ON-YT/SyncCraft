package systems.btx.Packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import static systems.btx.Parsers.Numbers.writeVarInt;
import static systems.btx.Parsers.Strings.writeString;
import static systems.btx.Parsers.UUIDParser.writeUUID;

public class LoginSuccessPacket {
    private String username;
    private UUID uuid;
    private int numberOfProperties;
    private String name;
    private String value;
    private boolean isSigned;
    private String signature;

    public LoginSuccessPacket(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.numberOfProperties = 0;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);

        writeVarInt(dataOut, 0x02);

        writeUUID(dataOut, uuid);

        writeString(dataOut, username);

        writeVarInt(dataOut, numberOfProperties);

        byte[] data = byteOut.toByteArray();
        ByteArrayOutputStream fullByteOut = new ByteArrayOutputStream();
        DataOutputStream fullDataOut = new DataOutputStream(fullByteOut);
        writeVarInt(fullDataOut, data.length);
        fullDataOut.write(data);

        return fullByteOut.toByteArray();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public int getNumberOfProperties() {
        return numberOfProperties;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean getIsSigned() {
        return isSigned;
    }

    public String getSignature() {
        return signature;
    }
}
