package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.Serializable;

public class Room implements Serializable {
    private int x;
    private int y;
    private int x1;
    private int y1;

    public Room(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.x1 = x + w;
        this.y1 = y + h;
    }

    public int getCoords(String coord) {
        return switch (coord) {
            case "x" -> x;
            case "y" -> y;
            case "x1" -> x1;
            case "y1" -> y1;
            default -> x;
        };
    }

    public boolean intersects(Room other) {
        if (this.x <= other.x1 && this.x1 >= other.x && this.y <= other.y1 && this.y1 >= other.y) {
            return true;
        }
        return false;
    }

    public Point center() {
        int xCenter = (x + x1) / 2;
        int yCenter = (y + y1) / 2;
        return new Point(xCenter, yCenter);
    }

    public static void buildRoom(Room room, TETile[][] world) {
        for (int x = room.x; x <= room.x1; x++) {
            for (int y = room.y; y <= room.y1; y++) {
                if ((x == room.x || x == room.x1 || y == room.y || y == room.y1) && world[x][y] != Tileset.FLOOR) {
                    world[x][y] = Tileset.WALL;
                } else {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }
}
