package com.saharw.andenginetest.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.saharw.andenginetest.SceneManager;
import com.saharw.andenginetest.entities.Player;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Created by Sahar on 11/17/2015.
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener{

    private HUD mGameHUD;
    private Text mScoreText;
    private int mScore = 0;
    private PhysicsWorld mPhysicsWorld;
    private Player mPlayer;

    private boolean mFirstTouch = false;

    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";


    @Override
    public void createScene() {
        createBackground();
        createHUD();
        createPhysics();
        loadLevel(1);
        setOnSceneTouchListener(this);
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
        camera.setHUD(null);
        camera.setCenter(400, 240);
        camera.setChaseEntity(null);
    }

    private void createBackground()
    {
        setBackground(new Background(Color.BLUE));
    }

    private void createHUD()
    {
        mGameHUD = new HUD();
        camera.setHUD(mGameHUD);

        // CREATE SCORE TEXT
        mScoreText = new Text(20, 420, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        mScoreText.setAnchorCenter(0, 0);
        mScoreText.setText("Score: 0");
        mScoreText.setColor(Color.WHITE);
        mGameHUD.attachChild(mScoreText);

        camera.setHUD(mGameHUD);
    }

    private void addToScore(int i)
    {
        mScore += i;
        mScoreText.setText("Score: " + mScore);
    }

    private void createPhysics()
    {
        mPhysicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
        registerUpdateHandler(mPhysicsWorld);
    }

    private void loadLevel(int levelID){
        final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

                // TODO later we will specify camera BOUNDS and create invisible walls
                // on the beginning and on the end of the level.

                return GameScene.this;
            }
        });

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

                final Sprite levelObject;

                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform1Region, vbom);
                    PhysicsFactory.createBoxBody(mPhysicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform2Region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform2");
                    mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform3Region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform3");
                    mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
                {
                    levelObject = new Sprite(x, y, resourcesManager.coinRegion, vbom)
                    {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed)
                        {
                            super.onManagedUpdate(pSecondsElapsed);

                            /**
                             * TODO
                             * we will later check if player collide with this (coin)
                             * and if it does, we will increase score and hide coin
                             * it will be completed in next articles (after creating player code)
                             */
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
                {
                    mPlayer = new Player(x, y, vbom, camera, mPhysicsWorld)
                    {
                        @Override
                        public void onDie()
                        {
                            // TODO Latter we will handle it.
                        }
                    };
                    levelObject = mPlayer;
                }
                else
                {
                    throw new IllegalArgumentException();
                }

                levelObject.setCullingEnabled(true);

                return levelObject;
            }
        });

        levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown())
        {
            if (!mFirstTouch)
            {
                mPlayer.setRunning();
                mFirstTouch = true;
            }
            else
            {
                mPlayer.jump();
            }
        }
        return false;
    }
}
