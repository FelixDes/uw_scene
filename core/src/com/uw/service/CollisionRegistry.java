package com.uw.service;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.uw.object.unplayable.BasicObject;

import java.util.*;

public class CollisionRegistry {
    public enum ObjectType {TERRAIN, COMMON_OBJECT, INTERACTABLE}

    private final EnumMap<ObjectType, Map<BasicObject, BoundingBox>> boxesMap = new EnumMap<>(ObjectType.class);

    public void add(ObjectType key, BasicObject object) {
        var b = boxesMap.getOrDefault(key, new HashMap<>(10));
        var mi = object.getScene().modelInstance;
        b.put(
                object,
                mi.calculateBoundingBox(new BoundingBox()).mul(mi.transform)
        );
        boxesMap.put(key, b);
    }

    public Set<BasicObject> getIntersecting(BoundingBox target, ObjectType key) {
        var result = new HashSet<BasicObject>();
        var boxes =  boxesMap.get(key);
        if (boxes == null) {
            return result;
        }
        for (var entry : boxes.entrySet()) {
            if (entry.getValue().intersects(target)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}
