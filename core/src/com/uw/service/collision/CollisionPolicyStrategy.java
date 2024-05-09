package com.uw.service.collision;

public sealed interface CollisionPolicyStrategy permits
        IgnoreCollisionPolicyStrategy,
        NeverCollisionPolicyStrategy,
        OverCollisionPolicyStrategy,
        OverWithMaxCollisionPolicyStrategy {

    CollisionPolicyStrategy OVER = new OverCollisionPolicyStrategy();
    CollisionPolicyStrategy NEVER = new NeverCollisionPolicyStrategy();
    CollisionPolicyStrategy IGNORE = new IgnoreCollisionPolicyStrategy();
}
