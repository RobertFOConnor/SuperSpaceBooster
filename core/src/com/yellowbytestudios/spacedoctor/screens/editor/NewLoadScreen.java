package com.yellowbytestudios.spacedoctor.screens.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.BackgroundManager;
import com.yellowbytestudios.spacedoctor.screens.menu.MainMenuScreen;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;


public class NewLoadScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private BackgroundManager bg;
    private SpriteText title;
    private SpriteButton newMapButton, loadMapButton;
    private SpriteButton backButton;
    private float buttonY = 180;


    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        bg = new BackgroundManager();
        newMapButton = new SpriteButton(Assets.NEW_MAP, new Vector2(-600, buttonY));
        loadMapButton = new SpriteButton(Assets.LOAD_MAP, new Vector2(MainGame.WIDTH, buttonY));
        title = new SpriteText(MainGame.languageFile.get("SELECT_OPTION").toUpperCase(), Fonts.timerFont);
        title.centerText();

        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));

        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        AnimationManager.applyAnimation(newMapButton, 200, buttonY);
        AnimationManager.applyAnimation(loadMapButton, 1120, buttonY);
        AnimationManager.startAnimation();
    }


    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (newMapButton.checkTouch(touch)) {
                advanceScreen(new SizeSelectScreen());
            } else if(loadMapButton.checkTouch(touch)) {
                if (MainGame.saveData.getMyMaps().size > 0) {
                    advanceScreen(new LoadMapScreen());
                }
            } else if (backButton.checkTouch(touch)) {
                goBack();
            }
        }
    }


    private void advanceScreen(final Screen s) {

        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT + 100);
        AnimationManager.applyAnimation(newMapButton, -600, buttonY);
        AnimationManager.applyAnimation(loadMapButton, MainGame.WIDTH, buttonY);
        AnimationManager.applyExitAnimation(backButton, -150, backButton.getY(), s);

        AnimationManager.startAnimation();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        title.draw(sb);
        backButton.draw(sb);
        newMapButton.draw(sb);
        loadMapButton.draw(sb);
        sb.end();
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
        advanceScreen(new MainMenuScreen());
    }
}
