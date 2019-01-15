package com.kalinasoft.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Figure {

    private final GdxTetris game;

    private static int[][][] proto = {
            {
                    {0,0,0,0},
                    {0,1,1,0},
                    {0,1,1,0},
                    {0,0,0,0}
            },
            {
                    {0,1,0,0},
                    {0,1,0,0},
                    {0,1,0,0},
                    {0,1,0,0}
            },
            {
                    {0,1,0,0},
                    {0,1,0,0},
                    {0,1,1,0},
                    {0,0,0,0}
            },
            {
                    {0,0,1,0},
                    {0,0,1,0},
                    {0,1,1,0},
                    {0,0,0,0}
            },
            {
                    {0,0,0,0},
                    {0,0,1,1},
                    {0,1,1,0},
                    {0,0,0,0}
            },
            {
                    {0,0,0,0},
                    {0,1,1,0},
                    {0,0,1,1},
                    {0,0,0,0}
            },
            {
                    {0,0,0,0},
                    {0,0,1,0},
                    {0,1,1,1},
                    {0,0,0,0}
            },
            {
                    {1,1,1,1},
                    {1,1,1,1},
                    {1,1,1,1},
                    {1,1,1,1}
            }
    };

    int[][] shape;

    Color color;

    float x,y;

    Figure(GdxTetris game){
        this.game = game;
        shape = proto[MathUtils.random(6)];
//        shape = proto[7];
        color = Color.BLUE;
    }

    public void update(float milis,Stakan parent){

    }

    @Override
    public Figure clone() throws CloneNotSupportedException {
        super.clone();
        Figure result = new Figure(game);
        result.shape = this.shape;
        return result;
    }

    void draw(SpriteBatch batch) {
        batch.setColor(color);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[i][j]>0)
                    batch.draw(game.brick,x*16+2+j*16,y*16+2-i*16);
            }
        }
    }

    void setCoords(float x, float y) {
        this.x = x;
        this.y = y;
    }

    void moveDown(float delta) {
        this.y-=delta;
    }
}
