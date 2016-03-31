package com.yellowbytestudios.spacedoctor.spriter;

import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;

/**
 * Created by Robert on 03/31/16.
 */
public abstract class MySpriterAnimationListener implements Player.PlayerListener {


    @Override
    public abstract void animationFinished(Animation animation);

    @Override
    public void animationChanged(Animation oldAnim, Animation newAnim) {

    }

    @Override
    public void preProcess(Player player) {

    }

    @Override
    public void postProcess(Player player) {

    }

    @Override
    public void mainlineKeyChanged(Mainline.Key prevKey, Mainline.Key newKey) {

    }
}
