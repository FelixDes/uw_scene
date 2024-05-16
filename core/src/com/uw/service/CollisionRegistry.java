package com.uw.service;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.uw.object.unplayable.BasicObject;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public class CollisionRegistry {
    public enum ObjectType {TERRAIN, COMMON_OBJECT, INTERACTABLE}

    private final EnumMap<ObjectType, Set<BasicObject>> boxesMap = new EnumMap<>(ObjectType.class);

    public void add(ObjectType key, BasicObject object) {
        var b = boxesMap.getOrDefault(key, new HashSet<>(10));
        b.add(object);
        boxesMap.put(key, b);
    }

    public Set<BasicObject> getIntersecting(Vector3 target, ObjectType key) {
        var result = new HashSet<BasicObject>();
        var boxes = boxesMap.get(key);
        if (boxes == null || boxes.isEmpty()) {
            return result;
        }
        for (var entry : boxes) {
            var solids = entry.getSolidBoundingBoxes();
            var empties = entry.getEmptyBoundingBoxes();
            if (empties.stream().noneMatch(it -> it.contains(target))) {
                if (solids.stream().anyMatch(it -> it.contains(target))) {
                    result.add(entry);
                }
            }
        }
        return result;
    }
}
