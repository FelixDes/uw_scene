package com.uw.service.collision;

import com.badlogic.gdx.math.Vector2;
import com.uw.object.unplayable.terrain.Terrain;

import java.util.HashSet;
import java.util.Set;

public class TerrainRegistry {
    private final Set<Terrain> terrains = new HashSet<>();

    public void add(Terrain object) {
        terrains.add(object);
    }

    public Set<Terrain> getContainsInYProjection(Vector2 target) {
        var result = new HashSet<Terrain>();
        if (!terrains.isEmpty()) {
            for (var entry : terrains) {
                if (entry.containsInYProjection(target)) {
                    result.add(entry);
                }
            }
        }
        return result;
    }

    public float getHeight(Vector2 target) {
        float max = Float.MIN_VALUE;
        for (Terrain t : getContainsInYProjection(target)) {
            float height = t.getHeight(target);
            if (height > max) {
                max = height;
            }
        }
        return max;
    }
}
