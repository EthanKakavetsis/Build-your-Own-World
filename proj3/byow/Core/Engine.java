package byow.Core;

import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.io.*;
import java.util.Random;

public class Engine implements Serializable {

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 60;
    private static final int GOLD_ROOM_SIZE = 20;

    private static final int GROUP_NUM = 1568;
    private static final int OFFSET = 10;
    private static Player playerCharacter;
    private String seed = "";
    private static int level = 1;
    private static int money = 0;
    private static TETile[][] theWorldTiles;

    private static World theWorld;
    boolean encounter = false;
    TERenderer ter = new TERenderer();

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        menuAndUI.displayMenu();
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(OFFSET);
        }
        char input = StdDraw.nextKeyTyped();
        while (true) {
            StdDraw.pause(OFFSET);
            if (StdDraw.hasNextKeyTyped()) {
                input = StdDraw.nextKeyTyped();
            }
            switch (input) {
                case 'n', 'N' -> {
                    seed = menuAndUI.seedPrompt();
                    theWorldTiles = interactWithInputString(seed);
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(theWorldTiles);
                    menuAndUI.displayHUD(level, money, false, theWorldTiles);
                    StdDraw.show();
                    gameWithKeyboard(theWorldTiles, playerCharacter);
                }

                case 'l', 'L' -> {
                    theWorld = loadGame();
                    theWorldTiles = theWorld.tiles;
                    money = theWorld.money;
                    level = theWorld.level;
                    playerCharacter = theWorld.player;
                    playerCharacter.insertPlayer(theWorld.tiles);
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(theWorld.tiles);
                    menuAndUI.displayHUD(level, money, false, theWorldTiles);
                    StdDraw.show();
                    gameWithKeyboard(theWorldTiles, playerCharacter);
                }

                case 'q', 'Q' -> System.exit(0);

                default -> StdDraw.pause(OFFSET);
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        WorldCreator.gameSeed(input);
        WorldCreator newWorld = new WorldCreator(WIDTH, HEIGHT);
        theWorld = newWorld.create();
        playerCharacter = theWorld.player;
        stringMove(input, theWorld.tiles);
        return theWorld.tiles;
    }

    private void stringMove(String input, TETile[][] world) {
        char move;
        for (int i = 0; i < input.length(); i++) {
            move = input.charAt(i);
            if (move == 'q') {
                saveGame(theWorld);
            } else if (move == 'l') {
                theWorld = loadGame();
                theWorldTiles = theWorld.tiles;
                playerCharacter = theWorld.player;
                world = theWorld.tiles;
            } else {
                playerCharacter.move(world, move, false, this);
            }
        }
    }

    public void increaseMoney() {
        money += 1;
        theWorld.money = money;
    }

    public void nextLevel() {
        level += 1;
        theWorld.level = level;
        theWorldTiles = interactWithInputString(WorldCreator.viewSeed() + 1);
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(theWorldTiles);
        menuAndUI.displayHUD(level, money, false, theWorldTiles);
        StdDraw.show();
        gameWithKeyboard(theWorldTiles, playerCharacter);
    }

    public void runEncounter() {
        WorldCreator newWorld = new WorldCreator(WIDTH, HEIGHT);
        TERenderer terE = new TERenderer();
        Room aRoom = new Room(WIDTH / 2 - OFFSET, HEIGHT / 2 - OFFSET, GOLD_ROOM_SIZE, GOLD_ROOM_SIZE);
        Room.buildRoom(aRoom, newWorld.getTiles());
        TETile[][] tempWorld = newWorld.getTiles();
        Random rand = new Random();
        int x = aRoom.getCoords("x");
        int y = aRoom.getCoords("y");
        int x1 = aRoom.getCoords("x1");
        int y1 = aRoom.getCoords("y1");
        for (int k = x + 1; k < x1; k++) {
            int yMoney = rand.nextInt(y + 1, y1);
            tempWorld[k][yMoney] = Tileset.MONEY;
        }
        TETile[][] encWorld = newWorld.getTiles();
        encounter = true;
        Player encPlayer = new Player(aRoom.center());
        encPlayer.insertPlayer(encWorld);
        terE.initialize(WIDTH, HEIGHT);
        terE.renderFrame(encWorld);
        menuAndUI.displayHUD(level, money, true, encWorld);
        StdDraw.show();
        gameWithKeyboard(encWorld, encPlayer);
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(theWorldTiles);
        menuAndUI.displayHUD(level, money, false, theWorldTiles);
        StdDraw.show();
        gameWithKeyboard(theWorldTiles, playerCharacter);
    }

    public void gameWithKeyboard(TETile[][] world, Player player) {
        StdDraw.disableDoubleBuffering();
        if (encounter) {
            Stopwatch enc = new Stopwatch();
            while (enc.elapsedTime() < OFFSET) {
                char input = keyboardInput(world);
                player.move(world, input, true, this);
            }
            encounter = false;
        } else {
            while (true) {
                char input = keyboardInput(world);
                if (input == ':') {
                    input = keyboardInput(world);
                    if (input == 'q' || input == 'Q') {
                        theWorld.money = money;
                        theWorld.level = level;
                        saveGame(theWorld);
                        System.exit(0);
                    }
                } else if (input == 'l' || input == 'L') {
                    theWorld = loadGame();
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(theWorld.tiles);
                    money = theWorld.money;
                    level = theWorld.level;
                    playerCharacter = theWorld.player;
                    playerCharacter.insertPlayer(theWorld.tiles);
                    menuAndUI.displayHUD(level, money, false, theWorldTiles);
                    StdDraw.show();
                    gameWithKeyboard(theWorld.tiles, playerCharacter);
                } else if (input == 'g' || input == 'G') {
                    Random rand = new Random();
                    Integer seedNum = rand.nextInt(GROUP_NUM);
                    seed = seedNum.toString();
                    theWorldTiles = interactWithInputString(seed);
                    level = 1;
                    money = 0;
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(theWorldTiles);
                    menuAndUI.displayHUD(level, money, false, theWorldTiles);
                    StdDraw.show();
                    gameWithKeyboard(theWorldTiles, playerCharacter);
                } else {
                    player.move(world, input, true, this);
                }
            }
        }
    }

    public char keyboardInput(TETile[][] world) {
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(OFFSET);
            if (encounter) {
                menuAndUI.displayHUD(level, money, encounter, world);
            } else {
                menuAndUI.displayHUD(level, money, encounter, theWorldTiles);
            }
        }
        return StdDraw.nextKeyTyped();
    }

    private void saveGame(World game) {
        File save = new File("./game.txt");
        try {
            if (!save.exists()) {
                save.createNewFile();
            }
            FileOutputStream fileStream = new FileOutputStream(save);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(game);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    private World loadGame() {
        File save = new File("./game.txt");
        if (save.exists()) {
            try {
                FileInputStream fileStream = new FileInputStream(save);
                ObjectInputStream objectStream  = new ObjectInputStream(fileStream);
                return (World) objectStream.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return new WorldCreator(WIDTH, HEIGHT).create();
    }

    public void testGame() {
        Engine game = new Engine();
        TERenderer tr = new TERenderer();
        String input = "lwaaaaaaaaaa";
        TETile[][] finalTILES = game.interactWithInputString(input);
        tr.initialize(WIDTH, HEIGHT);
        tr.renderFrame(finalTILES);
    }
}
