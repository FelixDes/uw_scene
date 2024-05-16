package com.uw.service.bounding.policy;

import com.uw.service.bounding.Boundary;

import java.util.Set;

public record ManualEmpty(
        Set<Boundary> empty
) implements BoundingPolicy {
    public static ManualEmpty of(Set<Boundary> empty) {
        return new ManualEmpty(empty);
    }
}
