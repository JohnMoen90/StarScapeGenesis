package world;

public class Tile {
    public static Tile[] tiles = new Tile[16];

    public static final Tile test_tile = new Tile((byte) 0, "test");

    private byte id;
    private String texture;

    public Tile(byte id, String texture) {
        this.id = id;
        this.texture = texture;
        if (tiles[id] != null)
            throw new IllegalStateException("Tiles at: [" + id + "] is already being used");
        tiles[id] = this;
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}
