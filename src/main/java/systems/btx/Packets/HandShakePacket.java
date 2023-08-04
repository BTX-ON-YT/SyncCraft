package systems.btx.Packets;

import java.io.*;

import static systems.btx.Parsers.Numbers.readVarInt;
import static systems.btx.Parsers.Numbers.writeVarInt;
import static systems.btx.Parsers.Strings.readString;
import static systems.btx.Parsers.Strings.writeString;

public class HandShakePacket {
    private int protocolVersion;
    private String serverAddress;
    private int serverPort;
    private int nextState;

    public HandShakePacket(int protocolVersion, String serverAddress, int serverPort, int nextState) {
        this.protocolVersion = protocolVersion;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.nextState = nextState;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);

        // Write Packet ID as VarInt
        writeVarInt(dataOut, 0x00);

        // Write Data (protocolVersion, serverAddress, serverPort, nextState)
        writeVarInt(dataOut, protocolVersion);
        writeString(dataOut, serverAddress);
        dataOut.writeShort(serverPort);
        writeVarInt(dataOut, nextState);

        // Calculate and write the Length of the Packet ID + Data
        byte[] data = byteOut.toByteArray();
        ByteArrayOutputStream fullByteOut = new ByteArrayOutputStream();
        DataOutputStream fullDataOut = new DataOutputStream(fullByteOut);
        writeVarInt(fullDataOut, data.length);
        fullDataOut.write(data);

        return fullByteOut.toByteArray();
    }

    public static HandShakePacket fromBytes(byte[] data) throws IOException {
        DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(data));

        // Read Data (protocolVersion, serverAddress, serverPort, nextState)
        int protocolVersion = readVarInt(dataIn);
        String serverAddress = readString(dataIn);
        int serverPort = dataIn.readUnsignedShort();
        int nextState = readVarInt(dataIn);

        return new HandShakePacket(protocolVersion, serverAddress, serverPort, nextState);
    }

    // Getters
    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getNextState() {
        if (nextState == 1) {
            return "STATUS";
        } else if (nextState == 2) {
            return "LOGIN";
        } else {
            return "UNKNOWN";
        }
    }
}
