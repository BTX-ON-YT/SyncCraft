package systems.btx.PacketHandlers;

import systems.btx.PacketBuilders.LoginSuccessPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import static systems.btx.Parsers.Boolean.readBoolean;
import static systems.btx.Parsers.Strings.readString;
import static systems.btx.Parsers.UUIDParser.readUUID;

public class LoginStart {
    public static String handle(int[] packetHeaders, DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        String username = readString(inputStream);
        Boolean HasPlayerUUID = readBoolean(inputStream);
        UUID PlayerUUID = null;

        if (HasPlayerUUID) {
            PlayerUUID = readUUID(inputStream);
        }

        System.out.println("Username: " + username);
        System.out.println("Has Player UUID: " + HasPlayerUUID);
        System.out.println("Player UUID: " + PlayerUUID);

        new LoginSuccessPacket(outputStream, PlayerUUID, username).send();

        return "Success";
    }
}
