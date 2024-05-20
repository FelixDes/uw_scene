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
import com.uw.di.Context;
import com.uw.domain.Resizable;
import com.uw.domain.Updatable;
import com.uw.object.player.BodilessPlayer;
import com.uw.object.unplayable.BasicObject;
import com.uw.object.unplayable.impl.CapsuleTop;
import com.uw.object.unplayable.impl.SandTerrain;
import com.uw.object.unplayable.impl.Stone;
import com.uw.service.WorldInteractionResolverService;
import com.uw.service.collision.CollisionRegistry;
import com.uw.service.overlay.OverlayManager;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalShadowLight;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;
import org.apache.commons.lang3.function.TriConsumer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import static com.uw.service.collision.CollisionRegistry.ObjectType.COMMON_OBJECT;
import static com.uw.service.collision.CollisionRegistry.ObjectType.TERRAIN;

public class UwScene extends ApplicationAdapter {
    private final Set<Disposable> disposables = new HashSet<>();
    private final Set<Updatable> updatables = new HashSet<>();
    private final Set<Resizable> resizables = new HashSet<>();

    private CollisionRegistry cRegistry;

    private BodilessPlayer player;
    private SceneManager sceneManager;

    @Override
    public void create() {
        Context ctx = dis(Context.instance);

        ctx.put(new SceneManager());

        Gdx.input.setCursorPosition(0, 0);
//        Gdx.input.setCursorCatched(true);

        // create scene
        sceneManager = upd(res(ctx.put(new SceneManager()), SceneManager::updateViewport), SceneManager::update);

        // create collision registry
        cRegistry = ctx.put(new CollisionRegistry());

        // create overlay manager
        upd(res(ctx.put(new OverlayManager())));

        // create terrain
        SandTerrain terrain = upd(col(dis(new SandTerrain(new Vector3(0, 0, 0))), TERRAIN));
        sceneManager.addScene(terrain.getScene());

        // create interaction resolver
        var interactionResolver = ctx.put(new WorldInteractionResolverService(terrain, cRegistry));

        // create common objects
        Set.of(
                upd(col(dis(new Stone(new Matrix4()
                        .set(
                                new Vector3(-15, 5, 25),
                                new Quaternion(),
                                new Vector3(5, 5, 5)
                        ))), COMMON_OBJECT)),
                upd(col(dis(new CapsuleTop(new Vector3(0, 13.5f, 0))), COMMON_OBJECT))
        ).forEach(st -> {
            sceneManager.addScene(st.getScene());
        });

        player = upd(new BodilessPlayer(new Vector3(0, terrain.getHeight(0, 0), 0), interactionResolver));
        sceneManager.setCamera(player.getCamera());

        Gdx.input.setInputProcessor(player);


        // Some default shit goes here
        //         |
        //         V

        // setup light
        DirectionalLight light = dis(new DirectionalShadowLight());
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
        Texture brdfLUT = dis(new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png")));
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
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // actual render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.render();

        updatables.forEach(updatable -> updatable.update(deltaTime));
    }

    @Override
    public void resize(int width, int height) {
        resizables.forEach(resizable -> resizable.resize(width, height));
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

    private <T extends Updatable> T upd(T obj) {
        updatables.add(obj);
        return obj;
    }

    private <T> T upd(T obj, BiConsumer<T, Float> adapter) {
        updatables.add((delta) -> adapter.accept(obj, delta));
        return obj;
    }


    private <T extends Resizable> T res(T obj) {
        resizables.add(obj);
        return obj;
    }

    private <T> T res(T obj, TriConsumer<T, Integer, Integer> adapter) {
        resizables.add((w, h) -> adapter.accept(obj, w, h));
        return obj;
    }
}