package com.yellowbytestudios.spacedoctor.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public class XBoxController implements BasicController {

    private Controller controller;
    private boolean rightTriggerJustPressed, switchGunJustPressed = false;

    public XBoxController(int controllerNumber) {
        controller = Controllers.getControllers().get(controllerNumber);
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
        return controller.getButton(XBox360Pad.BUTTON_A);
    }

    @Override
    public boolean downPressed() {
        return false;
    }

    @Override
    public boolean shootPressed() {
        if (controller.getAxis(XBox360Pad.AXIS_RIGHT_TRIGGER) < -0.5f) {
            if (!rightTriggerJustPressed) {
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

    @Override
    public boolean pausePressed() {
        return controller.getButton(XBox360Pad.BUTTON_START);
    }

    @Override
    public boolean switchGunPressed() {
        if (controller.getButton(XBox360Pad.BUTTON_RB)) {
            if (!switchGunJustPressed) {
                switchGunJustPressed = true;
                return true;
            } else {
                return false;
            }
        } else {
            switchGunJustPressed = false;
            return false;
        }
    }
}
