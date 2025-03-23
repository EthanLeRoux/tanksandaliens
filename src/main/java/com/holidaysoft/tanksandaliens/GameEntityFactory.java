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
                .type(GameEntityTypes.PLAYER)
                .at(100,100)
                .viewWithBBox(new Rectangle(20,20, Color.BLUE))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    @Spawns("bullet")
    public Entity createBullet(double x, double y, double bulletSpeed, double dirX, double dirY) {
        ProjectileComponent projectileComponent = new ProjectileComponent(new Point2D(dirX,dirY),bulletSpeed);

        return new EntityBuilder()
                .type(GameEntityTypes.BULLET)
                .at(x,y)
                .viewWithBBox(new Circle(5,5,5, Color.YELLOW))
                .with(new CollidableComponent(true))
                .with(projectileComponent)
                .buildAndAttach();
    }

    @Spawns("alien")
    public Entity createAlien(double x, double y,double alienSpeed, double dirX, double dirY) {
        ProjectileComponent projectileComponent = new ProjectileComponent(new Point2D(dirX,dirY),alienSpeed);

        return new EntityBuilder()
                .type(GameEntityTypes.ALIEN)
                .at(x,y)
                .viewWithBBox(new Circle(16,16,16, Color.RED))
                .with(new CollidableComponent(true))
                .with(projectileComponent)
                .buildAndAttach();
    }

    @Spawns("alien-bullet")
    public Entity createAlienBullet(double x, double y) {
        return new EntityBuilder()
                .type(GameEntityTypes.ALIEN_BULLET)
                .at(x,y)
                .viewWithBBox(new Circle(10,10,10, Color.GREEN))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    @Spawns("boss")
    public Entity createBoss() {
        return new EntityBuilder()
                .type(GameEntityTypes.BOSS)
                .at(200,200)
                .viewWithBBox(new Rectangle(100,100, Color.PINK))
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

}
