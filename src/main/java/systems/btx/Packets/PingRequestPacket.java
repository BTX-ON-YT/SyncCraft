package systems.btx.Packets;

import java.io.DataInputStream;
import java.io.IOException;

public class PingRequestPacket {
    private long payload;

    public PingRequestPacket(long payload) {
        this.payload = payload;
    }

    public static PingRequestPacket fromBytes(DataInputStream inputStream) throws IOException {
        DataInputStream dataIn = inputStream;

        long payload = dataIn.readLong();

        return new PingRequestPacket(payload);
    }

    public long getPayload() {
        return payload;
    }
}
