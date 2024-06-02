package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;

public class World implements Serializable {
    public TETile[][] tiles;
    private int width;
    private int height;

    public int money;
    public int level;
    public Player player;

    public World(TETile[][] tiles, Player player) {
        this.tiles = tiles;
        this.player = player;
        width = tiles.length;
        height = tiles[0].length;
    }
}


