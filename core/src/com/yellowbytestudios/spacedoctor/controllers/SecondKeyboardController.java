package com.yellowbytestudios.spacedoctor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by BobbyBoy on 10-Mar-16.
 */
public class SecondKeyboardController implements BasicController {

    @Override
    public boolean leftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.A);
    }

    @Override
    public boolean rightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.D);
    }

    @Override
    public boolean upPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.W);
    }

    @Override
    public boolean downPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.S);
    }

    @Override
    public boolean shootPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT);
    }

    @Override
    public boolean pausePressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.R);
    }

    @Override
    public boolean switchGunPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT);
    }
}
