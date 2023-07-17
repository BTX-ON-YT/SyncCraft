package systems.btx.Parsers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static systems.btx.Parsers.Numbers.*;

public class Strings {
    public static String readString(DataInputStream inputStream) throws IOException {
        int length = readVarInt(inputStream);
        byte[] bytes = new byte[length];

        inputStream.readFully(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static int getStringLengthWithLengthVarInt(String string) throws IOException {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

        int length = bytes.length;
        
        byte[] lengthVarInt = createVarInt(length);

        return length + lengthVarInt.length;
    }

    public static void writeString(DataOutputStream outputStream, String string) throws IOException {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

        writeVarInt(outputStream, bytes.length);

        outputStream.write(bytes);
    }
}
