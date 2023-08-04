package systems.btx.Classes;

import com.google.gson.Gson;
import com.sun.tools.jconsole.JConsoleContext;
import systems.btx.Packets.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

import static java.lang.String.format;
import static systems.btx.Constants.*;
import static systems.btx.Parsers.Numbers.createVarInt;
import static systems.btx.Parsers.Numbers.readVarInt;
import static systems.btx.Parsers.Packet.parsePacketHeaders;

public class Client implements Runnable {
    public Ticker ticker;
    private Server server;
    private Socket clientSocket;
    public String state = "HANDSHAKE";
    private Gson gson = new Gson();
    public String serverAddress;
    public int serverPort;
    public Player player;
    private PacketProcesser packetProcesser;
    public DataOutputStream outputStream;

    public Client(Socket clientSocket, Ticker ticker, Server server) {
        this.clientSocket = clientSocket;
        this.ticker = ticker;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            packetProcesser = new PacketProcesser(server, this, outputStream);

            while (!clientSocket.isClosed()) {
                // Read packet type
                int packetLength = readVarInt(inputStream);

                DataInputStream data = new DataInputStream(new ByteArrayInputStream(inputStream.readNBytes(packetLength)));

                int packetID = readVarInt(data);

                byte[] packetData = data.readAllBytes();

                Packet packet = new Packet(packetLength, packetID, packetData);

                packetProcesser.addPacket(packet);

                ticker.add(o -> {
                    packetProcesser.process();
                    return o;
                });

                /*System.out.println(packetID);
                switch (packetID) {
                    case 0x00:
                       if (state.equals("HANDSHAKE")) {
                           HandShakePacket handShakePacket = HandShakePacket.fromBytes(inputStream);

                           serverAddress = handShakePacket.getServerAddress();
                           serverPort = handShakePacket.getServerPort();
                           state = handShakePacket.getNextState();
                       } else if (state.equals("LOGIN")) {
                           LoginStartPacket loginStartPacket = LoginStartPacket.fromBytes(inputStream);

                           username = loginStartPacket.getName();
                           uuid = loginStartPacket.getPlayerUUID();
                           ticker.add(o -> {
                               LoginSuccessPacket loginSuccessPacket = new LoginSuccessPacket(uuid, username);

                               try {
                                   byte[] packetBytes = loginSuccessPacket.toBytes();
                                   outputStream.write(packetBytes);
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                               return o;
                           });

                            state = "PLAY";
                       } else if (state.equals("STATUS")) {
                           ticker.add(o -> {
                               String response = "{\n" +
                                       "    \"version\": {\n" +
                                       "        \"name\": \"1.20.1\",\n" +
                                       "        \"protocol\": 763\n" +
                                       "    },\n" +
                                       "    \"players\": {\n" +
                                       format("        \"max\": %d,\n", server.maxPlayers) +
                                       format("        \"online\": %d,\n", server.playerCount) +
                                       "        \"sample\": []" +
                                       "    },\n" +
                                       "    \"description\": {\n" +
                                       format("        \"text\": \"%s\"\n", server.motd) +
                                       "    },\n" +
                                       "    \"favicon\": \"data:image/png;base64,balls\",\n" +
                                       "    \"enforcesSecureChat\": true,\n" +
                                       "    \"previewsChat\": true\n" +
                                       "}";

                               StatusResponsePacket statusResponsePacket = new StatusResponsePacket(response);

                               try {
                                   byte[] packetBytes = statusResponsePacket.toBytes();

                                   outputStream.write(packetBytes);
                               } catch (IOException e) {
                                   throw new RuntimeException(e);
                               }
                               return o;
                           });
                       } else if (state.equals("PLAY")) {
                           if (isBundling) {
                               isBundling = false;
                               
                               bundle.clear();
                           } else {
                               isBundling = true;
                           }


                       }
                        break;
                    case 0x01: {
                        if (state.equals("STATUS")) {
                            PingRequestPacket pingRequestPacket = PingRequestPacket.fromBytes(inputStream);

                            PongResponsePacket pongResponsePacket = new PongResponsePacket(pingRequestPacket.getPayload());

                            byte[] packetBytes = pongResponsePacket.toBytes();

                            outputStream.write(packetBytes);

                            break;
                        }
                    }
                    default:
                        System.out.println("Received unknown packet type");
                        break;
                }*/
            }

            // Close the streams and socket
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
