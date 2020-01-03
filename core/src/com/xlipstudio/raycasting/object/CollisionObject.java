package com.xlipstudio.raycasting.object;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

public class CollisionObject extends ModelInstance {
    float[] meshPartVertices = {};
    short[] meshPartIndices = {};
    int vertexSize;
    Array<Vector3> terrainVertices = new Array<>();

    private Vector3 tempV = new Vector3();
    private Vector3 position = new Vector3();


    public CollisionObject(Model model) {
        super(model);

        //only one part

        MeshPart mp = model.meshParts.get(0);
        Mesh terrainMesh = mp.mesh.copy(false);
        meshPartIndices = new short[mp.size];
        terrainMesh.getIndices(mp.offset, mp.size, meshPartIndices, 0);
        vertexSize = terrainMesh.getVertexSize() / 4;
        meshPartVertices = new float[terrainMesh.getNumVertices() * vertexSize];
        terrainMesh.getVertices(meshPartVertices);

        update();
    }


    public CollisionObject setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
        transform.setTranslation(this.position);
        update();
        return this;
    }

    public CollisionObject setPosition(Vector3 position) {
        return this.setPosition(position.x, position.y, position.z);
    }

    public Vector3 getPosition() {
        return transform.getTranslation(position);
    }

    private void update() {
        terrainVertices.clear();

        for (int i = 0; i < meshPartIndices.length; i++) {
            int i1 = meshPartIndices[i] * vertexSize;
            Vector3 v = new Vector3(meshPartVertices[i1], meshPartVertices[i1 + 1], meshPartVertices[i1 + 2]);

            v.set(v.prj(transform));
            terrainVertices.add(v);
        }

    }


    public Collision checkIntersection(Ray ray) {
        Array<Collision> bests = new Array<>();


        for (int i = 0; i < terrainVertices.size; i += 3) {
            Vector3 p1 = terrainVertices.get(i);
            Vector3 p2 = terrainVertices.get(i + 1);
            Vector3 p3 = terrainVertices.get(i + 2);
            Vector3 nearsest = new Vector3();

            if (Intersector.intersectRayTriangle(ray, p1, p2, p3, nearsest)) {
                bests.add(new Collision(new Triangle(p1, p2, p3), nearsest));
            }
        }

        if (bests.size == 0) return null;
        if (bests.size == 1) return bests.get(0);
        Collision bestCollision = bests.get(0);

        for (Collision collision : bests) {
            if (collision.getBest().dst(ray.origin) < bestCollision.getBest().dst(ray.origin)) {
                bestCollision = collision;
            }
        }

        return bestCollision;
    }

    public void rotateAround(Vector3 point, Vector3 axis, float angle) {
        tempV.set(point);
        tempV.sub(position);
        position.add(tempV);
        transform.rotate(axis, angle);
        tempV.rotate(axis, angle);
        position.add(tempV.scl(-1));
        transform.setTranslation(position);
    }


}
