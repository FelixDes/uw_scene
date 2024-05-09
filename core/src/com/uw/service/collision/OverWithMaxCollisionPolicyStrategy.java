package com.uw.service.collision;

import java.util.function.Function;

// todo: not handled yet
public record OverWithMaxCollisionPolicyStrategy(
        Function<Float, Boolean> shouldOverflow
) implements CollisionPolicyStrategy{
}
