package systems.btx.Packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static systems.btx.Parsers.Numbers.writeVarInt;
import static systems.btx.Parsers.Strings.writeString;

public class DisconnectPacket {
    private String reason;

    public DisconnectPacket(String reason) {
        this.reason = reason;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);

        writeVarInt(dataOut, 0x00);

        writeString(dataOut, reason);

        byte[] data = byteOut.toByteArray();
        ByteArrayOutputStream fullByteOut = new ByteArrayOutputStream();
        DataOutputStream fullDataOut = new DataOutputStream(fullByteOut);
        writeVarInt(fullDataOut, data.length);
        fullDataOut.write(data);

        return fullByteOut.toByteArray();
    }

    public String getReason() {
        return reason;
    }
}
