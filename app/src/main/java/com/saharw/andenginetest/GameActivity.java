package com.saharw.andenginetest;

import android.view.KeyEvent;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import java.io.IOException;

/**
 * Created by Sahar on 10/27/2015.
 */
public class GameActivity extends BaseGameActivity {

    private static final int FRAMES_PER_SECONDS = 60;
    private static final float CAMERA_WIDTH = 800;
    private static final float CAMERA_HEIGHT = 480;
    private Camera mCamera;

    private ResourcesManager mResourcesManager;


    @Override
    public void onCreateResources(OnCreateResourcesCallback callback) throws IOException {
        ResourcesManager.prepareManager(mEngine, this, mCamera, getVertexBufferObjectManager());
        mResourcesManager = ResourcesManager.getInstance();
        callback.onCreateResourcesFinished();

    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback()
        {
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMenuScene();
            }
        }));
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
        options.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);// enable sound & music
        options.setWakeLockOptions(WakeLockOptions.SCREEN_ON); // prevent screen from timeout
        return options;

    }

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        return new LimitedFPSEngine(pEngineOptions, FRAMES_PER_SECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false;
    }
}
