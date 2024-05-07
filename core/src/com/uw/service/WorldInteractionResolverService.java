package com.uw.service;

import com.badlogic.gdx.math.Vector3;
import com.uw.domain.Position;
import com.uw.object.unplayable.Terrain;

public class WorldInteractionResolverService {
    private final Terrain terrain;
    public enum CollisionPolicy {OVER, IGNORE}

    public WorldInteractionResolverService(Terrain terrain) {
        this.terrain = terrain;
    }

    public void performMove(Position srcPosition, Vector3 shift) {
        var dst = srcPosition.getPos().cpy().add(shift);
        return dst;
    }
}
