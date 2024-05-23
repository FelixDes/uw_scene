package com.uw.exception;

public class OutOfTerrain extends RuntimeException{
    public OutOfTerrain() {
        super("Player fall from terrain");
    }
}
