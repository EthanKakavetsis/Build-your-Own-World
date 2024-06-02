package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldCreator {
    private int width;
    private int height;
    private TETile[][] tiles;

    private int MAX_NUM_ROOMS = 30;
    private int MAX_ROOM_SIZE = 12;
    private int MIN_ROOM_SIZE = 4;
    public List<Room> rooms = new ArrayList<>();
    private static long SEED = 0;
    private Random RANDOM = new Random(SEED);

    public WorldCreator(int width, int height) {
        this.width = width;
        this.height = height - 2;
        this.tiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public World create() {
        int num_rooms = 0;
        for (int i = 0; i < MAX_NUM_ROOMS; i++) {
            int h = RANDOM.nextInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
            int w = RANDOM.nextInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
            int x = RANDOM.nextInt(width - w - 1);
            int y = RANDOM.nextInt(height - h - 1);

            boolean intersect = false;

            Room aRoom = new Room(x, y, w, h);

            for (Room bRoom : rooms) {
                if (aRoom.intersects(bRoom)) {
                    intersect = true;
                    break;
                }
            }
            if (!intersect) {
                Point p = new Point(aRoom.center());

                int aRoomX = p.x;
                int aRoomY = p.y;

                Room.buildRoom(aRoom, tiles);
                rooms.add(aRoom);
                num_rooms++;

                if (num_rooms > 1) {
                    Point p2 = new Point(rooms.get(num_rooms - 2).center());

                    int bRoomX = p2.x;
                    int bRoomY = p2.y;

                    int hallVORH = RANDOM.nextInt(2);
                    if (hallVORH == 0) {
                        Hall hHall = new Hall(aRoomX, aRoomY, bRoomX, bRoomY);
                        Hall.xHall(hHall, tiles);
                        Hall vHall = new Hall(bRoomX, aRoomY, bRoomX, bRoomY);
                        Hall.yHall(vHall, tiles);
                    }
                    else {
                        Hall vHall = new Hall(aRoomX, aRoomY, bRoomX, bRoomY);
                        Hall.yHall(vHall, tiles);
                        Hall hHall = new Hall(aRoomX, bRoomY, bRoomX, bRoomY);
                        Hall.xHall(hHall, tiles);
                    }
                }
            }
            if (i == MAX_NUM_ROOMS - 1) {
                Room doorRoom = rooms.get(rooms.size() - 1);
                Room moneyRoom = rooms.get(rooms.size() / 2);
                Point p = moneyRoom.center();
                tiles[p.x][p.y] = Tileset.PORTAL;
                int xCoord = doorRoom.getCoords("x");
                int yCoord = doorRoom.getCoords("y");
                int x1Coord = doorRoom.getCoords("x1");
                int y1Coord = doorRoom.getCoords("y1");
                boolean valid = false;
                while (!valid) {
                    for (int k = xCoord + 1; k < x1Coord; k++) {
                        if (tiles[k][yCoord] == Tileset.WALL) {
                            tiles[k][yCoord] = Tileset.UNLOCKED_DOOR;
                            valid = true;
                            break;
                        } else if (tiles[k][y1Coord] == Tileset.WALL) {
                            tiles[k][y1Coord] = Tileset.UNLOCKED_DOOR;
                            valid = true;
                            break;
                        }
                    }
                    if (!valid) {
                        for (int k = yCoord + 1; k < y1Coord; k++) {
                            if (tiles[xCoord][k] == Tileset.WALL) {
                                tiles[xCoord][k] = Tileset.UNLOCKED_DOOR;
                                valid = true;
                                break;
                            } else if (tiles[x1Coord][k] == Tileset.WALL) {
                                tiles[x1Coord][k] = Tileset.UNLOCKED_DOOR;
                                valid = true;
                                break;
                            }
                        }
                    }

                }

            }
        }
        Player player = new Player(rooms.get(0).center());
        World newWorld = new World(tiles, player);
        player.insertPlayer(tiles);
        return newWorld;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    public static void gameSeed(String input) {
        String result = input.replaceAll("[^0-9]", "");
        if (result.length() > 0) {
            long seed = Long.parseLong(result);
            SEED = seed;
        }
    }

    public static String viewSeed() {
        return Long.toString(SEED);
    }
}
