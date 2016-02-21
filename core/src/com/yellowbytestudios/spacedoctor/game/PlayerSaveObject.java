package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.utils.Array;

/**
 * Created by BobbyBoy on 20-Feb-16.
 */
public class PlayerSaveObject {

    private int currLevel = 1;
    private int HELMET_NUM = 0;
    private Array<Integer> UNLOCKED_HEADS = new Array<Integer>();

    private boolean soundFXEnabled = true;
    private boolean musicEnabled = true;

    public PlayerSaveObject() {
        unlockHead(0);
    }

    public void setHead(int head) {
        HELMET_NUM = head;
    }

    public int getHead() {
        return HELMET_NUM;
    }

    public boolean isUnlocked(int headNum) {
        return UNLOCKED_HEADS.contains(headNum, true);
    }

    public void unlockHead(int headNum) {
        if (!isUnlocked(headNum)) {
            UNLOCKED_HEADS.add(headNum);
        }
    }

    public int getCurrLevel() {
        return currLevel;
    }

    public void setCurrLevel(int currLevel) {
        if (this.currLevel < 10) {
            this.currLevel = currLevel;
        }
    }

    public boolean isSoundFXEnabled() {
        return soundFXEnabled;
    }

    public void setSoundFXEnabled(boolean soundFXEnabled) {
        this.soundFXEnabled = soundFXEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setMusicEnabled(boolean musicEnabled) {
        this.musicEnabled = musicEnabled;
    }
}
