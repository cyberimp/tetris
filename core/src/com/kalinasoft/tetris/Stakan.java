package com.kalinasoft.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.Arrays;

class Stakan {

    private Color content[][];

    private final GdxTetris game;

    private Figure nextFigure;
    private Figure curFigure;

    Stakan(GdxTetris game){
        this.game = game;
        content = new Color[10][20];
        Color[] random = {Color.BLACK, Color.BLUE, Color.RED, Color.WHITE, Color.GREEN, Color.YELLOW};
        for (int x = 0; x<10; x++)
            for (int y=0;y<20;y++)
                content[x][y] = Color.BLACK;
//                content[x][y] = random[MathUtils.random(5)];

        nextFigure = new Figure(game);
        nextFigure.setCoords(10.5f,18);
        curFigure = new Figure(game);
        curFigure.setCoords(3,20);

    }

    void Draw(SpriteBatch batch){
        batch.setColor(Color.YELLOW);
        game.font.draw(batch,"NEXT",172,318);
        game.font.draw(batch,"SCORE: 1'000'000'000",0,350);

        batch.draw(game.back,0,0);
        for (int x=0;x<10;x++)
            for (int y=0;y<20;y++)
                if(content[x][y] != Color.BLACK){
                    batch.setColor(content[x][y]);
                    batch.draw(game.brick,x*16+2,y*16+2);
                }
                nextFigure.Draw(batch);
                curFigure.Draw(batch);
    }

    public void update(float delta) {
        if (canMoveDown(curFigure,delta))
            curFigure.moveDown(delta*3 );
        else {
            freeze(curFigure);
            curFigure = nextFigure;
            curFigure.setCoords(3,20);
            nextFigure = new Figure(game);
            nextFigure.setCoords(10.5f,18);
        }
    }

    private void freeze(Figure figure) {
        final int[][] shape = figure.shape;
        int figX = MathUtils.floor(figure.x);
        int figY = MathUtils.floor(figure.y);
        Gdx.app.log("figure","X:"+figX+",Y:"+figY);
        Gdx.app.log("figure",Arrays.deepToString(shape));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[j][i] == 1)
                    content[figX+i][figY-j] = figure.color;
            }
        }
    }

    private boolean canMoveDown(Figure figure, float delta) {
        final int[][] shape = figure.shape;
        int y = MathUtils.floor(figure.y-delta);
        int x = MathUtils.floor(figure.x);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[j][i] == 1 && y-j < 0)
                    return false;
                if (shape[j][i] == 1 && content[x+i][y-j] != Color.BLACK)
                    return false;
            }
        }
        return true;
    }
}
