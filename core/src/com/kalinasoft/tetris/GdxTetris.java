package com.kalinasoft.tetris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GdxTetris extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Skin skin;

	public Texture brick;
	public Texture back;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("small.fnt"));
		skin = new Skin(Gdx.files.internal("skin.json"));
		back = new Texture("glass.png");
		brick = new Texture("brick.png");

		this.setScreen(new MainMenuScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
