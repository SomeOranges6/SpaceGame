package main;

import entities.ProjectileBase;

public class ProjectileRegistry {

    public static ProjectileBase getPlayerProjectile(int x, int y, int width, int height, SpaceGame game) {
        return new ProjectileBase(x, y, width, height, game){

            public static int getDamage() {
                return 1;
            }

        };
    }
}

