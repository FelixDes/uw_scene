package com.uw.service.bounding.policy;

public record AutoFill() implements BoundingPolicy {
    public static AutoFill get() {
        return new AutoFill();
    }
}
