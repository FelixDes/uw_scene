package com.uw.object.unplayable.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.uw.object.unplayable.BasicObject;
import com.uw.service.boundingbox.AddSolidManual;

import java.util.Set;

import static com.uw.ConstantsKt.CAPSULE_TOP;
import static com.uw.service.collision.CollisionPolicyStrategy.NEVER;

public class CapsuleTop extends BasicObject {
    public CapsuleTop(Vector3 position) {
        super(position, Gdx.files.internal(CAPSULE_TOP), NEVER, AddSolidManual.of(
                Set.of(new BoundingBox(
                        new Vector3(-18.750004f,-10f,-18.750011f),
                        new Vector3(18.75f,33.352787f,18.750004f)
                )),
                Set.of(new BoundingBox(
                        new Vector3(-18.76f, 0, -3f),
                        new Vector3(18.76f, 13.3f, 3)
                ))
        ));
    }
}
    