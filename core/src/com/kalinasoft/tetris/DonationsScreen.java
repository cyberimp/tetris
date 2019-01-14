package com.kalinasoft.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class DonationsScreen implements Screen {

    final GdxTetris game;
    private final Stage stage;
    private final Table table;
    private OrthographicCamera camera;

    DonationsScreen(final GdxTetris game){
        this.game = game;
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 240, 360);

        Gdx.input.setInputProcessor(stage);
        TextButton normal = new TextButton("Plain donation\n15 RUB", game.skin);
        TextButton magic = new TextButton("Magic donation\n50 RUB", game.skin,"magic");
        TextButton rare = new TextButton("Rare donation\n100 RUB", game.skin,"rare");
        TextButton epic = new TextButton("Epic donation\n300 RUB", game.skin,"epic");
        TextButton legendary = new TextButton("Legendary donation\n1000 RUB", game.skin,"legendary");
        TextButton back = new TextButton("<--- BACK",game.skin);
        table = new Table();
        table.setFillParent(true);
        table.add(normal).width(300).padBottom(30);
        table.row();
        table.add(magic).width(300).padBottom(30);
        table.row();
        table.add(rare).width(300).padBottom(30);
        table.row();
        table.add(epic).width(300).padBottom(30);
        table.row();
        table.add(legendary).width(300).padBottom(30);
        table.row();
        table.add(back).width(300).padBottom(30);
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();


        stage.act(delta);
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
