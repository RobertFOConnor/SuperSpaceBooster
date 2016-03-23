package com.yellowbytestudios.spacedoctor.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.game.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.game.objects.PickUp;
import com.yellowbytestudios.spacedoctor.media.Assets;

public class Box2DContactListeners implements ContactListener {

    private Array<Fixture> bodiesToRemove;
    private Body enemy;
    private Body Exit;

    public Box2DContactListeners() {
        super();
        bodiesToRemove = new Array<Fixture>();
    }

    public void beginContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();


        if (fa == null || fb == null) return;

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            ((SpacemanPlayer) fa.getBody().getUserData()).addNumFootContacts(1);
            playFootstep();
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            ((SpacemanPlayer) fb.getBody().getUserData()).addNumFootContacts(1);
            playFootstep();
        }

        if (fa.getUserData() != null && fa.getUserData().equals("player")) {
            if (fb.getFilterData().categoryBits == Box2DVars.BIT_SPIKE) {
                killPlayer((SpacemanPlayer) fa.getBody().getUserData());
            }
        }
        if (fb.getUserData() != null && fb.getUserData().equals("player")) {
            if (fa.getFilterData().categoryBits == Box2DVars.BIT_SPIKE) {
                killPlayer((SpacemanPlayer) fb.getBody().getUserData());
            }
        }

        if (fa.getUserData() != null && fa.getUserData().equals("bullet")) {
            shootEnemy(fb, fa);
        }
        if (fb.getUserData() != null && fb.getUserData().equals("bullet")) {
            shootEnemy(fa, fb);
        }

        if (fa.getUserData() != null && fa.getUserData().equals("door")) {
            Exit = fa.getBody();
            ((SpacemanPlayer) fb.getBody().getUserData()).setFinished(true);
        }
        if (fb.getUserData() != null && fb.getUserData().equals("door")) {
            Exit = fb.getBody();
            ((SpacemanPlayer) fa.getBody().getUserData()).setFinished(true);
        }

        if (fa.getUserData() != null && fa.getUserData().equals("pickup")) {
            ((PickUp) fa.getBody().getUserData()).activate((SpacemanPlayer) fb.getBody().getUserData());
            bodiesToRemove.add(fa);
        }
        if (fb.getUserData() != null && fb.getUserData().equals("pickup")) {
            ((PickUp) fb.getBody().getUserData()).activate((SpacemanPlayer) fa.getBody().getUserData());
            bodiesToRemove.add(fb);
        }

        if (fa.getUserData() != null && fa.getUserData().equals("enemy")) {

            if (fb.getUserData().equals("player")) {
                killPlayer((SpacemanPlayer) fb.getBody().getUserData());
            }
        }
        if (fb.getUserData() != null && fb.getUserData().equals("enemy")) {
            if (fa.getUserData().equals("player")) {
                killPlayer((SpacemanPlayer) fa.getBody().getUserData());
            }
        }
    }


    public void endContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            ((SpacemanPlayer) fa.getBody().getUserData()).addNumFootContacts(-1);
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            ((SpacemanPlayer) fb.getBody().getUserData()).addNumFootContacts(-1);
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

    public Body getExit() {
        return Exit;
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

    private void killPlayer(SpacemanPlayer player) {
        player.setDead(true);
    }
}