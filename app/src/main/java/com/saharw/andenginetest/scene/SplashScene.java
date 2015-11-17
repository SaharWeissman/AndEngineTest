package com.saharw.andenginetest.scene;

import com.saharw.andenginetest.SceneManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

/**
 * Created by Sahar on 11/14/2015.
 */
public class SplashScene extends BaseScene{

    private Sprite mSplash;

    @Override
    public void createScene(){
        mSplash = new Sprite(0, 0, resourcesManager.splashRegion, vbom)
        {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera)
            {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        };

        mSplash.setScale(1.5f);
        mSplash.setPosition(400, 240);
        attachChild(mSplash);

    }

    @Override
    public void onBackKeyPressed()
    {

    }

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene(){
        mSplash.detachSelf();
        mSplash.dispose();
        this.detachSelf();
        this.dispose();
    }
}
