package com.yellowbytestudios.spacedoctor.game.player;

import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;

/**
 * Created by Robert on 03/28/16.
 */
public class Gun {

    public static int BOW = 6;
    public static int SHOTGUN = 5;
    public static int SNIPER = 4;
    public static int PISTOL = 3;
    public static int BLASTER = 2;
    public static int DRILL_CANNON = 1;

    private String shootSound;
    private int ammo = 10;
    private int id;
    private String name;

    //Buillet properties.
    private int bulletId = 1;

    public Gun(int id) {
        this.id = id;

        if (id == BLASTER) {
            shootSound = Assets.GUN_SOUND_1;
            bulletId = 1;
        } else {
            shootSound = Assets.GUN_SOUND_3;
            bulletId = 0;
        }
    }

    public boolean shoot() {
        if (ammo > 0 || MainGame.UNLIM_AMMO) {
            SoundManager.play(shootSound);
            ammo -= 1;
            return true;
        } else {
            SoundManager.play(Assets.GUN_SOUND_EMPTY);
            return false;
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

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }
}
