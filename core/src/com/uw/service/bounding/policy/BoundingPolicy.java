package com.uw.service.bounding.policy;

public sealed interface BoundingPolicy
        permits AddSolidManual, AutoFill, FullManual, ManualEmpty {
}
