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
}
