package com.yellowbytestudios.spacedoctor.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public class XBoxController implements BasicController {

    private Controller controller;
    private boolean rightTriggerJustPressed = false;

    public XBoxController() {
        controller = Controllers.getControllers().get(0);
    }

    @Override
    public boolean leftPressed() {
        return controller.getAxis(XBox360Pad.AXIS_LEFT_X) < -0.2f;
    }

    @Override
    public boolean rightPressed() {
        return controller.getAxis(XBox360Pad.AXIS_LEFT_X) > 0.2f;
    }

    @Override
    public boolean upPressed() {
        return controller.getAxis(XBox360Pad.AXIS_LEFT_Y) < -0.2f;
    }

    @Override
    public boolean downPressed() {
        return false;
    }

    @Override
    public boolean shootPressed() {
        if(controller.getAxis(XBox360Pad.AXIS_RIGHT_TRIGGER) < -0.5f) {
            if(!rightTriggerJustPressed) {
                rightTriggerJustPressed = true;
                return true;
            } else {
                return false;
            }
        } else {
            rightTriggerJustPressed = false;
            return false;
        }
    }
}
