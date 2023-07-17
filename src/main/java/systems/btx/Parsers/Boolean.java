package systems.btx.Parsers;

import java.io.DataInputStream;

public class Boolean {
    public static boolean readBoolean(DataInputStream inputStream) throws java.io.IOException {
        return inputStream.readBoolean();
    }
}
