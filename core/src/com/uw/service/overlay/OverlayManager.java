package com.uw.service.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.uw.domain.Resizable;
import com.uw.domain.Updatable;

public class OverlayManager implements Updatable, Resizable {

    private Array<Overlay> overlays = new Array<Overlay>();
    private Array<Overlay> toRemove = new Array<Overlay>();

    // add a new overlay on top of the rest
    public void push(Overlay overlay) {
        Overlay current = current();
        if(current != null) current.pause();

        overlays.add(overlay);

        overlay.show();
    }

    public void pop() {
        Overlay overlay = current();
        overlay.hide();

        if(overlays.size > 1) {
            Overlay previous = overlays.get(overlays.size - 2);
            previous.resume();
        }
    }

    // hide a specific overlay
    public void remove(Overlay overlay) {
        int index = overlays.indexOf(overlay, true);
        if(index >= 0) {
            overlay.hide();
        }

        if(overlays.size > 1 && index > 0) {
            Overlay previous = overlays.get(index - 1);
            previous.resume();
        }
    }

    public void replace(Overlay existing, Overlay newOverlay) {
        int index = overlays.indexOf(existing, true);
        if(index >= 0) {
            overlays.set(index, newOverlay);
            newOverlay.show(false);
            newOverlay.matchInputSettings(existing);
        }
    }

    public void replaceCurrent(Overlay newOverlay) {
        replace(current(), newOverlay);
    }

    // hide all of the overlays
    public void clear() {
        for (int i = 0; i < overlays.size; i++) {
            Overlay overlay = overlays.get(i);
            overlay.hide();
        }
    }

    // return the overlay on top
    public Overlay current() {
        if(overlays.size == 0) return null;
        return overlays.peek();
    }

    // draw all of the overlays, in order
    public void update(float delta) {

        var gl = Gdx.gl;

        if(overlays.size > 0) {
            gl.glDisable(GL20.GL_CULL_FACE);
            gl.glDisable(GL20.GL_DEPTH_TEST);
        }

        for(int i = 0; i < overlays.size; i++) {
            Overlay o = overlays.get(i);
            o.tick(delta);

            if(!o.visible) toRemove.add(o);
            else o.draw(delta);
        }

        if(overlays.size > 0) {
            gl.glEnable(GL20.GL_CULL_FACE);
            gl.glEnable(GL20.GL_DEPTH_TEST);
        }

        for(int i = 0; i < toRemove.size; i++) {
            overlays.removeValue(toRemove.get(i), true);
        }

        toRemove.clear();
    }

    // check whether the latest overlay wants to pause the game
    public boolean shouldPauseGame() {
        if(overlays.size == 0) return false;
        return current().pausesGame;
    }

    public void resize(int newWidth, int newHeight) {
        for(int i = 0; i < overlays.size; i++) {
            Overlay o = overlays.get(i);
            o.resize(newWidth, newHeight);
        }
    }
}