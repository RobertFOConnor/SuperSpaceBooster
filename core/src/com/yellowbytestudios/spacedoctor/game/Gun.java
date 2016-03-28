package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.graphics.Texture;
import com.yellowbytestudios.spacedoctor.media.Assets;

/**
 * Created by Robert on 03/28/16.
 */
public class Gun {

    public static int BLASTER = 0;
    public static int DRILL_CANNON = 1;

    private String shootSound;
    private int startAmmo = 10;
    private int id;
    private String name;
    private int bulletId = 1;

    public Gun(int id) {
        this.id = id;

        if(id == BLASTER) {
            shootSound = Assets.GUN_SOUND_1;
            bulletId = 1;
        } else {
            shootSound = Assets.GUN_SOUND_1;
            bulletId = 0;
        }
    }

    public int getId() {
        return id;
    }

    public int getBullet() {
        return bulletId;
    }

    public String getShootSound() {
        return shootSound;
    }
}
