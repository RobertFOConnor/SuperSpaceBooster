package com.yellowbytestudios.spacedoctor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public class KeyboardController implements BasicController {

    @Override
    public boolean leftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    @Override
    public boolean rightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    @Override
    public boolean upPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.UP);
    }

    @Override
    public boolean downPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.DOWN);
    }

    @Override
    public boolean shootPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }

    @Override
    public boolean switchGunPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT);
    }

    @Override
    public boolean pausePressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

    @Override
    public boolean menuUp() {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP);
    }

    @Override
    public boolean menuDown() {
        return Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
    }

    @Override
    public boolean menuSelect() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
    }

    @Override
    public boolean menuBack() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }
}
