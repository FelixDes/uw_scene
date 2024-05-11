package com.uw.exception;

import com.uw.object.unplayable.BasicObject;

import java.util.Set;
import java.util.stream.Collectors;

public class CollisionException extends RuntimeException {
    public CollisionException(Set<BasicObject> intersections) {
        super("User stuck in object: " + intersections.stream().map(BasicObject::toString).collect(Collectors.joining(", ")));
    }
}
    