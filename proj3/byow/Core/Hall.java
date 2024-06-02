package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.lang.Math;
public class Hall {
    private int x;
    private int y;
    private int x1;
    private int y1;

    public Hall(int x, int y, int x1, int y1) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
    }

    public static void xHall(Hall hall, TETile[][] world) {
        int xMin = Math.min(hall.x, hall.x1);
        int xMax = Math.max(hall.x, hall.x1);
        for (int x = xMin; x <= xMax; x++) {
            for (int y = hall.y - 1; y <= hall.y + 1; y++) {
                if (y == hall.y) {
                    world[x][y] = Tileset.FLOOR;
                } else if ((y == hall.y - 1 || y == hall.y + 1) && world[x][y] != Tileset.FLOOR) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }

    public static void yHall(Hall hall, TETile[][] world) {
        int yMin = Math.min(hall.y, hall.y1);
        int yMax = Math.max(hall.y, hall.y1);
        for (int x = hall.x - 1; x <= hall.x + 1; x++) {
            for (int y = yMin; y <= yMax; y++) {
                if (x == hall.x) {
                    world[x][y] = Tileset.FLOOR;
                } else if ((x == hall.x - 1 || x == hall.x + 1) && world[x][y] != Tileset.FLOOR) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }
}
