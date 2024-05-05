package com.uw;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;

public interface ViewAttachable {

    InputProcessor getInputProcessor();

    PerspectiveCamera getCamera();
}
