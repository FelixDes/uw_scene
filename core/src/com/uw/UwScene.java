package com.uw;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.uw.object.player.BodilessPlayer;
import com.uw.object.unplayable.BasicObject;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

import java.util.HashSet;
import java.util.Set;

public class UwScene extends ApplicationAdapter {
    private Set<Disposable> disposables = new HashSet<>();
    private Set<RenderUpdatable> updatables = new HashSet<>();

    private BodilessPlayer player;


    private GLTFLoader gltfLoader;
    private SceneManager sceneManager;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private SceneSkybox skybox;
    private DirectionalLightEx light;

//    private Matrix4 playerTransform = new Matrix4();
//    private Vector3 moveTransition = new Vector3();
//    private Vector3 curPosition = new Vector3();

    private float camHeight = 5f;

    @Override
    public void create() {
        // create scene
        sceneManager = new SceneManager();
        gltfLoader = new GLTFLoader();

        var stone1Asset = gltfLoader.load(Gdx.files.internal("3d/stones/stone1/stone1.gltf"));
        disposables.add(stone1Asset);
        var stone1 = new BasicObject(new Vector3(0, 0, 0), stone1Asset);
        sceneManager.addScene(stone1.getScene());

        player = new BodilessPlayer(new Vector3(0, camHeight, -4f));
        sceneManager.setCamera(player.getCamera());

        updatables.add(player);


        // setup light
        light = new DirectionalLightEx();
        light.direction.set(1, 0, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("2d/img.png"));

        sceneManager.setAmbientLight(1000f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }

//    private void processInput(float deltaTime) {
//        playerTransform.set(scene.modelInstance.transform);
//
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            moveTransition.z += speed * deltaTime;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            moveTransition.z -= speed * deltaTime;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            moveTransition.x += speed * deltaTime;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            moveTransition.x -= speed * deltaTime;
//        }
//
//        playerTransform.translate(moveTransition);
//
//        scene.modelInstance.transform.set(playerTransform);
//
//        scene.modelInstance.transform.getTranslation(curPosition);
//
//        moveTransition.set(0, 0, 0);
//    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

//        processInput(deltaTime);
        updatables.forEach(updatable -> updatable.update(deltaTime));
        player.update(deltaTime);

        // render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.update(deltaTime);
        sceneManager.render();
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        disposables.forEach(Disposable::dispose);
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
    }
}