package com.uw.object.unplayable.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.uw.object.unplayable.Terrain;


public class SandTerrain extends Terrain {
    private final BoundingBox boundingBox;

    public SandTerrain(Vector3 position) {
        super(position, Gdx.files.internal("3d/terrain/terrain.gltf"), new Pixmap(Gdx.files.internal("3d/terrain/heightmap.png")));
        this.sceneAsset.textures.forEach(texture -> texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear));

        var mi = new ModelInstance(this.sceneAsset.scene.model);
        boundingBox = mi.calculateBoundingBox(new BoundingBox()).mul(mi.transform);
    }

    @Override
    public float getHeight(float x, float z) {
        float h1 = boundingBox.getDepth();
        float w1 = boundingBox.getWidth();

        float h2 = heightMap.getHeight();
        float w2 = heightMap.getWidth();

        float x1 = x - boundingBox.min.x;
        float y1 = z - boundingBox.min.z;

        int x2 = (int) (w2 * x1 / w1);
        int y2 = (int) (h2 * y1 / h1);

        var height = boundingBox.getHeight();
        var offset = Math.abs(boundingBox.min.y);

        var c = heightMap.getPixel(x2, y2);
        int r = (c >> 24) & 0xFF;
        int g = (c >> 16) & 0xFF;
        int b = (c >> 8) & 0xFF;
        int al = c & 0xFF;

        System.out.println("Red: " + r);
        System.out.println("Green: " + g);
        System.out.println("Blue: " + b);
        System.out.println("Alpha: " + al);
        System.out.println(c);
        // todo: calc colorCoefficient
        var colorCoefficient = 1;

        return (height + offset) * colorCoefficient;
    }
}
    