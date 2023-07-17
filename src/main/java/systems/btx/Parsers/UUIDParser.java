package systems.btx.Parsers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

public class UUIDParser {
    public static UUID readUUID(DataInputStream inputStream) throws java.io.IOException {
        // read 2 unsigned longs
        long mostSigBits = inputStream.readLong();
        long leastSigBits = inputStream.readLong();

        // convert to 128-bit integer
        UUID uuid = new UUID(mostSigBits, leastSigBits);

        return uuid;
    }

    public static void writeUUID(DataOutputStream outputStream, UUID uuid) throws java.io.IOException {
        outputStream.writeLong(uuid.getMostSignificantBits());
        outputStream.writeLong(uuid.getLeastSignificantBits());
    }
}
