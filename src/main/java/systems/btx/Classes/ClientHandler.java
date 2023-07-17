package systems.btx.Classes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import systems.btx.PacketBuilders.LoginSuccessPacket;
import systems.btx.PacketHandlers.HandShake;
import systems.btx.PacketHandlers.LoginStart;
import systems.btx.Packets.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.UUID;

import static systems.btx.Constants.*;
import static systems.btx.Parsers.Numbers.createVarInt;
import static systems.btx.Parsers.Numbers.getVarIntLength;
import static systems.btx.Parsers.Packet.parsePacketHeaders;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    public Client client;
    private String state = "HANDSHAKE";

    private String StatusResponse;

    private Gson gson = new Gson();
    private String serverAddress;
    private int serverPort;
    private String nextState = "HANDSHAKE";
    private String name;
    private UUID uuid;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.client = new Client();
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            while (!clientSocket.isClosed()) {
                // Read packet type
                int[] packetHeaders = parsePacketHeaders(inputStream);
                int packetID = packetHeaders[1];
                int packetLength = packetHeaders[0];

                System.out.println(packetID);
                switch (packetID) {
                    case PACKET_HANDSHAKE: // this is used for other things too
                        if (nextState.equals("HANDSHAKE")) {
//                            String res = HandShake.handle(packetHeaders, PROTOCOL_VERSION, inputStream, outputStream);
//
//                            if (res.equals("LOGIN")) {
//                                state = "LOGIN";
//                            } else if (res.equals("STATUS")) {
//                                new StatusResponsePacket(outputStream, "{\n" +
//                                        "    \"version\": {\n" +
//                                        "        \"name\": \"1.20.1\",\n" +
//                                        "        \"protocol\": 763\n" +
//                                        "    },\n" +
//                                        "    \"players\": {\n" +
//                                        "        \"max\": 100,\n" +
//                                        "        \"online\": 5,\n" +
//                                        "        \"sample\": [\n" +
//                                        "            {\n" +
//                                        "                \"name\": \"thinkofdeath\",\n" +
//                                        "                \"id\": \"4566e69f-c907-48ee-8d71-d7ba5aa00d20\"\n" +
//                                        "            }\n" +
//                                        "        ]\n" +
//                                        "    },\n" +
//                                        "    \"description\": {\n" +
//                                        "        \"text\": \"Hello world\"\n" +
//                                        "    },\n" +
//                                        "    \"favicon\": \"\",\n" +
//                                        "    \"enforcesSecureChat\": true,\n" +
//                                        "    \"previewsChat\": true\n" +
//                                        "}\n").send();
//
//                                state = "STATUS";
//                            }

                            HandShakePacket handShakePacket = HandShakePacket.fromBytes(inputStream);

                            serverAddress = handShakePacket.getServerAddress();
                            serverPort = handShakePacket.getServerPort();
                            nextState = handShakePacket.getNextState();

                            if (nextState.equals("LOGIN")) {
                                String reason = "{\"text\":\"Not Implemented. Check the git hub for updates\"}";

                                DisconnectPacket disconnectPacket = new DisconnectPacket(reason);

                                byte[] packetBytes = disconnectPacket.toBytes();

                                outputStream.write(packetBytes);
                            }
                        } else if (nextState.equals("LOGIN")) {
                            LoginStartPacket loginStartPacket = LoginStartPacket.fromBytes(inputStream);

                            name = loginStartPacket.getName();
                            uuid = loginStartPacket.getPlayerUUID();
                        } else if (nextState.equals("STATUS")) {
                            String response = "{\n" +
                                    "    \"version\": {\n" +
                                    "        \"name\": \"1.20.1\",\n" +
                                    "        \"protocol\": 763\n" +
                                    "    },\n" +
                                    "    \"players\": {\n" +
                                    "        \"max\": 420,\n" +
                                    "        \"online\": 69,\n" +
                                    "        \"sample\": [\n" +
                                    "            {\n" +
                                    "                \"name\": \"thinkofdeath\",\n" +
                                    "                \"id\": \"4566e69f-c907-48ee-8d71-d7ba5aa00d20\"\n" +
                                    "            }\n" +
                                    "        ]\n" +
                                    "    },\n" +
                                    "    \"description\": {\n" +
                                    "        \"text\": \"Hello world\"\n" +
                                    "    },\n" +
                                    "    \"favicon\": \"data:image/png;base64,balls\",\n" +
                                    "    \"enforcesSecureChat\": true,\n" +
                                    "    \"previewsChat\": true\n" +
                                    "}";

                            StatusResponsePacket statusResponsePacket = new StatusResponsePacket(response);

                            byte[] packetBytes = statusResponsePacket.toBytes();

                            outputStream.write(packetBytes);
                        }
                        break;
                    case 0x01: {
                        PingRequestPacket pingRequestPacket = PingRequestPacket.fromBytes(inputStream);

                        PongResponsePacket pongResponsePacket = new PongResponsePacket(pingRequestPacket.getPayload());

                        byte[] packetBytes = pongResponsePacket.toBytes();

                        outputStream.write(packetBytes);

                        break;
                    }
                    default:
                        System.out.println("Received unknown packet type");
                        break;
                }
            }

            // Close the streams and socket
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
