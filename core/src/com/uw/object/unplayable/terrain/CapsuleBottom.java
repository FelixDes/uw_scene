package com.uw.object.unplayable.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.uw.ConstantsKt.CAPSULE_BOTTOM;

public class CapsuleBottom extends BoundedTerrain {
    public CapsuleBottom(Vector3 position) {
        super(position, Gdx.files.internal(CAPSULE_BOTTOM));
    }

    @Override
    public float getHeight(Vector2 position) {
        return bb.max.y;
    }

    @Override
    public boolean containsInYProjection(Vector2 point) {
        float radius_x = (bb.max.x - bb.min.x) / 2;
        float radius_y = (bb.max.z - bb.min.z) / 2;

        float center_x = radius_x + bb.min.x;
        float center_y = radius_y + bb.min.z;

        return (Math.pow(point.x - center_x, 2) / Math.pow(radius_x, 2)
                + Math.pow(point.y - center_y, 2) / Math.pow(radius_y, 2) < 1);
    }
}
