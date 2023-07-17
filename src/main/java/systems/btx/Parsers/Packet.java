package systems.btx.Parsers;

import java.io.DataInputStream;
import java.io.IOException;

import static systems.btx.Parsers.Numbers.readVarInt;

public class Packet {
    public static int[] parsePacketHeaders(DataInputStream inputStream) throws IOException {
        int packetLength = readVarInt(inputStream);
        int packetID = readVarInt(inputStream);

        int[] packetHeaders = new int[2];

        packetHeaders[0] = packetLength;
        packetHeaders[1] = packetID;

        return packetHeaders;
    }
}
