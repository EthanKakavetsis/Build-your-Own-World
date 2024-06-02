package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {
    private int xPos;
    private int yPos;
    private boolean enc = false;
    public TETile charTile = Tileset.AVATAR;
    public Player(Point p) {
        xPos = p.x;
        yPos = p.y;
    }

    public Point playerCoord() {
        Point p = new Point(xPos, yPos);
        return p;
    }

    public void insertPlayer(TETile[][] world) {
        world[xPos][yPos] = charTile;
    }

    public void move(TETile[][] world, char direction, Boolean draw, Engine game) {
        switch (direction) {
            case 'w', 'W' -> moveUP(world, draw, game);
            case 's', 'S' -> moveDown(world, draw, game);
            case 'a', 'A' -> moveLeft(world, draw, game);
            case 'd', 'D' -> moveRight(world, draw, game);
        }
    }

    public void moveUP(TETile[][] world, Boolean display, Engine game) {
        if (!world[xPos][yPos + 1].description().equals("wall")) {
            if (world[xPos][yPos + 1].description().equals("unlocked door")) {
                Engine newLevel = new Engine();
                newLevel.nextLevel();
            }
            if (world[xPos][yPos + 1].description().equals("portal")) {
                enc = true;
            }
            if (world[xPos][yPos + 1].description().equals("money")) {
                game.increaseMoney();
            }
            world[xPos][yPos + 1] = charTile;
            world[xPos][yPos] = Tileset.FLOOR;
            if (display == true) {
                world[xPos][yPos].draw(xPos, yPos);
                world[xPos][yPos + 1].draw(xPos, yPos + 1);
            }
            yPos = yPos + 1;
            if (enc == true) {
                enc = false;
                Engine newLevel = new Engine();
                newLevel.runEncounter();
            }
        }
    }

    public void moveDown(TETile[][] world, Boolean display, Engine game) {
        if (!world[xPos][yPos - 1].description().equals("wall")) {
            if (world[xPos][yPos - 1].description().equals("unlocked door")) {
                Engine newLevel = new Engine();
                newLevel.nextLevel();
            }
            if (world[xPos][yPos - 1].description().equals("portal")) {
                enc = true;
            }
            if (world[xPos][yPos - 1].description().equals("money")) {
                game.increaseMoney();
            }
            world[xPos][yPos - 1] = charTile;
            world[xPos][yPos] = Tileset.FLOOR;
            if (display == true) {
                world[xPos][yPos].draw(xPos, yPos);
                world[xPos][yPos - 1].draw(xPos, yPos - 1);
            }
            yPos = yPos - 1;
            if (enc == true) {
                enc = false;
                Engine newLevel = new Engine();
                newLevel.runEncounter();
            }
        }
    }

    public void moveLeft(TETile[][] world, Boolean display, Engine game) {
        if (!world[xPos - 1][yPos].description().equals("wall")) {
            if (world[xPos - 1][yPos].description().equals("unlocked door")) {
                Engine newLevel = new Engine();
                newLevel.nextLevel();
            }
            if (world[xPos - 1][yPos].description().equals("portal")) {
                enc = true;
            }
            if (world[xPos - 1][yPos].description().equals("money")) {
                game.increaseMoney();
            }
            world[xPos - 1][yPos] = charTile;
            world[xPos][yPos] = Tileset.FLOOR;
            if (display == true) {
                world[xPos][yPos].draw(xPos, yPos);
                world[xPos - 1][yPos].draw(xPos - 1, yPos);
            }
            xPos = xPos - 1;
            if (enc == true) {
                enc = false;
                Engine newLevel = new Engine();
                newLevel.runEncounter();
            }
        }
    }

    public void moveRight(TETile[][] world, Boolean display, Engine game) {
        if (!world[xPos + 1][yPos].description().equals("wall")) {
            if (world[xPos + 1][yPos].description().equals("unlocked door")) {
                Engine newLevel = new Engine();
                newLevel.nextLevel();
            }
            if (world[xPos + 1][yPos].description().equals("portal")) {
                enc = true;
            }
            if (world[xPos + 1][yPos].description().equals("money")) {
                game.increaseMoney();
            }
            world[xPos + 1][yPos] = charTile;
            world[xPos][yPos] = Tileset.FLOOR;
            if (display == true) {
                world[xPos][yPos].draw(xPos, yPos);
                world[xPos + 1][yPos].draw(xPos + 1, yPos);
            }
            xPos = xPos + 1;
            if (enc == true) {
                enc = false;
                Engine newLevel = new Engine();
                newLevel.runEncounter();
            }
        }
    }
}
