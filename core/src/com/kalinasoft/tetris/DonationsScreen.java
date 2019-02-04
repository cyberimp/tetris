package com.kalinasoft.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

class DonationsScreen implements Screen, PurchaseObserver {

    private final GdxTetris game;
    private final Stage stage;
    private final OrthographicCamera camera;
    private boolean disabled = true;

    private IAPButton[] buttons;

    private static final String sku[] = {
            "white_donation",
            "green_donation",
            "blue_donation",
            "purple_donation",
            "orange_donation"
    };

    DonationsScreen(final GdxTetris game){
        this.game = game;
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 240, 360);

        PurchaseManagerConfig pmc = new PurchaseManagerConfig();
        for (String id : sku)
            pmc.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier(id));
        this.game.purchaseManager.install(this,pmc,true);

        InputMultiplexer multiplexer = new InputMultiplexer();

        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter(){

            @Override
            public boolean keyUp(int character) {
                if (character == Input.Keys.BACK ) {
                    game.setScreen(new MainMenuScreen(game));
                    dispose();
                    return true;
                }
                else
                    return false;
            }
        });

        Gdx.input.setInputProcessor(multiplexer);



        buttons = new IAPButton[5];
        buttons[0] = new IAPButton(game.bundle.get("donation_plain")+"\n"+
                game.bundle.get("donation_plain_value"), game,sku[0]);
        buttons[1] = new IAPButton(game.bundle.get("donation_magic")+"\n"+
                game.bundle.get("donation_magic_value"), game,"magic",sku[1]);
        buttons[2] = new IAPButton(game.bundle.get("donation_rare")+"\n"+
                game.bundle.get("donation_rare_value"), game,"rare",sku[2]);
        buttons[3] = new IAPButton(game.bundle.get("donation_epic")+"\n"+
                game.bundle.get("donation_epic_value"), game,"epic",sku[3]);
        buttons[4] = new IAPButton(game.bundle.get("donation_legendary")+"\n"+
                game.bundle.get("donation_legendary_value"), game,"legendary",sku[4]);
        TextButton back = new TextButton("<--- "+game.bundle.get("action_back"),game.skin);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        for (int i = 0; i < 5; i++) {
            buttons[i].setDisabled(true);
            table.add(buttons[i]).width(300).padBottom(30);
            table.row();
        }
        table.add(back).width(300).padBottom(30);
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    private void buttonsEnable(){
        for (int i = 0; i < 5; i++) {
            buttons[i].setDisabled(false);
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        if (disabled){
            if (game.purchaseManager.installed()){
                buttonsEnable();
                disabled = false;
            }
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

    @Override
    public void handleInstall() {
        Gdx.app.log("IAP","installed");

    }

    @Override
    public void handleInstallError(Throwable e) {

    }

    @Override
    public void handleRestore(Transaction[] transactions) {

    }

    @Override
    public void handleRestoreError(Throwable e) {

    }

    @Override
    public void handlePurchase(Transaction transaction) {
        Gdx.app.log("IAP",transaction.toString());
    }

    @Override
    public void handlePurchaseError(Throwable e) {

    }

    @Override
    public void handlePurchaseCanceled() {

    }
}
