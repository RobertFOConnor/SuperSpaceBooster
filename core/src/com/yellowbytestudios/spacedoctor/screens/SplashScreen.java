package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.I18NBundle;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.menu.TitleScreen;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;

public class SplashScreen implements Screen {

    private OrthoCamera camera;
    private String percentage;
    private Sprite loadScreen;
    private Sprite loadWheel;
    private boolean displayImage = false;

    public SplashScreen() {
        camera = new OrthoCamera();
        camera.resize();
        Fonts.load();
        Assets.load();
    }

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
    }

    @Override
    public void update(float dt) {
        camera.update();
        percentage = ((int) (Assets.manager.getProgress() * 100) + "%");

        if (!displayImage) {
            if (Assets.manager.getProgress() > 0.02) {
                Texture texture = Assets.manager.get(Assets.LOADSCREEN, Texture.class);
                texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                loadScreen = new Sprite(texture);
                loadWheel = new Sprite(Assets.manager.get(Assets.LOADWHEEL, Texture.class));
                loadWheel.setPosition(MainGame.WIDTH - 180, 100);

                loadScreen.setAlpha(0f);
                loadWheel.setAlpha(0f);

                AnimationManager.applyFadeAnimation(loadScreen);
                AnimationManager.applyFadeAnimation(loadWheel);
                AnimationManager.startAnimation();

                displayImage = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {

        if (displayImage) {
            sb.setProjectionMatrix(camera.combined);
            sb.begin();
            loadScreen.draw(sb);
            loadWheel.rotate(10f);
            loadWheel.draw(sb);
            Fonts.GUIFont.setColor(Color.WHITE);
            Fonts.GUIFont.draw(sb, percentage, MainGame.WIDTH - Fonts.getWidth(Fonts.GUIFont, percentage) - 250, 160);
            sb.end();
        }

        if (Assets.update()) { // DONE LOADING. SHOW TITLE SCREEN.

            //SoundManager.setMusic(Assets.MAIN_THEME);
            MainGame.languageFile = Assets.manager.get(Assets.LANGUAGE_FILE, I18NBundle.class);


            //Setup skin (scene2d)
            Skin skin = Assets.manager.get(Assets.SKIN, Skin.class);
            TextField.TextFieldStyle tfs = skin.get("default", TextField.TextFieldStyle.class);
            TextButton.TextButtonStyle tbs = skin.get("default", TextButton.TextButtonStyle.class);
            tfs.font = Fonts.GUIFont;
            tfs.fontColor = Color.BLACK;
            tbs.font = Fonts.GUIFont;

            if (MainGame.QUICK_BOOT) {
                ScreenManager.setScreen(new GameScreen(1));
            } else {
                ScreenManager.setScreen(new TitleScreen());
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.resize();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void goBack() {

    }
}
