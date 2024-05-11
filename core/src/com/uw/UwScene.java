package com.uw;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.uw.object.player.BodilessPlayer;
import com.uw.object.unplayable.BasicObject;
import com.uw.object.unplayable.impl.SandTerrain;
import com.uw.object.unplayable.impl.Stone;
import com.uw.service.CollisionRegistry;
import com.uw.service.WorldInteractionResolverService;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalShadowLight;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

import java.util.HashSet;
import java.util.Set;

import static com.uw.service.CollisionRegistry.ObjectType.COMMON_OBJECT;
import static com.uw.service.CollisionRegistry.ObjectType.TERRAIN;

public class UwScene extends ApplicationAdapter {
    private final Set<Disposable> disposables = new HashSet<>();
    private final Set<RenderUpdatable> updatables = new HashSet<>();

    private CollisionRegistry cRegistry;

    private BodilessPlayer player;
    private SceneManager sceneManager;

    @Override
    public void create() {
        Gdx.input.setCursorPosition(0, 0);
        Gdx.input.setCursorCatched(true);

        // create scene
        sceneManager = dis(new SceneManager());

        // create collision registry
        cRegistry = new CollisionRegistry();

        // create terrain
        SandTerrain terrain = col(dis(new SandTerrain(new Vector3(0, 0, 0))), TERRAIN);
        sceneManager.addScene(terrain.getScene());

        // create interaction resolver
        var interactionResolver = new WorldInteractionResolverService(terrain, cRegistry);

        // create common objects
        Set.of(
                col(dis(new Stone(new Matrix4()
                        .set(
                                new Vector3(-10, 5, 13),
                                new Quaternion(),
                                new Vector3(5, 5, 5)
                        ))), COMMON_OBJECT)
        ).forEach(st -> {
            sceneManager.addScene(st.getScene());
        });

        player = new BodilessPlayer(new Vector3(0, terrain.getHeight(0, 0), 0), interactionResolver);
        sceneManager.setCamera(player.getCamera());

        Gdx.input.setInputProcessor(player);
        updatables.add(player);


        // Some default shit goes here
        //         |
        //         V

        // setup light
        DirectionalLight light = new DirectionalShadowLight();
        light.direction.set(1, -4, 1).nor();
        light.color.set(Color.rgba8888(96, 151, 240, 1));
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        Cubemap environmentCubemap = iblBuilder.buildEnvMap(1024);
        disposables.add(environmentCubemap);
        Cubemap diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        disposables.add(diffuseCubemap);
        Cubemap specularCubemap = iblBuilder.buildRadianceMap(10);
        disposables.add(specularCubemap);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        Texture brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));
        disposables.add(brdfLUT);

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        SceneSkybox skybox = new SceneSkybox(environmentCubemap);
        disposables.add(skybox);
        sceneManager.setSkyBox(skybox);
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }

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
        disposables.forEach(Disposable::dispose);
    }

    private <T extends Disposable> T dis(T obj) {
        disposables.add(obj);
        return obj;
    }

    private <T extends BasicObject> T col(T obj, CollisionRegistry.ObjectType type) {
        cRegistry.add(type, obj);
        return obj;
    }

    private <T extends BasicObject> T upd(T obj) {
        updatables.add(obj);
        return obj;
    }
}