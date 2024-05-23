package com.uw.object.unplayable.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.uw.exception.OutOfTerrain;

import static com.uw.ConstantsKt.SAND_TERRAIN_HEIGHTMAP;
import static com.uw.ConstantsKt.SAND_TERRAIN_MODEL;


public class SandTerrain extends BoundedTerrain {
    protected final Pixmap heightMap;

    public SandTerrain(Vector3 position) {
        super(position, Gdx.files.internal(SAND_TERRAIN_MODEL));
        this.heightMap = new Pixmap(Gdx.files.internal(SAND_TERRAIN_HEIGHTMAP));

        this.sceneAsset.textures.forEach(texture -> texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear));
    }

    @Override
    public float getHeight(Vector2 position) {
        float h1 = bb.getDepth();
        float w1 = bb.getWidth();

        float h2 = heightMap.getHeight();
        float w2 = heightMap.getWidth();

        float x1 = position.x - bb.min.x;
        float y1 = position.y - bb.min.z;

        int x2 = (int) (w2 * x1 / w1);
        int y2 = (int) (h2 * y1 / h1);

        if (!(0 <= x2 && x2 <= w2 && 0 <= y2 && y2 <= h2)) {
            throw new OutOfTerrain();
        }

        float height = bb.getHeight();
        float offset = bb.min.y;

        var c = new Color(heightMap.getPixel(x2, y2));

        float midColorCoefficient = (c.r + c.g + c.b) / 3f;

        return (height + offset) * midColorCoefficient;
    }

    @Override
    public boolean containsInYProjection(Vector2 position) {
        float h1 = bb.getDepth();
        float w1 = bb.getWidth();

        float h2 = heightMap.getHeight();
        float w2 = heightMap.getWidth();

        float x1 = position.x - bb.min.x;
        float y1 = position.y - bb.min.z;

        int x2 = (int) (w2 * x1 / w1);
        int y2 = (int) (h2 * y1 / h1);

        return 0 <= x2 && x2 <= w2 && 0 <= y2 && y2 <= h2;
    }
}
    