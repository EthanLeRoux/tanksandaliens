package com.holidaysoft.tanksandaliens;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class GameEntityFactory implements EntityFactory {

    @Spawns("tank")
    public Entity createTank(){
        return FXGL.entityBuilder()
                .type(EntityTypes.PLAYER)
                .viewWithBBox(new Rectangle(70,70,Color.BLUE))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    @Spawns("tank-bullet")
    public Entity createBullet(){
        PhysicsComponent physicsComponent = new PhysicsComponent();
        double ySpeed = 0.1;
        double xSpeed = 0.1;

        physicsComponent.setLinearVelocity(0,ySpeed);

        return FXGL.entityBuilder()
                .viewWithBBox(new Rectangle(25,25, Color.YELLOW))
                .with(new CollidableComponent(true))
                .with(physicsComponent)
                .buildAndAttach();
    }

    @Spawns("alien")
    public Entity createAlien(double x, double y){
        return FXGL.entityBuilder()
                .at(x, y)
                .viewWithBBox(new Circle(25,25,50,Color.RED))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    @Spawns("enemy-bullet")
    public Entity createEnemyBullet(){
        return FXGL.entityBuilder()
                .viewWithBBox(new Rectangle(25,25, Color.GREEN))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }
}
