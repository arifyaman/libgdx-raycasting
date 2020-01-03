package com.xlipstudio.raycasting.object;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

public class Pointer extends CollisionObject {
    private Vector3 up = new Vector3();
    private Vector3 tmpVec = new Vector3();

    public Pointer(Model model) {
        super(model);
        up.set(0, 1, 0);

    }

    public Pointer arrangeDir(Vector3 trinormal) {
        float yAng = (float) Math.acos(trinormal.y);
        float zAng = (float) Math.asin(trinormal.z);

        if (trinormal.x > 0) yAng *= -1;

        transform.idt();


        if (trinormal.x != 0) {
            transform.rotateRad(Vector3.Z, yAng);
        }
        if (trinormal.z != 0) {
            transform.rotateRad(Vector3.X, zAng);
        }

        return this;
    }

    private void updateUp(Vector3 direction) {
        tmpVec.set(direction).crs(up).nor();
        up.set(tmpVec).crs(direction).nor();
    }


}
