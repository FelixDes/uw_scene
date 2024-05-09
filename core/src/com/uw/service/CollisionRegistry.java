package com.uw.service;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.uw.object.unplayable.BasicGltfObject;

import java.util.*;

public class CollisionRegistry {
    public enum ObjectType {TERRAIN, COMMON_OBJECT, INTERACTABLE}

    private final EnumMap<ObjectType, Map<BasicGltfObject, BoundingBox>> boxesMap = new EnumMap<>(ObjectType.class);

    public void add(ObjectType key, BasicGltfObject object) {
        var b = boxesMap.getOrDefault(key, new HashMap<>(10));
        var mi = object.getScene().modelInstance;
        b.put(
                object,
                mi.calculateBoundingBox(new BoundingBox()).mul(mi.transform)
        );
        boxesMap.put(key, b);
    }

    public Set<BasicGltfObject> getIntersecting(BoundingBox target, ObjectType key) {
        var result = new HashSet<BasicGltfObject>();
        for (var entry : boxesMap.get(key).entrySet()) {
            if (entry.getValue().intersects(target)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}
