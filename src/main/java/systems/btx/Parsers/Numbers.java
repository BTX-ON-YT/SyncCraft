package systems.btx.Parsers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Numbers {
    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    public static int readVarInt(DataInputStream inputStream) throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = inputStream.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    public static long readVarLong(DataInputStream inputStream) throws IOException  {
        long value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = inputStream.readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 64) throw new RuntimeException("VarLong is too big");
        }

        return value;
    }

    public static void writeVarInt(DataOutputStream outputStream, int value) throws IOException {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                outputStream.writeByte(value);
                return;
            }

            outputStream.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }
    }

    public static void writeVarLong(DataOutputStream outputStream, long value) throws IOException {
        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                outputStream.writeLong(value);
                return;
            }

            outputStream.writeLong((value & SEGMENT_BITS) | CONTINUE_BIT);

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }
    }

    public static byte[] createVarInt(int value) throws IOException {
        byte[] bytes = new byte[5];

        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                bytes[0] = (byte) value;
                break;
            }

            bytes[0] = (byte) ((value & SEGMENT_BITS) | CONTINUE_BIT);

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }

        // shrink array to fit
        byte[] bytes2 = new byte[bytes.length - 1];

        for (int i = 0; i < bytes2.length; i++) {
            bytes2[i] = bytes[i];
        }

        bytes = bytes2;

        // reverse array

        for (int i = 0; i < bytes.length / 2; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }

        // return bytes;

        return bytes;
    }

    public static int getVarIntLength(int value) {
        int length = 0;
        do {
            value >>>= 7;
            length++;
        } while (value != 0);
        return length;
    }

    public static int varIntToInteger(byte[] bytes) throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = bytes[position];
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }
}
