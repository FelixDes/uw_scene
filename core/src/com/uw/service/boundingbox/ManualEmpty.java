package com.uw.service.boundingbox;

import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.Set;

public record ManualEmpty(
        Set<BoundingBox> empty
) implements BoundingBoxPolicy {
    public static ManualEmpty of(Set<BoundingBox> empty) {
        return new ManualEmpty(empty);
    }
}
