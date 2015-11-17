package com.saharw.andenginetest;

import android.graphics.Color;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

/**
 * Created by Sahar on 11/14/2015.
 */
public class ResourcesManager {

    public Font font;


    // holds singleton instance
    private static final ResourcesManager sInstance = new ResourcesManager();

    // variables
    public Engine engine;
    public GameActivity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;

    public ITextureRegion splashRegion; // for splash scree
    private BitmapTextureAtlas splashTextureAtlas; // for splash scree

    // textures & texture regions
    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion options_region;

    // Game Textures
    public BuildableBitmapTextureAtlas gameTextureAtlas;

    // Game Texture Regions
    public ITextureRegion platform1Region;
    public ITextureRegion platform2Region;
    public ITextureRegion platform3Region;
    public ITextureRegion coinRegion;

    public ITiledTextureRegion mPlayerRegion;

    private BuildableBitmapTextureAtlas menuTextureAtlas;

    // load & unload methods
    public void loadMenuResources(){
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }

    public void loadGameResources(){
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }

    private void loadMenuGraphics(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_bckgd.png");
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
        options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");

        try
        {
            this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.menuTextureAtlas.load();
        }
        catch (final ITextureAtlasBuilder.TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }

    }

    private void loadMenuAudio()
    {

    }

    private void loadGameGraphics(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

        platform1Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
        platform2Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
        platform3Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform3.png");
        coinRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");
        mPlayerRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);

        try
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        }
        catch (final ITextureAtlasBuilder.TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }

    private void loadGameFonts()
    {

    }

    private void loadGameAudio()
    {

    }

    private void loadMenuFonts()
    {
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font3.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }

    public void loadSplashScreen(){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
        splashTextureAtlas.load();
    }

    public void unloadSplashScreen(){
        splashTextureAtlas.unload();
        splashRegion = null;
    }

    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }

    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }

    public void unloadGameTextures()
    {
        // TODO (Since we did not create any textures for game scene yet)
    }



    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }

    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static ResourcesManager getInstance()
    {
        return sInstance;
    }
}
