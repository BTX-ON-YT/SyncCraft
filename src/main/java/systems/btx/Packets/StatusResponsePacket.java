package systems.btx.Packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static systems.btx.Parsers.Numbers.writeVarInt;
import static systems.btx.Parsers.Strings.writeString;

public class StatusResponsePacket {
    private String response;

    public StatusResponsePacket(String response) {
        this.response = response;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);

        // Write Packet ID as VarInt
        writeVarInt(dataOut, 0x00);

        writeString(dataOut, response);

        byte[] data = byteOut.toByteArray();
        ByteArrayOutputStream fullByteOut = new ByteArrayOutputStream();
        DataOutputStream fullDataOut = new DataOutputStream(fullByteOut);
        writeVarInt(fullDataOut, data.length);
        fullDataOut.write(data);

        return fullByteOut.toByteArray();
    }

    public String getResponse() {
        return response;
    }
}
