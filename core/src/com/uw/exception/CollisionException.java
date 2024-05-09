package com.uw.exception;

import com.uw.object.unplayable.BasicGltfObject;

import java.util.Set;
import java.util.stream.Collectors;

public class CollisionException extends RuntimeException {
    public CollisionException(Set<BasicGltfObject> intersections) {
        super("User stuck in object: " + intersections.stream().map(BasicGltfObject::toString).collect(Collectors.joining(", ")));
    }
}
    