package com.uw.service.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class WindowOverlay extends Overlay {
    public class GamepadEvent extends InputEvent {}

	public float timer = 0;
	public Table stageTable;
	public Float uiScale = null;
	SpriteBatch uiBatch = new SpriteBatch();
	Skin skin;
	Interpolation lerp = Interpolation.sine;

	public boolean showBackground = true;
	public boolean dimScreen = true;

	protected float lerpValue = 0;
	protected Integer gamepadSelectionIndex = null;
	protected Integer lastGamepadSelectionIndex = null;

	protected Array<Actor> buttonOrder = new Array<Actor>();
	protected ArrayMap<Actor, Label> buttonLabels = new ArrayMap<Actor, Label>();

	public boolean canCancelOverlay = true;
	public boolean animateBackground = true;
	public boolean animate = true;
	protected int align = Align.center;

	protected NinePatchDrawable background;

	Vector2 offset = new Vector2();

	float keyRepeat = 0f;

	public boolean playSoundOnOpen = true;

	public WindowOverlay() {
		getSkin();
		background = new NinePatchDrawable(new NinePatch(skin.getRegion("window"), 11, 11, 11, 11));
	}

	private void getSkin() {
        if (skin != null) return;
        var at = new TextureAtlas(Gdx.files.internal("ui/skin.atlas"));

        Texture windowTexture = new Texture(Gdx.files.internal("ui/window.png"));
        TextureAtlas.AtlasRegion windowRegion1 = at.addRegion("window", windowTexture, 0, 0, windowTexture.getWidth(), windowTexture.getHeight());

        windowRegion1.names = new String[]{"split", "pad"};
        windowRegion1.values = new int[][]{{8, 8, 8, 8}, {0, 0, 0, 0}};

        var skin = new Skin(
                Gdx.files.internal("ui/skin.json"), at
        );

        var font = new BitmapFont(Gdx.files.internal("ui/pixel.fnt"), Gdx.files.internal("ui/pixel.png"), false, true);
        font.getData().markupEnabled = true;
        font.getData().padBottom = 0f;
        font.getData().padTop = 0f;
        font.getData().padLeft = 0f;
        font.getData().padRight = 0f;
        skin.add("default-font", font);


        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
        labelStyle.font = font;
        skin.add("default", labelStyle);


        this.skin = skin;
    }

	@Override
	public void tick(float delta) {
		if(running) {
			timer += delta;
		}
	}

	@Override
	public void onShow() {
		getSkin();

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        int scaleFactor = width < height * 1.5 ? width : height;
        float scaleMod = width < height * 1.5 ? 480f * 1.5f : 480f;
        uiScale = (scaleFactor / scaleMod) * 3f;

		Gdx.app.log("Delver", Gdx.graphics.getWidth() + " x " + Gdx.graphics.getHeight());

		//FillViewport viewport = new FillViewport(Gdx.graphics.getWidth() / uiScale, Gdx.graphics.getHeight() / uiScale);
		ScalingViewport viewport = new ScalingViewport(Scaling.fill, 850f, 480f);
		ui = new Stage(viewport);

		resize(width, height);

		makeLayout();

		if(catchInput)
			Gdx.input.setInputProcessor(ui);

	}

	protected void makeLayout() {
		if(stageTable == null) {
			stageTable = new Table();
			ui.addActor(stageTable);
		}
		else {
			stageTable.clear();
		}

		stageTable.setFillParent(true);
		stageTable.align(align);

		Table mainTable = new Table();
		if(background != null && showBackground) mainTable.setBackground(background);
		stageTable.add(mainTable).fill();

		Table contentTable = makeContent();
	    mainTable.add(contentTable).pad(4f);
	}

	protected void makeLayout(Table contentTable) {
		if(stageTable == null) {
			stageTable = new Table();
			ui.addActor(stageTable);
		}
		else {
			stageTable.clear();
		}

		stageTable.setFillParent(true);
		stageTable.align(align);

		Table mainTable = new Table();
		if(background != null && showBackground) mainTable.setBackground(background);
		stageTable.add(mainTable).fill();

	    mainTable.add(contentTable).pad(4f);

	    ui.addActor(stageTable);
	}

	@Override
	protected void draw(float delta) {

		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		int scaleFactor = width < height * 1.5 ? width : height;
		float scaleMod = width < height * 1.5 ? 480f * 1.5f : 480f;
		uiScale = (scaleFactor / scaleMod) * 3f;

		gl = Gdx.gl;


		if(catchInput && dimScreen) {
			uiBatch.begin();
			uiBatch.enableBlending();

			if(animateBackground)
				uiBatch.setColor(0, 0, 0, 0.5f * lerpValue);
			else
				uiBatch.setColor(0, 0, 0, 0.5f);

			        Pixmap flashPixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        flashPixmap.setColor(Color.WHITE);
        flashPixmap.fill();
        var flashTex = new Texture(flashPixmap);
        var flashRegion = new TextureRegion(flashTex, 0, 0, 2, 2);

			uiBatch.draw(flashRegion,
					-100,
					-100,
					100,
					100
			);			uiBatch.end();
			uiBatch.disableBlending();
		}

		stageTable.setPosition(0 + offset.x, offset.y);

		super.draw(delta);
	}

	@Override
	public void onHide() {

	}

	// Override this to make table content
	public Table makeContent() {
		Table contentTable = new Table(skin);
        contentTable.add(new Label("!@#$%^&*(", skin.get(Label.LabelStyle.class)));
        return contentTable;
	}
}
