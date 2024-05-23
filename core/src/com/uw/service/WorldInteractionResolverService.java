package com.uw.service;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.uw.domain.Position;
import com.uw.exception.CollisionException;
import com.uw.service.collision.CollisionRegistry;
import com.uw.service.collision.TerrainRegistry;
import com.uw.service.collision.strategy.IgnoreCollisionPolicyStrategy;
import com.uw.service.collision.strategy.NeverCollisionPolicyStrategy;
import com.uw.service.collision.strategy.OverCollisionPolicyStrategy;
import com.uw.service.collision.strategy.OverWithMaxCollisionPolicyStrategy;

import java.util.ArrayList;


public class WorldInteractionResolverService {
    private final TerrainRegistry terrainRegistry;
    private final CollisionRegistry collisionRegistry;

    public enum HeightOffsetResolvingType {KEEP, RESET}

    private final float STEP = 0.007f;

    public WorldInteractionResolverService(TerrainRegistry terrainRegistry, CollisionRegistry collisionRegistry) {
        this.terrainRegistry = terrainRegistry;
        this.collisionRegistry = collisionRegistry;
    }

    public void performPlayerMove(Position srcPosition, Vector3 shift, HeightOffsetResolvingType heightOffsetResolvingType) {
        // Get current offset over terrain
        var heightOffset = switch (heightOffsetResolvingType) {
            case KEEP -> getPlayerOffset(srcPosition);
            case RESET -> 0f;
        };

//        if (!collisionRegistry.getIntersecting(
//                srcPosition.getPos(),
//                COMMON_OBJECT
//        ).isEmpty()) {
//            System.out.println("will throw an error");
//        }

        var srcIntersecting = collisionRegistry.getIntersecting(srcPosition.getPos());

        // Will perform only vector in the horizon
        if (srcIntersecting.isEmpty()) {
            var moved = move(srcPosition, shift);
            // Cannot move
            // Try to move by global x/z
            if (!moved) {
                var vectors = new ArrayList<Vector3>();
                if (shift.x != 0) {
                    vectors.add(new Vector3(shift.x, 0, 0));
                }
                if (shift.z != 0) {
                    vectors.add(new Vector3(0, 0, shift.z));
                }

                for (var vector : vectors) {
                    moved = move(srcPosition, vector);
                    if (moved) break;
                }
            }

            // Enrich with vertical shift (pinned to terrain)
            var dstPosition = srcPosition.getPos();
            dstPosition.y = terrainRegistry.getHeight(new Vector2(dstPosition.x, dstPosition.z))  + heightOffset;
        } else { // Src is in collision.
            throw new CollisionException(srcIntersecting);
        }
    }

    private boolean move(Position srcPosition, Vector3 shift) {
        var requestedShiftLength = shift.len();
        var realShiftLength = 0f;

        var shiftPerStep = shift.cpy().nor().scl(STEP);
        var shiftedPosition = srcPosition.getPos().cpy();

        var moved = false;

        while (true) {
            shiftedPosition.add(shiftPerStep);
            realShiftLength += STEP;
            var intersecting = collisionRegistry
                    .getIntersecting(shiftedPosition)
                    .stream()
                    .filter(o -> switch (o.getCollisionPolicyStrategy()) {
                        case IgnoreCollisionPolicyStrategy ignore -> false;
                        case NeverCollisionPolicyStrategy ignore -> true;
                        case OverCollisionPolicyStrategy ignore -> true;
                        case OverWithMaxCollisionPolicyStrategy ignore -> true;
                    })
                    .toList();
            if (intersecting.isEmpty() // pos not in obj
                    && requestedShiftLength >= realShiftLength // pos is not too far from src
                    && !terrainRegistry.getContainsInYProjection(new Vector2(shiftedPosition.x, shiftedPosition.z)).isEmpty()) // pos on terrain TODO: add epsilon bound
            {
                srcPosition.getPos().add(shiftPerStep);
                moved = true;
            } else {
                // todo: handle other strategies
                break;
            }
        }
        return moved;
    }

    private float getPlayerOffset(Position srcPosition) {
        return srcPosition.getPos().y - terrainRegistry.getHeight(new Vector2(srcPosition.getPos().x, srcPosition.getPos().z));
    }
}
