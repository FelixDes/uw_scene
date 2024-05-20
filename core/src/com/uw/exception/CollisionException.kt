package com.uw.exception

import com.uw.`object`.unplayable.BasicObject
import java.util.stream.Collectors

class CollisionException(intersections: Set<BasicObject>) :
    RuntimeException(
        "User stuck in object: " + intersections.stream().map { obj: BasicObject -> obj.toString() }
            .collect(Collectors.joining(", "))
    )
