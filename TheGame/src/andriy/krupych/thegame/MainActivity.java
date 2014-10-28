package andriy.krupych.thegame;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public class MainActivity extends BaseGameActivity {

	private Camera mCamera;
	private Scene mScene;
	
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
		return engineOptions;
	}
	
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
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
}
