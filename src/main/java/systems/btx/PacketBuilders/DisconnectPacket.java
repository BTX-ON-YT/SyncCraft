package systems.btx.PacketBuilders;

import netscape.javascript.JSObject;

import java.io.DataOutputStream;
import java.io.IOException;

import static systems.btx.Parsers.Numbers.*;
import static systems.btx.Parsers.Strings.getStringLengthWithLengthVarInt;
import static systems.btx.Parsers.Strings.writeString;

public class DisconnectPacket {
    private final DataOutputStream outputStream;
    private final String reason;

    public DisconnectPacket(DataOutputStream outputStream, String reason) {
        this.outputStream = outputStream;
        this.reason = reason;
    }

    public void send() throws IOException {
        byte[] packetIDBytes = createVarInt(0x00);

        // Convert packet id to int
        int packetID = varIntToInteger(packetIDBytes);

        int StringLength = reason.length();

        int PacketLength = packetIDBytes.length + getStringLengthWithLengthVarInt(reason);

        System.out.println("Packet Length: " + PacketLength);

        writeVarInt(outputStream, PacketLength);

        writeVarInt(outputStream, packetID);

        writeString(outputStream, reason);
    }
}
