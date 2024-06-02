package byow.Core;

public class Stopwatch {
    private long start;

    public Stopwatch() {
        start = System.currentTimeMillis();
    }

    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0; // Convert milliseconds to seconds
    }
}

