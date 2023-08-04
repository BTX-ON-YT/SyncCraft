package systems.btx.Classes;

import java.util.UUID;

public class Player {
    public String username;
    public UUID uuid;
    public int positionX;
    public int positionY;
    public int positionZ;
    public int rotationX;
    public int rotationY;

    public Player(String username, UUID uuid) {
        this.username = username;
        this.uuid = uuid;
        this.positionX = 0;
        this.positionY = 0;
        this.positionZ = 0;
        this.rotationX = 0;
        this.rotationY = 0;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getPositionZ() {
        return positionZ;
    }

    public int getRotationX() {
        return rotationX;
    }

    public int getRotationY() {
        return rotationY;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setPositionZ(int positionZ) {
        this.positionZ = positionZ;
    }
}
