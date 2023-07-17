package systems.btx.PacketHandlers;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import static systems.btx.Parsers.Numbers.*;
import static systems.btx.Parsers.Strings.readString;

public class HandShake {
    public static String handle(int[] packetHeaders, int ProtocalVersion, DataInputStream inputStream, DataOutputStream outputStream) {
        try {
            int protocolVersion = readVarInt(inputStream);
            String serverAddress = readString(inputStream);
            int serverPort = inputStream.readUnsignedShort();
            int nextState = readVarInt(inputStream);

            // System.out.println("Protocol Version: " + protocolVersion);
            // System.out.println("Server Address: " + serverAddress);
            // System.out.println("Server Port: " + serverPort);
            // System.out.println("Next State: " + nextState);

            if (protocolVersion != ProtocalVersion) {
                System.out.println("Protocol version mismatch!");
                return "Protocol version mismatch!";
            }

            if (nextState == 1) {
                return "STATUS";
            } else if (nextState == 2) {
                return "LOGIN";
            } else {
                System.out.println("Invalid next state!");
                return "Invalid next state!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
