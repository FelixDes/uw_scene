package com.uw.service.bounding;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class BoundingCylinder implements Boundary {
    private final BoundingBox bb;

    public final Vector3 symmetryAxis;

    public BoundingCylinder(Vector3 min, Vector3 max, Vector3 symmetryAxis) {
        this(new BoundingBox(min, max), symmetryAxis);
    }

    public BoundingCylinder(BoundingBox bb, Vector3 symmetryAxis) {
        this.bb = bb;
        if (symmetryAxis.equals(Vector3.X) || symmetryAxis.equals(Vector3.Y) || symmetryAxis.equals(Vector3.Z)) {
            this.symmetryAxis = symmetryAxis;
        } else {
            throw new IllegalStateException(symmetryAxis + " is not an axes");
        }
    }

    @Override
    public boolean contains(Vector3 point) {
        float center_x;
        float center_y;
        float radius_x;
        float radius_y;
        if (symmetryAxis.equals(Vector3.X)) {
            radius_x = (bb.max.z - bb.min.z) / 2;
            radius_y = (bb.max.y - bb.min.y) / 2;

            center_x = radius_x + bb.min.z;
            center_y = radius_y + bb.min.y;
        } else if (symmetryAxis.equals(Vector3.Y)) {
            radius_x = (bb.max.x - bb.min.x) / 2;
            radius_y = (bb.max.z - bb.min.z) / 2;

            center_x = radius_x + bb.min.x;
            center_y = radius_y + bb.min.z;
        } else if (symmetryAxis.equals(Vector3.Z)) {
            radius_x = (bb.max.x - bb.min.x) / 2;
            radius_y = (bb.max.y - bb.min.y) / 2;

            center_x = radius_x + bb.min.x;
            center_y = radius_y + bb.min.y;
        } else {
            throw new IllegalStateException("impossible");
        }
        return bb.contains(point) && (Math.pow(point.x - center_x, 2) / Math.pow(radius_x, 2)
                + Math.pow(point.z - center_y, 2) / Math.pow(radius_y, 2) < 1);

    }

    @Override
    public Boundary mul(Matrix4 transform) {
        bb.mul(transform);
        return this;
    }
}
