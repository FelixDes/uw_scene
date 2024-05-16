package com.uw.service.bounding.policy;

import com.uw.service.bounding.Boundary;

import java.util.Set;

public record AddSolidManual(
        Set<Boundary> addSolid,
        Set<Boundary> empty
)
        implements BoundingPolicy {
    public static AddSolidManual of(Set<Boundary> addSolid, Set<Boundary> empty) {
        return new AddSolidManual(addSolid, empty);
    }
}
    