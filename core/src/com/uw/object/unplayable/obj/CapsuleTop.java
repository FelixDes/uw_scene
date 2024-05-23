package com.uw.object.unplayable.obj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.uw.service.bounding.BoundingBoxAdapter;
import com.uw.service.bounding.BoundingCylinder;
import com.uw.service.bounding.policy.FullManual;

import java.util.Set;

import static com.uw.ConstantsKt.CAPSULE_TOP;
import static com.uw.service.collision.strategy.CollisionPolicyStrategy.NEVER;

public class CapsuleTop extends BoundedObject {
    public CapsuleTop(Vector3 position) {
        super(position, Gdx.files.internal(CAPSULE_TOP), NEVER, FullManual.of(
                Set.of(new BoundingCylinder(
                        new Vector3(-18.75f,-10f,-18.75f),
                        new Vector3(18.75f, 34f,18.75f),
                        Vector3.Y
                )),
                Set.of(new BoundingBoxAdapter(
                        new Vector3(-2.7f, -5, -18.75f),
                        new Vector3(2.7f, 13.0f, 18.75f)
                ))
        ));
    }
}
    