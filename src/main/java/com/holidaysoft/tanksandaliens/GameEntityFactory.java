package com.holidaysoft.tanksandaliens;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class GameEntityFactory implements EntityFactory {

    @Spawns("tank")
    public Entity createTank() {
        return new EntityBuilder()
                .at(100,100)
                .viewWithBBox(new Rectangle(50,70, Color.BLUE))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    @Spawns("bullet")
    public Entity createBullet(double x, double y, double bulletSpeed, double dirX, double dirY) {
        PhysicsComponent physicsComponent = new PhysicsComponent();
        ProjectileComponent projectileComponent = new ProjectileComponent(new Point2D(dirX,dirY),bulletSpeed);

        return new EntityBuilder()
                .at(x,y)
                .viewWithBBox(new Circle(10,10,10, Color.YELLOW))
                .with(new CollidableComponent(true))
                .with(projectileComponent)
                .buildAndAttach();
    }

    @Spawns("alien")
    public Entity createAlien(double x, double y) {
        return new EntityBuilder()
                .at(x,y)
                .viewWithBBox(new Circle(50,50,50, Color.RED))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    @Spawns("bullet")
    public Entity createAlienBullet(double x, double y) {
        return new EntityBuilder()
                .at(x,y)
                .viewWithBBox(new Circle(10,10,10, Color.GREEN))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

}
