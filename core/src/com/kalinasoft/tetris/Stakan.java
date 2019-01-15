package com.kalinasoft.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.Arrays;

class Stakan {

    private Color content[][];

    private final GdxTetris game;

    private Figure nextFigure;
    private Figure curFigure;
    private Color[] random = {Color.BLUE, Color.RED, Color.WHITE, Color.GREEN, Color.YELLOW};

    Stakan(GdxTetris game){
        this.game = game;
        content = new Color[10][20];
        for (int x = 0; x<10; x++)
            for (int y=0;y<20;y++)
                content[x][y] = Color.BLACK;
//                content[x][y] = random[MathUtils.random(5)];

        nextFigure = new Figure(game);
        nextFigure.setCoords(10.5f,18);
        nextFigure.color = random[MathUtils.random(4)];
        curFigure = new Figure(game);
        spawn(curFigure);

    }

    private void spawn(Figure figure){
        boolean firstEmpty = true;
        for (int i = 0; i < 4; i++)
            if (figure.shape[i][0]==1)
                firstEmpty = false;

        if (firstEmpty)
            figure.setCoords(3,20);
        else
            figure.setCoords(3,20);

    }

    void draw(SpriteBatch batch){
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
                nextFigure.draw(batch);
                curFigure.draw(batch);
    }

    void update(float delta) {
        if (canMoveDown(curFigure,delta*3))
            curFigure.moveDown(delta*3 );
        else {
            freeze(curFigure);
            curFigure = nextFigure;
            spawn(curFigure);
            nextFigure = new Figure(game);
            nextFigure.color = random[MathUtils.random(4)];
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
                if (shape[j][i] == 1 && figY-j < 20 && figY-j >= 0)
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
                if (shape[j][i] == 1 && y-j < 0 && y-j>19)
                    return false;
                if (shape[j][i] == 1 && content[x+i][y-j] != Color.BLACK)
                    return false;
            }
        }
        return true;
    }
}
