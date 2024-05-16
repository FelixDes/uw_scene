package com.uw.service.boundingbox;

public record AutoFill() implements BoundingBoxPolicy{
    public static AutoFill get() {
        return new AutoFill();
    }
}
