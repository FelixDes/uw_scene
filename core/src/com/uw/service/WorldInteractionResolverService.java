package com.uw.service;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.uw.domain.Position;
import com.uw.exception.CollisionException;
import com.uw.object.unplayable.Terrain;
import com.uw.service.collision.IgnoreCollisionPolicyStrategy;
import com.uw.service.collision.NeverCollisionPolicyStrategy;
import com.uw.service.collision.OverCollisionPolicyStrategy;
import com.uw.service.collision.OverWithMaxCollisionPolicyStrategy;

import static com.uw.service.CollisionRegistry.ObjectType.COMMON_OBJECT;

public class WorldInteractionResolverService {
    private final Terrain terrain;
    private final CollisionRegistry collisionRegistry;

    public enum HeightOffsetResolvingType {KEEP, RESET}

    private final float STEP = 0.1f;

    public WorldInteractionResolverService(Terrain terrain, CollisionRegistry collisionRegistry) {
        this.terrain = terrain;
        this.collisionRegistry = collisionRegistry;
    }

    public void performPlayerMove(Position srcPosition, Vector3 shift, HeightOffsetResolvingType heightOffsetResolvingType) {
        // Get current offset over terrain
        var heightOffset = switch (heightOffsetResolvingType) {
            case KEEP -> getPlayerOffset(srcPosition);
            case RESET -> 0f;
        };

        var srcBoundingBox = new BoundingBox(srcPosition.getPos().cpy(), srcPosition.getPos().cpy());

        var srcIntersecting = collisionRegistry.getIntersecting(
                srcBoundingBox,
                COMMON_OBJECT
        );

        // Will perform only vector in the horizon
        if (srcIntersecting.isEmpty()) {
            var requestedShiftLength = shift.len();
            var realShiftLength = 0f;

            var shiftPerStep = shift.cpy().nor().scl(STEP);
            var shiftedPosition = srcPosition.getPos().cpy();

            while (true) {
                shiftedPosition.add(shiftPerStep);
                var shiftedPosBoundingBox = new BoundingBox(
                        shiftedPosition, shiftedPosition
                );
                realShiftLength += STEP;
                var intersecting = collisionRegistry.getIntersecting(shiftedPosBoundingBox, COMMON_OBJECT).stream().filter(o -> switch (o.getCollisionPolicyStrategy()) {
                    case IgnoreCollisionPolicyStrategy ignore -> false;
                    case NeverCollisionPolicyStrategy ignore -> true;
                    case OverCollisionPolicyStrategy ignore -> true;
                    case OverWithMaxCollisionPolicyStrategy ignore -> true;
                }).toList();
                if (intersecting.isEmpty() && requestedShiftLength >= realShiftLength) {
                    srcPosition.getPos().add(shiftPerStep);
                } else {
                    // todo: handle other strategies
                    break;
                }
            }
        } else { // !! Src is in collision.
            // todo: handle collision
            throw new CollisionException(srcIntersecting);
        }

        // Enrich with vertical shift (pinned to terrain)
        var dstPosition = srcPosition.getPos();
        dstPosition.y = terrain.getHeight(dstPosition.x, dstPosition.z) + heightOffset;
    }

    private float getPlayerOffset(Position srcPosition) {
        return srcPosition.getPos().y - terrain.getHeight(srcPosition.getPos().x, srcPosition.getPos().z);
    }
}
