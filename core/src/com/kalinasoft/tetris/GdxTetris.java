package com.kalinasoft.tetris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GdxTetris extends Game {
	SpriteBatch batch;
	BitmapFont font;
	Skin skin;

	Texture brick;
	Texture back;
	Texture gameover;

	HighScoreAdapter adapter;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("small.fnt"));
		skin = new Skin(Gdx.files.internal("skin.json"));
		back = new Texture("glass.png");
		brick = new Texture("brick.png");
		gameover = new Texture("gameover.png");
		Gdx.input.setCatchBackKey(true);
		adapter.signIn();
		this.setScreen(new MainMenuScreen(this));

	}

	void setAdapter(HighScoreAdapter context){
		this.adapter = context;
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
