package systems.btx.Classes;

import org.bouncycastle.util.Pack;
import systems.btx.Packets.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.String.format;

public class PacketProcesser {
    private DataOutputStream outputStream;
    public Server server;
    public Client client;
    public ArrayList<Packet> packetQueue;

    public PacketProcesser(Server server, Client client, DataOutputStream outputStream) {
        this.server = server;
        this.client = client;
        this.packetQueue = new ArrayList<Packet>();
        this.outputStream = outputStream;
    }
    public void addPacket(Packet packet) {
        packetQueue.add(packet);
    }

    public void process() {
        while (!packetQueue.isEmpty()) {
            Packet packet = packetQueue.get(0);
            packetQueue.remove(0);
            switch (packet.getID()) {
                case 0x00: {
                    switch (client.state) {
                        case "HANDSHAKE": {
                            try {
                                HandShakePacket handShakePacket = HandShakePacket.fromBytes(packet.getBytes());

                                client.serverAddress = handShakePacket.getServerAddress();
                                client.serverPort = handShakePacket.getServerPort();
                                client.state = handShakePacket.getNextState();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                        case "STATUS": {
                            try {
                                client.ticker.add(o -> {
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        case "LOGIN": {
                            try {
                                LoginStartPacket loginStartPacket = LoginStartPacket.fromBytes(packet.getBytes());

                                client.player = new Player(loginStartPacket.getName(), loginStartPacket.getPlayerUUID());

                                client.ticker.add(o -> {
                                    LoginSuccessPacket loginSuccessPacket = new LoginSuccessPacket(client.player.getUUID(), client.player.getUsername());

                                    try {
                                        byte[] packetBytes = loginSuccessPacket.toBytes();
                                        outputStream.write(packetBytes);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    client.state = "PLAY";

                                    return o;
                                });

                                client.ticker.add(o -> {


                                    return o;
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    break;
                }
                case 0x01: {
                    if (client.state.equals("STATUS")) {
                        try {
                            PingRequestPacket pingRequestPacket = PingRequestPacket.fromBytes(packet.getBytes());

                            PongResponsePacket pongResponsePacket = new PongResponsePacket(pingRequestPacket.getPayload());

                            byte[] packetBytes = pongResponsePacket.toBytes();

                            outputStream.write(packetBytes);

                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
