package com.uw.service.collision.strategy;

import java.util.function.Function;

// todo: not handled yet
public record OverWithMaxCollisionPolicyStrategy(
        Function<Float, Boolean> shouldOverflow
) implements CollisionPolicyStrategy{
}
