/**
 * This is a class representing the coordinates pair (x,y).
 */
public class Coordinates {
    private long x, y;

    public Coordinates(long x, long y){
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
