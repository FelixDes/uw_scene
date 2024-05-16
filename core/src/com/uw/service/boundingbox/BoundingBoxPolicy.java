package com.uw.service.boundingbox;

public sealed interface BoundingBoxPolicy
        permits AutoFill, ManualEmpty, AddSolidManual {
}
