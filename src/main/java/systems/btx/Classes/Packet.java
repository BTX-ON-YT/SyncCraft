package systems.btx.Classes;

public class Packet {
    public int length;
    public int id;
    public byte[] data;

    public Packet(int length, int id, byte[] data) {
        this.length = length;
        this.id = id;
        this.data = data;
    }

    public int getLength() {
        return length;
    }

    public int getID() {
        return id;
    }

    public byte[] getBytes() {
        return data;
    }
}
