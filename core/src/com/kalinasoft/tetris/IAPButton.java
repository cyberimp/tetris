package com.kalinasoft.tetris;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

class IAPButton extends TextButton {
    private final GdxTetris game;
    private final String id;

    IAPButton(String text, GdxTetris game, String id) {
        super(text, game.skin);
        this.game = game;
        this.id = id;
        handlePush();
    }

    IAPButton(String text, GdxTetris game, String styleName, String id) {
        super(text, game.skin, styleName);
        this.game = game;
        this.id = id;
        handlePush();
    }

    private void handlePush() {
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.purchaseManager.purchase(id);
            }
        });
    }
}
