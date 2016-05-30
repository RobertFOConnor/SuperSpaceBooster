package com.yellowbytestudios.spacedoctor.textscroller;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by BobbyBoy on 18-Oct-15.
 */
public class ConversationManager {

    private Vector2 touch;
    private Array<String> script;
    private TextScroller textScroller;
    private int scriptLine = 0;

    private boolean conversationMode = false;


    public void update() {
        if(conversationMode)
        textScroller.update();
    }

    public void advanceConversation() {
        if(textScroller.isFinished()) {

            if(scriptLine < script.size-1) {
                scriptLine++;
                textScroller = new TextScroller(script.get(scriptLine));
            } else {
                conversationMode = false;
            }
        }
    }

    public void render(SpriteBatch sb) {
        if(isConversationMode()) {
            textScroller.render(sb);
        }
    }

    public boolean isConversationMode() {
        return conversationMode;
    }

    public void startConversation(String fileName) {
        script = ScriptReader.readScript(fileName);
        scriptLine = 0;
        textScroller = new TextScroller(script.get(scriptLine));
        conversationMode = true;
    }
}
