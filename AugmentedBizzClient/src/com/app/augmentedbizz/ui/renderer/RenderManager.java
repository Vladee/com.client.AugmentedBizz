package com.app.augmentedbizz.ui.renderer;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.data.ModelDataListener;
import com.app.augmentedbizz.ui.MainActivity;
import com.app.augmentedbizz.ui.glview.AugmentedGLSurfaceView;
import com.qualcomm.QCAR.QCAR;

/**
 * Manager for graphical rendering.
 * 
 * @author Vladi
 *
 */
public class RenderManager implements ModelDataListener
{
	private static int depthSize = 16;
	private static int stencilSize = 0;
	private MainActivity mainActivity;
	private AugmentedRenderer augmentedRenderer;
	
	public RenderManager(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		this.augmentedRenderer = new AugmentedRenderer();
	}
	
	public void initialize() throws Exception
	{
		if(getGlSurfaceView() == null)
		{
			throw(new Exception("GL surface view not available"));
		}
		
		getGlSurfaceView().init(QCAR.requiresAlpha(), depthSize, stencilSize);
		getGlSurfaceView().setRenderer(augmentedRenderer);
	}
	
	/**
	 * @return The GL surface view of the main screen.
	 */
	public AugmentedGLSurfaceView getGlSurfaceView()
	{
		return (AugmentedGLSurfaceView)mainActivity.findViewById(R.id.augmentedGLSurfaceView);
	}

	/**
	 * @return The augmented renderer
	 */
	public AugmentedRenderer getAugmentedRenderer()
	{
		return augmentedRenderer;
	}

	@Override
	public void onModelData(OpenGLModelConfiguration openGLModelConfiguration)
	{
		// TODO
	}

	@Override
	public void onModelError(Exception e)
	{
		// TODO
	}
}
