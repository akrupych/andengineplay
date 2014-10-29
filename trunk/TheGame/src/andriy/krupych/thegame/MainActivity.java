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
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class MainActivity extends BaseGameActivity {

	private Camera mCamera;
	private Scene mScene;
	private Sound mSound;
	private Music mMusic;
	private TextureRegion mOgreTextureRegion;
	private TextureRegion mTrollTextureRegion;
	private TextureRegion mGruntTextureRegion;
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new FixedStepEngine(pEngineOptions, 60);
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, 800, 480);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(mCamera.getWidth(), mCamera.getHeight()), mCamera);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return engineOptions;
	}
	
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		SoundFactory.setAssetBasePath("sfx/");
		MusicFactory.setAssetBasePath("sfx/");
		try {
			mMusic = MusicFactory.createMusicFromAsset(getMusicManager(), this, "music.mp3");
			mSound = SoundFactory.createSoundFromAsset(getSoundManager(), this, "sound.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
				getTextureManager(), 200, 200);
//		BitmapTextureAtlas atlas = new BitmapTextureAtlas(getTextureManager(), 124, 46);
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
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
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
