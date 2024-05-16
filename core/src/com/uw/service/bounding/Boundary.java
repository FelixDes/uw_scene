package com.uw.service.bounding;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public interface Boundary {
    boolean contains(Vector3 point);

    Boundary mul(Matrix4 transform);
}
