package com.kalinasoft.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.text.DecimalFormat;
import java.util.Arrays;

class Stakan {

    final private Color content[][];

    private final GdxTetris game;
    private long score = 0;

    private int[] dirty;
    private float rowWidth = 80f;
    private boolean falling = true;

    private Figure nextFigure;
    private Figure curFigure;
    private ShapeRenderer shape;
    private Color back = new Color(0.2f, 0.2f, 0.2f, 1);
    final private Color[] random = {Color.BLUE, Color.RED, Color.WHITE, Color.GREEN, Color.YELLOW};
    private final DecimalFormat df;


    Stakan(GdxTetris game){
        this.game = game;
        shape = new ShapeRenderer();
        dirty = new int[4];
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
        if (!falling) {
            batch.end();
            shape.setProjectionMatrix(batch.getProjectionMatrix());
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(back);
            for (int i = 0; (i < 4) && dirty[i] < 20; i++) {
                shape.rect(82 - rowWidth / 2, dirty[i] * 16 + 2, rowWidth, 16);
            }

            shape.end();

            batch.begin();
        }
        batch.setColor(Color.WHITE);
    }

    /***
     * update method of glass -- moving figures
     * @param delta milliseconds from last update
     * @return true if game over
     */
    boolean update(float delta) {
        if (falling) {
            if (canMoveDown(delta * 3))
                curFigure.moveDown(delta * 3);
            else {
                if (!freeze(curFigure)) {
                    game.adapter.add(score);
                    return true;
                }
                if (rowCheck() == 0) {
                    respawn();
                }
                else {
                    rowWidth = 0f;
                    falling = false;
                }
            }
        }
        else
        {
            rowWidth += delta *200f;
            if (rowWidth >160f){
                falling = true;
                restack();
                respawn();
            }
        }
        return false;
    }

    private void restack() {
        int i=0;
        while (i<4 && dirty[i]<20){
            for (int j = dirty[i]-i; j < 19; j++) {
                for (int k = 0; k < 10; k++) {
                    content[k][j] = content[k][j+1];
                }
            }
            for (int j = 0; j < 10; j++) {
                content[j][19] = Color.BLACK;
            }
            ++i;
            score+=i*100;
        }
    }


    private void respawn(){
        curFigure = nextFigure;
        curFigure.spawn();
        nextFigure = new Figure(game);
        nextFigure.color = random[MathUtils.random(4)];
        nextFigure.setCoords(10.5f, 18);
    }

    private int rowCheck() {
        int result = 0;
        for (int i = 0; i < 4; i++)
            dirty[i] = 20;
        int row = 0;
        for (int i = 0; i < 20; i++) {
            boolean full = true;
            for (int j = 0; j < 10; j++)
                if (content[j][i] == Color.BLACK)
                    full = false;
            if (full) {
                dirty[row] = i;
                result++;
                row++;
            }
        }
        return result;
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
        if (falling)
            if (figureCheck(curFigure.shape,curFigure.x-1.f,curFigure.y))
                curFigure.x--;
    }

    void tryRight() {
        if (falling)
            if (figureCheck(curFigure.shape, curFigure.x + 1.f, curFigure.y))
                curFigure.x++;
    }

    void tryRotate() {
        if (!falling)
            return;
        int[][] newShape = new int[4][4];
        //mirroring and transposing matrix is the way to rotate figure
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                newShape[3-i][j] = curFigure.shape[j][i];

        if (figureCheck(newShape,curFigure.x,curFigure.y))
            curFigure.shape = newShape;

    }

    void dispose(){
        shape.dispose();
    }
}
