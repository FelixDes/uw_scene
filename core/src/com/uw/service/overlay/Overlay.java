package com.uw.service.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

//package com.uw.service.overlay;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Pixmap;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.*;
//import com.badlogic.gdx.math.Interpolation;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
//import com.badlogic.gdx.utils.Align;
//import com.badlogic.gdx.utils.Scaling;
//import com.badlogic.gdx.utils.viewport.ScalingViewport;
//import com.badlogic.gdx.utils.viewport.Viewport;
//
//public class Overlay {
//    Stage ui;
//    Table stageTable;
//    Skin skin;
//
//    GL20 gl;
//    protected NinePatchDrawable background;
//
//    private void getSkin() {
//        if (skin != null) return;
//        var at = new TextureAtlas(Gdx.files.internal("ui/skin.atlas"));
//
//        Texture windowTexture = new Texture(Gdx.files.internal("ui/window.png"));
//        TextureAtlas.AtlasRegion windowRegion1 = at.addRegion("window", windowTexture, 0, 0, windowTexture.getWidth(), windowTexture.getHeight());
//
//        windowRegion1.names = new String[]{"split", "pad"};
//        windowRegion1.values = new int[][]{{8, 8, 8, 8}, {0, 0, 0, 0}};
//
//        var skin = new Skin(
//                Gdx.files.internal("ui/skin.json"), at
//        );
//
//        var font = new BitmapFont(Gdx.files.internal("ui/pixel.fnt"), Gdx.files.internal("ui/pixel.png"), false, true);
//        font.getData().markupEnabled = true;
//        font.getData().padBottom = 0f;
//        font.getData().padTop = 0f;
//        font.getData().padLeft = 0f;
//        font.getData().padRight = 0f;
//        skin.add("default-font", font);
//
//
//        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
//        labelStyle.font = font;
//        skin.add("default", labelStyle);
//
//
//        this.skin = skin;
//    }
//
//    public void show() {
//        getSkin();
//        background = new NinePatchDrawable(new NinePatch(skin.getRegion("window"), 11, 11, 11, 11));
//
//        int width = Gdx.graphics.getWidth();
//        int height = Gdx.graphics.getHeight();
//
//        int scaleFactor = width < height * 1.5 ? width : height;
//        float scaleMod = width < height * 1.5 ? 480f * 1.5f : 480f;
//        var uiScale = (scaleFactor / scaleMod) * 3f;
//
//
//        //FillViewport viewport = new FillViewport(Gdx.graphics.getWidth() / uiScale, Gdx.graphics.getHeight() / uiScale);
//        ScalingViewport viewport = new ScalingViewport(Scaling.fill, 850f, 480f);
//        ui = new Stage(viewport);
//
//        resize(width, height);
//
//        makeLayout();
//    }
//
//    protected float lerpValue = 0;
//    Interpolation lerp = Interpolation.sine;
//    private Color animateColor = new Color();
//    Vector2 offset = new Vector2();
//    SpriteBatch uiBatch = new SpriteBatch();
//
//    public void draw() {
//        gl = Gdx.gl;
//
//        gl.glDisable(GL20.GL_CULL_FACE);
//        gl.glDisable(GL20.GL_DEPTH_TEST);
//
//        lerpValue = 1f;
//        float moveLerpValue = 0f;
//
//        uiBatch.begin();
//        uiBatch.enableBlending();
//
//        uiBatch.setColor(0, 0, 0, 0.5f);
//
//        Pixmap flashPixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
//        flashPixmap.setColor(Color.WHITE);
//        flashPixmap.fill();
//        var flashTex = new Texture(flashPixmap);
//        var flashRegion = new TextureRegion(flashTex, 0, 0, 2, 2);
//
//        uiBatch.draw(flashRegion,
//                -100,
//                -100,
//                100,
//                100
//        );
//        uiBatch.end();
//        uiBatch.disableBlending();
//
//
//        stageTable.setPosition(0 + offset.x, moveLerpValue + offset.y);
//
//        animateColor.set(1, 1, 1, 1f * lerpValue);
//        stageTable.setColor(animateColor);
//
//        gl = Gdx.gl;
//        if (ui != null) {
//            ui.draw();
//        }
//
//        gl.glEnable(GL20.GL_CULL_FACE);
//        gl.glEnable(GL20.GL_DEPTH_TEST);
//    }
//
//    protected void makeLayout() {
//        if (stageTable == null) {
//            stageTable = new Table();
//            ui.addActor(stageTable);
//        } else {
//            stageTable.clear();
//        }
//
//        stageTable.setFillParent(true);
//        stageTable.align(Align.center);
//
//        Table mainTable = new Table();
//        if (background != null) mainTable.setBackground(background);
//        stageTable.add(mainTable).fill();
//
//        Table contentTable = new Table(skin);
//        contentTable.add(new Label("!@#$%^&*(", skin.get(Label.LabelStyle.class)));
//        mainTable.add(contentTable).pad(4f);
//    }
//
//    public void resize(int width, int height) {
//        if (ui != null && ui.getViewport() != null) {
//            Viewport viewport = ui.getViewport();
//            viewport.setWorldHeight(height);
//            viewport.setWorldWidth(width);
//            viewport.update(width, height, true);
//        }
//    }
//}
public abstract class Overlay {
    protected Stage ui;
    protected GL20 gl;
    public boolean pausesGame = true;
    public boolean visible = false;
    public boolean running = false;
    public boolean showCursor = true;
    public boolean catchInput = true;

    private InputProcessor previousInputProcessor = null;

    private boolean cursorWasShownBefore = false;

    public Overlay() { }

    public void show() {
        show(true);
    }

    public void show(boolean setInputSettings) {
        visible = true;
        running = true;

        if(setInputSettings) {
            cursorWasShownBefore = !Gdx.input.isCursorCatched();

            if(catchInput) {
                previousInputProcessor = Gdx.input.getInputProcessor();
            }
        }

        onShow();
    }

    public void hide() {
        visible = false;
        running = false;

        if(showCursor != cursorWasShownBefore) {
            Gdx.input.setCursorCatched(!cursorWasShownBefore);
        }

        if(previousInputProcessor != null)
            Gdx.input.setInputProcessor(previousInputProcessor);

        onHide();
    }

    protected void draw(float delta) {
        gl = Gdx.gl;

        if(ui != null) {
            if(running) ui.act(delta);
            ui.draw();
        }
    }

    public abstract void tick(float delta);
    public abstract void onShow();
    public abstract void onHide();

    public void back() {
        remove();
    }

    public void remove() {
        OverlayManager.instance.remove(this);
    }

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
    }

    public void resize(int width, int height) {
        if(ui != null && ui.getViewport() != null) {
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
