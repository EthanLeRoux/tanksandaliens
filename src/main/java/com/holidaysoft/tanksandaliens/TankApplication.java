package com.holidaysoft.tanksandaliens;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.util.Map;
import java.io.IOException;
import java.util.Random;

import com.holidaysoft.tanksandaliens.GameEntityFactory;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class TankApplication extends GameApplication {
    private Entity player;
    GameEntityFactory gef;
    Entity boss;
    protected  void initMainMenu(Pane mainMenuRoot){

    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(600);
        gameSettings.setHeight(600);
        gameSettings.setTitle("Tanks vs Aliens!");
        gameSettings.setVersion("0.1");
        gameSettings.setMainMenuEnabled(true);
    }

    @Override
    protected void initInput() {
        double moveYSpeed = 3;
        double moveXSpeed = 3;
        GameEntityFactory gef = new GameEntityFactory();

        getInput().addAction(new UserAction("player-move-up") {
            @Override
            protected void onAction() {
                player.translate(0,-moveYSpeed);
            }
        },KeyCode.UP);
        getInput().addAction(new UserAction("player-move-down") {
            @Override
            protected void onAction() {
                player.translate(0,moveYSpeed);
            }
        },KeyCode.DOWN);
        getInput().addAction(new UserAction("player-move-left") {
            @Override
            protected void onAction() {
                player.translate(-moveXSpeed,0);
            }
        },KeyCode.LEFT);
        getInput().addAction(new UserAction("player-move-right") {
            @Override
            protected void onAction() {
                player.translate(moveXSpeed,0);
            }
        },KeyCode.RIGHT);

        getInput().addAction(new UserAction("player-shoot") {
            @Override
            protected void onActionBegin() {
                Entity bullet = gef.createBullet(player.getX()+5,player.getY()+5, 300,0,-5);
                if(!bullet.isVisible()){
                    bullet.removeFromWorld();
                }
            }
        },KeyCode.SPACE);
    }
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("alien-count",0);
        vars.put("hp",3);
        vars.put("score",0);
        vars.put("boss-hp",10);
    }

    @Override
    protected void initGame() {
        gef = new GameEntityFactory();

        player = gef.createTank();

        if(getWorldProperties().intProperty("score").intValue()<=10){
            getGameTimer().runAtInterval(() -> {
                Random random = new Random();
                double x =random.nextDouble(500);
                double y = random.nextDouble(500);

                Entity alien = gef.createAlien(x,y);
                inc("alien-count",+1);
            },Duration.seconds(1));
        }

        getWorldProperties().intProperty("score").addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newNumber) {
                if(getWorldProperties().intProperty("score").intValue()==10){
                    Entity boss = gef.createBoss();
                }
            }
        });

    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physicsWorld = getPhysicsWorld();
        physicsWorld.addCollisionHandler(new CollisionHandler(GameEntityTypes.BULLET, GameEntityTypes.ALIEN) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity alien) {
                alien.removeFromWorld();
                bullet.removeFromWorld();
                inc("score",+1);
            }
        });
        physicsWorld.addCollisionHandler(new CollisionHandler(GameEntityTypes.PLAYER, GameEntityTypes.ALIEN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity alien) {
                alien.removeFromWorld();
                inc("hp",-1);

                if(getWorldProperties().intProperty("hp").intValue()==0){
                    player.removeFromWorld();
                    getDialogService().showMessageBox("YOU DIED", new Runnable() {
                        @Override
                        public void run() {
                            getGameController().gotoGameMenu();
                        }
                    });
                }
            }
        });
        physicsWorld.addCollisionHandler(new CollisionHandler(GameEntityTypes.BULLET, GameEntityTypes.BOSS) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity boss) {
                bullet.removeFromWorld();
                inc("boss-hp",-1);

                if(getWorldProperties().intProperty("boss-hp").intValue() <= 0) {
                    boss.removeFromWorld();
                    //boss = null;
                }

                int bossHp = getWorldProperties().intProperty("boss-hp").intValue();
                System.out.println("Boss got hit! Current hp = " + bossHp);


            }
        });
    }

    @Override
    protected void initUI(){
        Text alienCounterText = new Text();
        Text alienCounterLabel = new Text();
        Text playerHPText = new Text();
        Text playerHPLabel = new Text();

        Text scoreText = new Text();
        Text scoreLabel = new Text();

        scoreLabel.setText("Score ");
        scoreLabel.setTranslateX(50);
        scoreLabel.setTranslateY(300);

        scoreText.setTranslateX(100);
        scoreText.setTranslateY(300);

        playerHPText.setTranslateX(50);
        playerHPText.setTranslateY(200);

        playerHPLabel.setText("HP ");
        playerHPLabel.setTranslateX(10);
        playerHPLabel.setTranslateY(200);

        alienCounterLabel.setText("Aliens on screen: ");
        alienCounterLabel.setTranslateX(30);
        alienCounterLabel.setTranslateY(100);

        alienCounterText.setTranslateX(150);
        alienCounterText.setTranslateY(100);

        alienCounterText.textProperty().bind(getWorldProperties().intProperty("alien-count").asString());
        playerHPText.textProperty().bind(getWorldProperties().intProperty("hp").asString());
        scoreText.textProperty().bind(getWorldProperties().intProperty("score").asString());
        getGameScene().addUINode(alienCounterText);
        getGameScene().addUINode(alienCounterLabel);
        getGameScene().addUINode(playerHPLabel);
        getGameScene().addUINode(playerHPText);
        getGameScene().addUINodes(scoreLabel,scoreText);
    }

//    @Override
//    protected void onUpdate(double tpf) {
//        gef = new GameEntityFactory();
//
//
//    }

    public static void main(String[] args) {
        launch(args);
    }
}