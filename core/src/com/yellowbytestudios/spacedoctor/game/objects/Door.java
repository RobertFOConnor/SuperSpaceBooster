package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Door extends Box2DSprite {

    private Vector2 startPos, endPos;
    private float SPEED = 4f;

    public Door(Body body) {
        super(body);
    }

    public void activate(int[] assNums) {
        for(int i : assNums) {
            for(int k : associationNumbers) {
                if(i == k) {




                }
            }
        }
    }

    public Vector2 getStartPos() {
        return startPos;
    }

    public void setStartPos(Vector2 startPos) {
        this.startPos = startPos;
    }

    public Vector2 getEndPos() {
        return endPos;
    }

    public void setEndPos(Vector2 endPos) {
        this.endPos = endPos;
    }
}
