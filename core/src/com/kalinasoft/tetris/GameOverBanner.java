package com.kalinasoft.tetris;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

class GameOverBanner {

    private TextureRegion up;
    private TextureRegion down;
    private float tearHeight = 1.0f;
    private final GdxTetris game;

    GameOverBanner(GdxTetris game){
        this.game = game;
        up = new TextureRegion(game.gameover,160,1);
        down = new TextureRegion(game.gameover,0,79,160,1);
    }

    private void setTear(){
        up = new TextureRegion(game.gameover,160, MathUtils.floor( tearHeight));
        down = new TextureRegion(game.gameover,0,80-MathUtils.floor( tearHeight),
                160, MathUtils.floor( tearHeight));
    }

    void update(float millis){
        if (tearHeight < 40.0f) {
            tearHeight += millis*8;
            setTear();
        }
    }

    void draw(Batch batch){
        batch.draw(down,2,150);
        batch.draw(up,2,230-tearHeight);
    }
}
