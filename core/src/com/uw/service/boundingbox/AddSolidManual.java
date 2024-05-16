package com.uw.service.boundingbox;

import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.Set;

public record AddSolidManual(
        Set<BoundingBox> addSolid,
        Set<BoundingBox> empty
)
        implements BoundingBoxPolicy {
    public static AddSolidManual of(Set<BoundingBox> addSolid, Set<BoundingBox> empty) {
        return new AddSolidManual(addSolid, empty);
    }
}
    