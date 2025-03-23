package com.holidaysoft.tanksandaliens;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.Map;
import java.io.IOException;
import java.util.Random;

import com.holidaysoft.tanksandaliens.GameEntityFactory;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class TankApplication extends GameApplication {
    private Entity player;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(600);
        gameSettings.setHeight(600);
        gameSettings.setTitle("Tanks vs Aliens!");
        gameSettings.setVersion("0.1");
    }

    @Override
    protected void initInput() {
        double moveYSpeed = 3;
        double moveXSpeed = 3;
        getInput().addAction(new UserAction("move-player-up") {
            @Override
            protected void onAction() {
                player.translate(0,-moveYSpeed);
            }
        },KeyCode.UP);
        getInput().addAction(new UserAction("move-player-down") {
            @Override
            protected void onAction() {
                player.translate(0,moveYSpeed);
            }
        },KeyCode.DOWN);
        getInput().addAction(new UserAction("move-player-left") {
            @Override
            protected void onAction() {
                player.translate(-moveXSpeed,0);
            }
        },KeyCode.LEFT);
        getInput().addAction(new UserAction("move-player-right") {
            @Override
            protected void onAction() {
                player.translate(moveXSpeed,0);
            }
        },KeyCode.RIGHT);
    }
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("alien-count",0);
    }

    @Override
    protected void initGame() {
        GameEntityFactory gef = new GameEntityFactory();

        player = gef.createTank();

        getGameTimer().runAtInterval(() -> {
            Random random = new Random();
            double x =random.nextDouble(500);
            double y = random.nextDouble(500);

            Entity alien = gef.createAlien(x,y);
            inc("alien-count",+1);
            System.out.println("New alien spawned!");
        },Duration.seconds(1));
    }

    @Override
    protected void initUI(){
        Text alienCounterText = new Text();
        alienCounterText.setTranslateX(50);
        alienCounterText.setTranslateY(100);
        alienCounterText.textProperty().bind(getWorldProperties().intProperty("alien-count").asString());
        getGameScene().addUINode(alienCounterText);
    }

    public static void main(String[] args) {
        launch(args);
    }
}