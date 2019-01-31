package com.kalinasoft.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.text.DecimalFormat;
import java.util.Arrays;

class Stakan {

    final private Color content[][];

    private final GdxTetris game;
    private long score = 0;

    private Figure nextFigure;
    private Figure curFigure;
    final private Color[] random = {Color.BLUE, Color.RED, Color.WHITE, Color.GREEN, Color.YELLOW};
    private final DecimalFormat df;

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
        nextFigure.color = random[MathUtils.random(4)];
        curFigure.spawn();

        df = new DecimalFormat();
        df.applyPattern("0000000000");
    }

    void draw(SpriteBatch batch){
        batch.setColor(Color.YELLOW);
        game.font.draw(batch,"NEXT",172,318);
        game.font.draw(batch,"SCORE: "+df.format(score),0,350);

        batch.draw(game.back,0,0);
        for (int x=0;x<10;x++)
            for (int y=0;y<20;y++)
                if(content[x][y] != Color.BLACK){
                    batch.setColor(content[x][y]);
                    batch.draw(game.brick,x*16+2,y*16+2);
                }
                nextFigure.draw(batch);
                curFigure.draw(batch);
        batch.setColor(Color.WHITE);
    }

    /***
     * update method of glass -- moving figures
     * @param delta milliseconds from last update
     * @return true if game over
     */
    boolean update(float delta) {
        if (canMoveDown(delta*3))
            curFigure.moveDown(delta*3 );
        else {
            if (!freeze(curFigure)) {
                game.adapter.add(score);
                return true;
            }
            curFigure = nextFigure;
            curFigure.spawn();
            nextFigure = new Figure(game);
            nextFigure.color = random[MathUtils.random(4)];
            nextFigure.setCoords(10.5f,18);
        }
        return false;
    }

    /**
     * freeze figure in glass
     * @param figure figure to be frozen
     * @return true if place for figure is empty
     */
    private boolean freeze(Figure figure) {
        final int[][] shape = figure.shape;
        int figX = MathUtils.floor(figure.x);
        int figY = MathUtils.floor(figure.y);
        boolean result = true;
        Gdx.app.log("figure","X:"+figX+",Y:"+figY);
        Gdx.app.log("figure",Arrays.deepToString(shape));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[j][i] == 1 && figY-j < 20 && figY-j >= 0) {
                    if (content[figX + i][figY - j]!=Color.BLACK)
                        result = false;
                    content[figX + i][figY - j] = figure.color;
                }
            }
        }
        score += 15;
        return result;
    }

    /**
     * Check collisions for every figure movement
     * @param figure array for new figure morph
     * @param x new x coordinate
     * @param y new y coordinate
     * @return true if figure will fit
     */
    private boolean figureCheck (int[][] figure, float x, float y){
        int intY = MathUtils.floor(y);
        int intX = MathUtils.floor(x);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (figure[j][i] == 1 &&( intX+i < 0|| intY-j<0 ||intY-j>19 ||intX+i>9))
                    return false;//oh whatever, we crossed THE LINE

                if (figure[j][i] == 1 && content[intX+i][intY-j] != Color.BLACK)
                    return false;
            }
        }
        return true;
    }

    /**
     * Check if we can move this figure down
     * @param delta movement step
     * @return true if figure can be moved
     */
    private boolean canMoveDown(float delta) {
        final int[][] shape = curFigure.shape;
        int y = MathUtils.floor(curFigure.y-delta);
        int x = MathUtils.floor(curFigure.x);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[j][i] == 1 &&( y-j < 0 || y-j>19))
                    return false;

                if (shape[j][i] == 1 && content[x+i][y-j] != Color.BLACK)
                    return false;
            }
        }
        return true;
    }

    void tryLeft() {
        if (figureCheck(curFigure.shape,curFigure.x-1.f,curFigure.y))
            curFigure.x--;
    }

    void tryRight() {
        if (figureCheck(curFigure.shape, curFigure.x + 1.f, curFigure.y))
            curFigure.x++;
    }

    void tryRotate() {
        int[][] newShape = new int[4][4];
        //mirroring and transposing matrix is the way to rotate figure
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                newShape[3-i][j] = curFigure.shape[j][i];

        if (figureCheck(newShape,curFigure.x,curFigure.y))
            curFigure.shape = newShape;

    }
}
