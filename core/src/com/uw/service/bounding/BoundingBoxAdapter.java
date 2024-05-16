package com.uw.service.bounding;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class BoundingBoxAdapter implements Boundary {
    private final BoundingBox bb;

    public BoundingBoxAdapter(Vector3 min, Vector3 max) {
        this(new BoundingBox(min, max));
    }

    public BoundingBoxAdapter(BoundingBox bb) {
        this.bb = bb;
    }

    @Override
    public boolean contains(Vector3 point) {
        return bb.contains(point);
    }

    @Override
    public Boundary mul(Matrix4 transform) {
        bb.mul(transform);
        return this;
    }
}
