package com.uw.object.unplayable.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.uw.object.unplayable.BasicObject;
import com.uw.service.bounding.policy.AutoFill;

import static com.uw.ConstantsKt.STONE;
import static com.uw.service.collision.CollisionPolicyStrategy.NEVER;

public class Stone extends BasicObject {
    public Stone(Vector3 position) {
        super(position, Gdx.files.internal(STONE), NEVER, AutoFill.get());
    }

    public Stone(Matrix4 transform) {
        super(transform, Gdx.files.internal(STONE), NEVER, AutoFill.get());
    }
}