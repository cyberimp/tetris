package com.kalinasoft.tetris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class GdxTetris extends Game {
    PurchaseManager purchaseManager;
    SpriteBatch batch;
    BitmapFont font;
    Skin skin;

    Texture brick;
    Texture back;
    Texture gameover;

    I18NBundle bundle;

    HighScoreAdapter adapter;

    @Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("small.fnt"));
        skin = new Skin(Gdx.files.internal("skin.json"));
        back = new Texture("glass.png");
        brick = new Texture("brick.png");
        Locale locale = adapter.getLocale();
        FileHandle baseFileHandle = Gdx.files.internal("translations");
        bundle = I18NBundle.createBundle(baseFileHandle,locale);
        gameover = new Texture(bundle.get("resource_game_over"));
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
