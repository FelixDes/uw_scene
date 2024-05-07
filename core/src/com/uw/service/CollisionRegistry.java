package com.uw.service;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public class CollisionRegistry {
    public enum ObjectType {TERRAIN, OBJECT, INTERACTABLE}

    private final EnumMap<ObjectType, Array<BoundingBox>> boxesMap = new EnumMap<>(ObjectType.class);

    public void add(ObjectType key, BoundingBox box) {
        var b = boxesMap.getOrDefault(key, new Array<>(false, 10));
        b.add(box);
        boxesMap.put(key, b);
    }

    public Set<BoundingBox> getIntersecting(BoundingBox target, ObjectType key) {
        var result = new HashSet<BoundingBox>();
        for (var box : boxesMap.get(key)) {
            if (box.intersects(target)) {
                result.add(box);
            }
        }
        return result;
    }

    public Set<BoundingBox> getIntersecting(Vector3 target, ObjectType key) {
        var result = new HashSet<BoundingBox>();
        for (var box : boxesMap.get(key)) {
            if (box.intersects(target)) {
                result.add(box);
            }
        }
        return result;
    }
}
