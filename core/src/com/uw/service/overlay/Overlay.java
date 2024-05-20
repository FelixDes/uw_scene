package com.uw.service.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;


public abstract class Overlay {
    protected Stage ui;
    protected GL20 gl = Gdx.gl;
    public boolean pausesGame = true;
    public boolean visible = false;
    public boolean running = false;
    public boolean showCursor = true;
    public boolean catchInput = true;

    private InputProcessor previousInputProcessor = null;

    private boolean cursorWasShownBefore = false;

    public void show() {
        show(true);
    }

    public void show(boolean setInputSettings) {
        visible = true;
        running = true;

        if (setInputSettings) {
            cursorWasShownBefore = !Gdx.input.isCursorCatched();

            if (catchInput) {
                previousInputProcessor = Gdx.input.getInputProcessor();
            }
        }

        onShow();
    }

    public void hide() {
        visible = false;
        running = false;

        if (showCursor != cursorWasShownBefore) {
            Gdx.input.setCursorCatched(!cursorWasShownBefore);
        }

        if (previousInputProcessor != null)
            Gdx.input.setInputProcessor(previousInputProcessor);

        onHide();
    }

    protected void draw(float delta) {
        if (ui != null) {
            if (running) ui.act(delta);
            ui.draw();
        }
    }

    public abstract void tick(float delta);

    public abstract void onShow();

    public abstract void onHide();

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
    }

    public void resize(int width, int height) {
        if (ui != null && ui.getViewport() != null) {
            Viewport viewport = ui.getViewport();
            viewport.setWorldHeight(height / 2);
            viewport.setWorldWidth(width / 2);
            viewport.update(width, height, true);
        }
    }

    public void matchInputSettings(Overlay existing) {
        previousInputProcessor = existing.previousInputProcessor;
        cursorWasShownBefore = existing.cursorWasShownBefore;
    }
}
