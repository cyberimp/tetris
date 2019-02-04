package com.kalinasoft.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class GameScreen implements Screen{
    private final GdxTetris game;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final Stakan stakan;
    private final GameOverBanner banner;

    private final Music bgm;
    private boolean isGameOver =false;

    private final InputHandler inputHandler;
    private float backTimer = 0f;


    GameScreen(final GdxTetris game){
        this.game = game;
        stakan = new Stakan(game);
        banner = new GameOverBanner(game);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 240, 360);
        viewport = new FitViewport(240, 360, camera);
        bgm = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        bgm.setLooping(true);
        inputHandler = new InputHandler();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputHandler);
        bgm.play();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        InputHandler.INPUT input = inputHandler.consume();
        while(input!=null){
            switch (input){
                case LEFT:
                    stakan.tryLeft();
                    break;
                case RIGHT:
                    stakan.tryRight();
                    break;
                case ROTATE:
                    stakan.tryRotate();
                    break;
                case DOWN:
                    stakan.setTurbo();
                    break;
                case BACK:
                    if(isGameOver||backTimer>0f){
                        game.setScreen(new MainMenuScreen(game));
                        dispose();
                    }
                    else {
                        backTimer = 2f;
                        game.adapter.toast();
                    }
            }
            input = inputHandler.consume();
        }
        if (!isGameOver) {
            isGameOver = stakan.update(delta);
            if (backTimer > 0f)
                backTimer-=delta;
        }
        else
            banner.update(delta);

        game.batch.begin();
        stakan.draw(game.batch);

        if (isGameOver)
            banner.draw(game.batch);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        bgm.pause();
    }

    @Override
    public void dispose() {
        bgm.dispose();
        stakan.dispose();
    }

}
