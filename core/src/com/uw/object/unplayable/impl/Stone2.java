package com.uw.object.unplayable.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.uw.object.unplayable.BasicGltfObject;

import static com.uw.ConstantsKt.STONE2;
import static com.uw.service.collision.CollisionPolicyStrategy.NEVER;

public class Stone2 extends BasicGltfObject {
    public Stone2(Vector3 position) {
        super(position, Gdx.files.internal(STONE2), NEVER);
    }
}