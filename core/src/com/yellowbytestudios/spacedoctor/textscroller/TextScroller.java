package com.yellowbytestudios.spacedoctor.textscroller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.media.Fonts;

/**
 * Created by BobbyBoy on 17-Oct-15.
 */
public class TextScroller {

    private Array<String> messageArray;
    private String message, displayString;
    private int charCounter = 0;
    private int lineCounter = 0;

    private static Texture textBox, textArrow;
    private long startTime;
    private long tickerTime = 20;

    private boolean finished = false;

    private float x, y;


    public TextScroller(String message) {
        textBox = new Texture(Gdx.files.internal("textBox.png"));
        textArrow = new Texture(Gdx.files.internal("textArrow.png"));
        this.message = message;
        displayString = "";

        startTime = System.nanoTime();

        x = MainGame.WIDTH/2-textBox.getWidth()/2;
        y = MainGame.HEIGHT-(textBox.getHeight()+20);


        messageArray = new Array<String>();
        String remainderString = message;

        int maxChar = 40;
        int spacePos;
        while(remainderString.length() > 0) {
            if(remainderString.length() > maxChar) {
                String sub = remainderString.substring(0, maxChar);
                spacePos = sub.lastIndexOf(" ");
                remainderString = remainderString.substring(spacePos+1, remainderString.length());
                messageArray.add(sub.substring(0, spacePos));
            } else {
                messageArray.add(remainderString.substring(0, remainderString.length()));
                remainderString = "";
            }
        }
    }

    public void update() {

        if(!finished) {
            float timeElapsed = (System.nanoTime() - startTime) / 1000000;


            String tickerString = messageArray.get(lineCounter);

            if (timeElapsed > tickerTime) {

                if (charCounter < tickerString.length()) {
                    charCounter++;
                    displayString = tickerString.substring(0, charCounter);

                } else {

                    if(lineCounter < messageArray.size-1) {
                        lineCounter++;
                        charCounter = 0;
                        displayString = "";

                    } else {
                        finished = true;
                    }
                }

                startTime = System.nanoTime();
            }
        }
    }


    public void render(SpriteBatch sb) {
        sb.draw(textBox, x, y);



        if(lineCounter > 0) {
            Fonts.largeFont.draw(sb, messageArray.get(lineCounter-1).toUpperCase(), x + 80, y + 180);
            Fonts.largeFont.draw(sb, displayString.toUpperCase(), x + 80, y + 80);
        } else {
            Fonts.largeFont.draw(sb, displayString.toUpperCase(), x + 80, y + 180);
        }

        if(finished) {
            sb.draw(textArrow, x+1700, y+40);
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
