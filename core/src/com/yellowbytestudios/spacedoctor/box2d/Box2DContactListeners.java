package com.yellowbytestudios.spacedoctor.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.game.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;

public class Box2DContactListeners implements ContactListener {

    private Array<Fixture> bodiesToRemove;
    private Body enemy;
    private int numFootContacts;

    private boolean atDoor = false;
    private Body door;

    private SpacemanPlayer player;

    public Box2DContactListeners() {
        super();
        bodiesToRemove = new Array<Fixture>();
    }

    public void beginContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();


        if (fa == null || fb == null) return;

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts++;
            playFootstep();
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts++;
            playFootstep();
        }

        if (fa.getUserData() != null && fa.getUserData().equals("player")) {
            if (fb.getFilterData().categoryBits == Box2DVars.BIT_SPIKE) {
                killPlayer();
            }
        }
        if (fb.getUserData() != null && fb.getUserData().equals("player")) {
            if (fa.getFilterData().categoryBits == Box2DVars.BIT_SPIKE) {
                killPlayer();
            }
        }

        if (fa.getUserData() != null && fa.getUserData().equals("bullet")) {
            shootEnemy(fb, fa);
        }
        if (fb.getUserData() != null && fb.getUserData().equals("bullet")) {
            shootEnemy(fa, fb);
        }

        if (fa.getUserData() != null && fa.getUserData().equals("door")) {
            door = fa.getBody();
            atDoor = true;
        }
        if (fb.getUserData() != null && fb.getUserData().equals("door")) {
            door = fb.getBody();
            atDoor = true;
        }

        if (fa.getUserData() != null && fa.getUserData().equals("pickup")) {
            bodiesToRemove.add(fa);
        }
        if (fb.getUserData() != null && fb.getUserData().equals("pickup")) {
            bodiesToRemove.add(fb);
        }

        if (fa.getUserData() != null && fa.getUserData().equals("enemy")) {

            if (fb.getUserData().equals("player")) {
                killPlayer();
            }
        }
        if (fb.getUserData() != null && fb.getUserData().equals("enemy")) {
            if (fa.getUserData().equals("player")) {
                killPlayer();
            }
        }
    }


    public void endContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }

        if (fa.getUserData() != null && fa.getUserData().equals("bullet")) {
            bodiesToRemove.removeValue(fa, true);
        }
        if (fb.getUserData() != null && fb.getUserData().equals("bullet")) {
            bodiesToRemove.removeValue(fb, true);
        }

        if (fa.getUserData() != null && fa.getUserData().equals("pickup")) {
            bodiesToRemove.removeValue(fa, true);
        }
        if (fb.getUserData() != null && fb.getUserData().equals("pickup")) {
            bodiesToRemove.removeValue(fb, true);
        }
    }

    public boolean playerInAir() {
        return numFootContacts == 0;
    }

    public void preSolve(Contact c, Manifold m) {
    }

    public void postSolve(Contact c, ContactImpulse ci) {
    }

    public Array<Fixture> getBodies() {
        return bodiesToRemove;
    }

    public Body getEnemy() {
        return enemy;
    }

    public void nullifyEnemy() {
        enemy = null;
    }

    public Body getDoor() {
        return door;
    }

    public boolean isAtDoor() {
        return atDoor;
    }

    public void setPlayer(SpacemanPlayer player) {
        this.player = player;
    }

    private void playFootstep() {
        if ((int) (Math.random() * 3) == 2) {
            SoundManager.play(Assets.FOOTSTEP_SOUND);
        } else if ((int) (Math.random() * 3) == 1) {
            SoundManager.play(Assets.FOOTSTEP2_SOUND);
        } else {
            SoundManager.play(Assets.FOOTSTEP3_SOUND);
        }
    }

    private void shootEnemy(Fixture fa, Fixture fb) {
        bodiesToRemove.add(fb);

        if (fa.getUserData().equals("enemy")) {
            enemy = fa.getBody();
        }
    }

    private void killPlayer() {
        player.setDead(true);
    }
}