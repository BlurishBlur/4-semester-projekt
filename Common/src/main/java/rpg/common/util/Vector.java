package rpg.common.util;

import java.io.Serializable;

public class Vector implements Serializable {

    private float x;
    private float y;

    public Vector() {
        this(0, 0);
    }

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector plus(Vector other) {
        return new Vector(this.x + other.getX(), this.y + other.getY());
    }

    public Vector minus(Vector other) {
        return new Vector(this.x - other.getX(), this.y - other.getY());
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(Vector other) {
        set(other.getX(), other.getY());
    }

    public void set(float x, float y) {
        setX(x);
        setY(y);
    }

    public void add(Vector other) {
        add(other.getX(), other.getY());
    }

    public void add(float x, float y) {
        addX(x);
        addY(y);
    }

    public void addX(float x) {
        this.x += x;
    }

    public void addY(float y) {
        this.y += y;
    }

    public void subtract(Vector other) {
        subtract(other.getX(), other.getY());
    }

    public void subtract(float x, float y) {
        subtractX(x);
        subtractY(y);
    }

    public void subtractX(float x) {
        this.x -= x;
    }

    public void subtractY(float y) {
        this.y -= y;
    }

    public float getMagnitude() {
        return magnitude(y, x);
    }

    public float getDistanceTo(Vector other) {
        return this.minus(other).getMagnitude();
    }

    public void scalar(float scalar) {
        x *= scalar;
        y *= scalar;
    }

    public float getAngle() {
        return (float) Math.toDegrees(Math.atan2(y, x));
    }
    
    public float getAngleTo(Vector other) {
        //return (float) Math.toDegrees(Math.atan2(getCrossProduct(other), getDotProduct(other)));
        
        return (float) Math.toDegrees(Math.acos(getDotProduct(other) / getMagnitude() * other.getMagnitude()));
    }

    public void normalize(float factor) {
        x = x / (magnitude(factor) / factor);
        y = y / (magnitude(factor) / factor);
    }

    public boolean isDiagonal() {
        return x != 0 && y != 0;
    }

    public boolean isMoving() {
        return x != 0 || y != 0;
    }

    public float getDotProduct(Vector other) {
        return x * other.getX() + y * other.getY();
    }
    
    public float getCrossProduct(Vector other) {
        return x * other.getY() - y * other.getX();
    }

    private float magnitude(float factor1, float factor2) { // y, x
        return (float) Math.sqrt(Math.pow(factor1, 2) + Math.pow(factor2, 2));
    }

    private float magnitude(float factor) {
        return magnitude(factor, factor);
    }

    @Override
    public String toString() {
        return "x: " + x + "\ny: " + y;
    }

}
