package model;
/**
 * 
 */
public class Vector2D {

    public double x;
    public double y;

    public Vector2D() {
        this(0);
    }

    public Vector2D(double x) {
        this(x, x);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D vect) {
        this.x = vect.x;
        this.y = vect.y;
    }

    public boolean equals(Vector2D a) {
        return this.x == a.x && this.y == a.y;
    }

    public boolean isNull() {
        return (this.x == 0 && this.y == 0);
    }

    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D set(Vector2D a) {
        this.x = a.x;
        this.y = a.y;
        return this;
    }

    public Vector2D add(Vector2D a) {
        this.x += a.x;
        this.y += a.y;
        return this;
    }

    public Vector2D sub(Vector2D a) {
        this.x -= a.x;
        this.y -= a.y;
        return this;
    }

    public Vector2D mult(double a) {
        this.x *= a;
        this.y *= a;
        return this;
    }

    public Vector2D div(double a) {
        this.x /= a;
        this.y /= a;
        return this;
    }

    public Vector2D negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    public Vector2D normalize() {
        if (isNull()) {
            return this;
        }

        double absx = Math.abs(this.x);
        double absy = Math.abs(this.y);
        if (absx > absy) {
            this.x /= absx;
            this.y = 0;
        } else if (absx < absy) {
            this.x = 0;
            this.y /= absy;
        } else {
            this.x /= absx;
            this.y /= absy;
        }
        return this;
    }

    public double manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }

    public double manhattanDistance(Vector2D a) {
        return Math.abs(this.x - a.x) + Math.abs(this.y - a.y);
    }

    public double tchebychevDistance() {
        return Math.max(x, y);
    }

    public double tchebychevDistance(Vector2D a) {
        return Math.max(Math.abs(this.x - a.x), Math.abs(this.y - a.y));
    }

    public double euclidianDistance2() {
        return x * x + y * y;
    }

    public double euclidianDistance2(Vector2D a) {
        return Math.pow(this.x - a.x, 2) + Math.pow(this.y - a.y, 2);
    }

    public double euclidianDistance() {
        return Math.sqrt(euclidianDistance());
    }

    public double euclidianDistance(Vector2D a) {
        return Math.sqrt(euclidianDistance2(a));
    }

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a).add(b);
    }

    public static Vector2D sub(Vector2D a, Vector2D b) {
        return new Vector2D(a).sub(b);
    }

    public static Vector2D mult(Vector2D a, double b) {
        return new Vector2D(a).mult(b);
    }

    public static Vector2D div(Vector2D a, double b) {
        return new Vector2D(a).div(b);
    }

    public String toString() {
        return "[" + x + ":" + y + "]";
    }

}
