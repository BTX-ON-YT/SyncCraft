package systems.btx.Packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static systems.btx.Parsers.Numbers.writeVarInt;

public class PongResponsePacket {
    private long payload;

    public PongResponsePacket(long payload) {
        this.payload = payload;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);

        writeVarInt(dataOut, 0x01);

        dataOut.writeLong(payload);

        byte[] data = byteOut.toByteArray();
        ByteArrayOutputStream fullByteOut = new ByteArrayOutputStream();
        DataOutputStream fullDataOut = new DataOutputStream(fullByteOut);
        writeVarInt(fullDataOut, data.length);
        fullDataOut.write(data);

        return fullByteOut.toByteArray();
    }

    public long getPayload() {
        return payload;
    }
}
