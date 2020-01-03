package com.xlipstudio.raycasting.object;

import com.badlogic.gdx.math.Vector3;

public class Triangle  {
    Vector3 p1, p2, p3;


    public Triangle(Vector3 p1, Vector3 p2, Vector3 p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Vector3 nor() {
        Vector3 a = new Vector3(p2).sub(p1);
        Vector3 b = new Vector3(p3).sub(p1);
        return a.crs(b).nor();
    }

    @Override
    public String toString() {
        return "-----\n" + p1.toString() + "\n"+ p2.toString() + "\n"+ p3.toString() + "\n" + "-----\n";
    }
}
