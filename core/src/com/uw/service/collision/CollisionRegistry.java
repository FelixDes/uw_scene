package com.uw.service.collision;

import com.badlogic.gdx.math.Vector3;
import com.uw.object.unplayable.BasicObject;
import com.uw.object.unplayable.obj.BoundedObject;

import java.util.HashSet;
import java.util.Set;

public class CollisionRegistry {
    private final Set<BoundedObject> objects = new HashSet<>();

    public void add(BoundedObject object) {
        objects.add(object);
    }

    public Set<BasicObject> getIntersecting(Vector3 target) {
        var result = new HashSet<BasicObject>();
        if (!objects.isEmpty()) {
            for (var entry : objects) {
                var solids = entry.getSolidBoundingBoxes();
                var empties = entry.getEmptyBoundingBoxes();
                if (empties.stream().noneMatch(it -> it.contains(target))) {
                    if (solids.stream().anyMatch(it -> it.contains(target))) {
                        result.add(entry);
                    }
                }
            }
        }
        return result;
    }
}
