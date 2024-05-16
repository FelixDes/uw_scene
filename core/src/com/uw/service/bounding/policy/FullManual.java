package com.uw.service.bounding.policy;

import com.uw.service.bounding.Boundary;

import java.util.Set;

public record FullManual(
        Set<Boundary> solid,
        Set<Boundary> empty
) implements BoundingPolicy {
    public static FullManual of(Set<Boundary> solid, Set<Boundary> empty) {
        return new FullManual(solid, empty);
    }
}
