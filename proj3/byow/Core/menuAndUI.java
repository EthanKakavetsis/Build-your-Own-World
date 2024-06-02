package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class menuAndUI {
    public static final int WIDTH = Engine.WIDTH;
    public static final int HEIGHT = Engine.HEIGHT;
    public static final int CANVAS_PIXELS = 16;
    public static Font fontSmall = new Font("Copperplate Gothic Bold", Font.BOLD, 25);
    public static Font fontBig = new Font("Copperplate Gothic Bold", Font.BOLD, 50);
    public static Font fontTiny = new Font("Copperplate Gothic Bold", Font.BOLD, 12);

    public static void displayMenu() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(WIDTH * CANVAS_PIXELS, HEIGHT * CANVAS_PIXELS);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.setFont(fontSmall);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
        StdDraw.line(0, HEIGHT - 1.5, WIDTH, HEIGHT - 1.5);
        StdDraw.line(0, HEIGHT - 0.5, WIDTH, HEIGHT - 0.5);
        StdDraw.line(0, HEIGHT - 0.01, WIDTH, HEIGHT - 0.01);
        StdDraw.line(0, 2, WIDTH, 2);
        StdDraw.line(0, 0.5, WIDTH, 0.5);
        StdDraw.line(0, 1.5, WIDTH, 1.5);
        StdDraw.line(0, 0.01, WIDTH, 0.01);
        StdDraw.line(2, 0, 2, HEIGHT);
        StdDraw.line(0.5, 0, 0.5, HEIGHT);
        StdDraw.line(1.5, 0, 1.5, HEIGHT);
        StdDraw.line(0.01, 0, 0.01, HEIGHT);
        StdDraw.line(WIDTH - 2, 0, WIDTH - 2, HEIGHT);
        StdDraw.line(WIDTH - 1.5, 0, WIDTH - 1.5, HEIGHT);
        StdDraw.line(WIDTH - 0.5, 0, WIDTH - 0.5, HEIGHT);
        StdDraw.line(WIDTH - 0.01, 0, WIDTH - 0.01, HEIGHT);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "(N)ew Game");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "(L)oad Game");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "(Q)uit");
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT - HEIGHT / 4, "Forever Maze");
        StdDraw.show();
    }

    public static void displayHUD(int level, int money, boolean enc, TETile[][] world) {
        Font font = new Font("Monaco", Font.BOLD, 16 - 2);
        double trueMX = StdDraw.mouseX();
        double trueMY = StdDraw.mouseY();
        int mX = (int) trueMX / 1;
        int mY = (int) trueMY / 1;
        if (mX >= WIDTH) {
            mX = WIDTH - 1;
        }
        if (mY >= HEIGHT) {
            mY = HEIGHT - 1;
        }
        TETile mouseTile = world[mX][mY];
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH / 2, HEIGHT - 1, WIDTH / 2, 1);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
        if (enc) {
            StdDraw.text(10, HEIGHT - 1, mouseTile.description());
            StdDraw.setPenColor(Color.yellow);
            StdDraw.text(WIDTH / 2, HEIGHT - 1, "Level: GOLD ROOM");
            StdDraw.text(WIDTH - 10, HEIGHT - 1, "$" + money);
        } else {
            StdDraw.text(10, HEIGHT - 1, mouseTile.description());
            StdDraw.text(WIDTH / 2, HEIGHT - 1, "Level: " + level + " (Press G for a new random world)");
            StdDraw.setPenColor(Color.yellow);
            StdDraw.text(WIDTH - 10, HEIGHT - 1, "$" + money);
        }
        StdDraw.show(10);
    }

    public static String seedPrompt() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + HEIGHT / 10, "Type a seed ending with s:");
        StdDraw.show();
        return getCharInput();

    }

    public static String getCharInput() {
        String typed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                typed += StdDraw.nextKeyTyped();
                StdDraw.clear(Color.BLACK);
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + HEIGHT / 10, "Type a seed ending with s:");
                StdDraw.text(WIDTH / 2, HEIGHT / 2, typed);
                StdDraw.show();
                if (typed.charAt(typed.length() - 1) == 's' || typed.charAt(typed.length() - 1) == 'S') {
                    if (typed.length() < 2) {
                        return "0";
                    }
                    return typed.substring(0, typed.length() - 1);
                }
            }
        }
    }
}
