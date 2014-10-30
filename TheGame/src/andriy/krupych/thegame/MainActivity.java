package andriy.krupych.thegame;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class MainActivity extends BaseGameActivity {
	
	private static final int SCREEN_WIDTH = 480;
	private static final int SCREEN_HEIGHT = 800;
	private static final int SCREEN_CENTER_X = SCREEN_WIDTH / 2;
	private static final int SCREEN_CENTER_Y = SCREEN_HEIGHT / 2;
	
	private Sound mSound;
	private Music mMusic;
	
	private TextureRegion mOgreTextureRegion;
	private TextureRegion mTrollTextureRegion;
	private TextureRegion mGruntTextureRegion;
	private TextureRegion mBackgroundTextureRegion;
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new FixedStepEngine(pEngineOptions, 60);
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		Camera camera = new Camera(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
				new RatioResolutionPolicy(camera.getWidth(), camera.getHeight()), camera);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return engineOptions;
	}
	
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		loadSounds();
		loadGraphics();
		loadBackground();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	private void loadBackground() {
		BitmapTextureAtlas atlas = new BitmapTextureAtlas(
				getEngine().getTextureManager(), 32, 32, TextureOptions.REPEATING_BILINEAR);
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, this, "grass.png", 0, 0);
		mBackgroundTextureRegion.setTextureSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		atlas.load();
	}

	private void loadGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
				getTextureManager(), 200, 200, TextureOptions.BILINEAR);
		mOgreTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, this, "ogre.png");
		mTrollTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, this, "troll.png");
		mGruntTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, this, "grunt.png");
		try {
			atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
					BitmapTextureAtlas>(1, 1, 1));
			atlas.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
	}

	private void loadSounds() {
		SoundFactory.setAssetBasePath("sfx/");
		MusicFactory.setAssetBasePath("sfx/");
		try {
			mMusic = MusicFactory.createMusicFromAsset(getMusicManager(), this, "music.mp3");
			mSound = SoundFactory.createSoundFromAsset(getSoundManager(), this, "sound.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		Scene scene = new Scene();
		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		Sprite sprite = new Sprite(SCREEN_CENTER_X, SCREEN_CENTER_Y,
				mBackgroundTextureRegion, getVertexBufferObjectManager());
		pScene.attachChild(sprite);
		Sprite grunt = new Sprite(100, SCREEN_HEIGHT - 100, mGruntTextureRegion,
				getVertexBufferObjectManager());
		pScene.attachChild(grunt);
		Sprite troll = new Sprite(200, SCREEN_HEIGHT - 100, mTrollTextureRegion,
				getVertexBufferObjectManager());
		pScene.attachChild(troll);
		Sprite ogre = new Sprite(300, SCREEN_HEIGHT - 100, mOgreTextureRegion,
				getVertexBufferObjectManager());
		pScene.attachChild(ogre);
		float baseBuffer[] = {
				0, 0, 0,
				0, 40, 0,
				SCREEN_WIDTH, 40, 0,
				SCREEN_WIDTH, 0, 0
		};
		Mesh meshBase = new Mesh(0, 0, baseBuffer,
				baseBuffer.length / 3, DrawMode.TRIANGLE_FAN, getVertexBufferObjectManager());
		meshBase.setColor(0.7f, 0.7f, 0.7f);
		pScene.attachChild(meshBase);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public synchronized void onResumeGame() {
		if (mMusic != null && !mMusic.isPlaying()) {
			mMusic.play();
			mSound.play();
		}
		super.onResumeGame();
	}
	
	@Override
	public synchronized void onPauseGame() {
		if (mMusic != null && mMusic.isPlaying()) {
			mMusic.pause();
		}
		super.onPauseGame();
	}
}
