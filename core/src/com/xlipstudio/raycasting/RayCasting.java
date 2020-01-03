package com.xlipstudio.raycasting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.xlipstudio.raycasting.object.Collision;
import com.xlipstudio.raycasting.object.CollisionObject;
import com.xlipstudio.raycasting.object.Pointer;

public class RayCasting extends BaseG3dTest {
    Array<CollisionObject> collisionObjects = new Array<>();
    Model pointerModel;
    Model terrainModel;

    Pointer pointer;
    CollisionObject terrain;
    Environment environment;
    DirectionalLight light;

    @Override
    public void create() {

        super.create();

        cam.position.set(15, 15, 15);
        cam.lookAt(0, 0, 0);

        ModelLoader loader = new ObjLoader();
        terrainModel = loader.loadModel(Gdx.files.internal("terrain.obj"));
        pointerModel = loader.loadModel(Gdx.files.internal("box.obj"));


        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.7f, 1.f));
        light = new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.5f, -1.0f, -0.8f);
        environment.add(light);


        pointer = new Pointer(pointerModel);
        terrain = new CollisionObject(terrainModel);

        terrain.setPosition(0, 0, 0);


        collisionObjects.add(pointer);
        collisionObjects.add(terrain);



        CameraInputController cameraInputController = new CameraInputController(cam) {
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                Ray pickedRay = cam.getPickRay(screenX, screenY);
                Collision collision = terrain.checkIntersection(pickedRay);


                if (collision != null) {
                    Vector3 triNormal = collision.getTriangle().nor();

                    pointer.arrangeDir(triNormal);
                    pointer.setPosition(collision.getBest());
                }
                return super.mouseMoved(screenX, screenY);

            }
        };
        Gdx.input.setInputProcessor(cameraInputController);

    }

    @Override
    protected void render(ModelBatch batch, Array<ModelInstance> instances) {
        batch.render(collisionObjects, environment);
    }
}
