package com.xlipstudio.raycasting.object;

import com.badlogic.gdx.math.Vector3;

public class Collision {
    private Triangle triangle;
    private Vector3 best;

    public Collision(Triangle triangle, Vector3 best) {
        this.triangle = triangle;
        this.best = best;
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public void setTriangle(Triangle triangle) {
        this.triangle = triangle;
    }

    public Vector3 getBest() {
        return best;
    }

    public void setBest(Vector3 best) {
        this.best = best;
    }

    @Override
    public String toString() {
        return triangle.toString() + "Best: " + best;
    }
}
