package com.holidaysoft.tanksandaliens;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.util.Map;
import java.util.Random;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class TankApplication extends GameApplication {
    private Entity player;
    GameEntityFactory gef;
    Entity boss;
    private boolean isBossDead = false;

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
                play("player-shoot.wav");
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
        vars.put("hp",5);
        vars.put("score",0);
        vars.put("boss-hp",30);
    }

    @Override
    protected void initGame() {
        gef = new GameEntityFactory();
        player = gef.createTank();

        getGameScene().setBackgroundRepeat("bg.png");
        play("Orbital Colossus.mp3");
        if(getWorldProperties().intProperty("score").intValue()<=10){
            getGameTimer().runAtInterval(() -> {
                Random random = new Random();
                double x =random.nextDouble(500);
                double y = 10;
                Entity alien = gef.createAlien(x,y,100,0,5);
            },Duration.seconds(1));
        }

        getWorldProperties().intProperty("score").addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newNumber) {
                if(getWorldProperties().intProperty("score").intValue()==10){
                    boss = gef.createBoss();

                        getGameTimer().runAtInterval(()->{
                            if(isBossDead == false) {
                                Entity alienBullet = gef.createAlienBullet(boss.getX() + 5, boss.getY() + 5, 100, 0, 5);
                            }
                        },Duration.seconds(1));

                    getGameTimer().runAtInterval(() -> {
                        if (isBossDead == false) {
                            double newX = boss.getX() + 10;
                            if (newX > getAppWidth()) {
                                newX = 0;
                            }
                            boss.setX(newX);

                            double newY = boss.getY() + (Math.random()*10);
                            if(newY > getAppHeight()) {
                                newY = 0;
                            }
                            boss.setY(newY);
                        }
                    }, Duration.seconds(0.2));
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
                play("explosion.wav");
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
                    play("explosion.wav");
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
                play("alien-dmg.wav");
                bullet.removeFromWorld();
                inc("boss-hp",-1);

                if(getWorldProperties().intProperty("boss-hp").intValue() <= 0) {
                    play("explosion.wav");
                    boss.removeFromWorld();
                    isBossDead = true;
                    getDialogService().showMessageBox("YOU BEAT THE BOSS!", new Runnable() {
                        @Override
                        public void run() {
                            getGameController().gotoGameMenu();
                        }
                    });
                }

                int bossHp = getWorldProperties().intProperty("boss-hp").intValue();
                System.out.println("Boss got hit! Current hp = " + bossHp);
            }
        });
        physicsWorld.addCollisionHandler(new CollisionHandler(GameEntityTypes.ALIEN_BULLET, GameEntityTypes.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity alienBullet, Entity player) {
                alienBullet.removeFromWorld();
                inc("hp",-1);

                if(getWorldProperties().intProperty("hp").intValue()==0){
                    play("explosion.wav");
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
        physicsWorld.addCollisionHandler(new CollisionHandler(GameEntityTypes.PLAYER, GameEntityTypes.BOSS) {
            @Override
            protected void onCollisionBegin(Entity player, Entity boss) {
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
    }

    @Override
    protected void initUI(){
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

        playerHPText.textProperty().bind(getWorldProperties().intProperty("hp").asString());
        scoreText.textProperty().bind(getWorldProperties().intProperty("score").asString());

        getGameScene().addUINode(playerHPLabel);
        getGameScene().addUINode(playerHPText);
        getGameScene().addUINodes(scoreLabel,scoreText);
    }

    public static void main(String[] args) {
        launch(args);
    }
}