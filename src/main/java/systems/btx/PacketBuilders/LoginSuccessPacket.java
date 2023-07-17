package systems.btx.PacketBuilders;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import static systems.btx.Parsers.Numbers.*;
import static systems.btx.Parsers.Strings.getStringLengthWithLengthVarInt;
import static systems.btx.Parsers.Strings.writeString;
import static systems.btx.Parsers.UUIDParser.writeUUID;

public class LoginSuccessPacket {
    private final DataOutputStream outputStream;
    private final UUID uuid;
    private final String username;

    public LoginSuccessPacket(DataOutputStream outputStream, UUID uuid, String username) {
        this.outputStream = outputStream;
        this.uuid = uuid;
        this.username = username;
    }

    public void send() throws IOException {
        byte[] packetIDBytes = createVarInt(0x00);

        // Convert packet id to int
        int packetID = varIntToInteger(packetIDBytes);

        int PacketLength = packetIDBytes.length + 16 + getStringLengthWithLengthVarInt(username);

        System.out.println("Packet Length: " + PacketLength);

        writeVarInt(outputStream, PacketLength);

        writeVarInt(outputStream, packetID);

        writeUUID(outputStream, uuid);

        writeString(outputStream, username);

        writeVarInt(outputStream, 0);
    }
}
