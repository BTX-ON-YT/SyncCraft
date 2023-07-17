package systems.btx.PacketBuilders;

import com.google.gson.JsonObject;

import java.io.DataOutputStream;
import java.io.IOException;

import static systems.btx.Parsers.Numbers.*;
import static systems.btx.Parsers.Strings.getStringLengthWithLengthVarInt;
import static systems.btx.Parsers.Strings.writeString;

public class StatusResponsePacket {
    private final DataOutputStream outputStream;

    private final String response;

    public StatusResponsePacket(DataOutputStream outputStream, String response) {
        this.outputStream = outputStream;
        this.response = response;
    }

    public void send() throws IOException {
        byte[] packetIDBytes = createVarInt(0x00);

        // Convert packet id to int
        int packetID = varIntToInteger(packetIDBytes);

        String responseString = response.toString();

        int PacketLength = packetIDBytes.length + getStringLengthWithLengthVarInt(responseString);

        writeVarInt(outputStream, PacketLength);

        writeVarInt(outputStream, packetID);

        writeString(outputStream, responseString);
    }
}
