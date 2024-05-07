package com.uw.object.unplayable.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.uw.object.unplayable.BasicGltfObject;

import static com.uw.ConstantsKt.STONE;

public class Stone extends BasicGltfObject {
    public Stone(Vector3 position) {
        super(position, Gdx.files.internal(STONE));
    }
}
    