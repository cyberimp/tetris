package com.kalinasoft.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


class MainMenuScreen implements Screen {

    private final GdxTetris game;
    private final Stage stage;
    private final OrthographicCamera camera;
    private final TextButton hiScore;


    MainMenuScreen(final GdxTetris game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 240, 360);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        Label title = new Label("BRICKS",game.skin);
        TextButton newGame = new TextButton("New game", game.skin);
        newGame.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    Gdx.app.log("btn", "pressed");
                                    game.setScreen(new GameScreen(game));
                                    dispose();
                                }
                            });
        hiScore = new TextButton("High score", game.skin);
        hiScore.setDisabled(true);
        hiScore.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.adapter.show();
            }
        });

        TextButton donate = new TextButton("Donations", game.skin, "epic");
        donate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.setScreen(new DonationsScreen(game));
                dispose();
            }
        });

        table.add(title).padBottom(100);
        table.row();
        table.add(newGame).width(300).padBottom(30);
        table.row();
        table.add(hiScore).width(300).padBottom(30);
        table.row();
        table.add(donate).width(300);
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

        if (hiScore.isDisabled()) {
            if (game.adapter.checkReady())
                hiScore.setDisabled(false);
        }
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
