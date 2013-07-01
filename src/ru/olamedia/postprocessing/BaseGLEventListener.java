package ru.olamedia.postprocessing;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public class BaseGLEventListener implements GLEventListener {

	private FilterChainRenderer filterChain;
	private Screen screen;

	public FilterChainRenderer getFilterChain() {
		return filterChain;
	}

	public void setSceneRenderer(IRenderer sceneRenderer) {
		filterChain.setSource(sceneRenderer);
	}

	public BaseGLEventListener() {
		filterChain = new FilterChainRenderer();
	}

	public void init(GLAutoDrawable drawable) {
		System.out.println("[" + this.getClass().getName() + "] init");
		// if (BaseGlobalEnvironment.getInstance().wantsVSync()) {
		// enable vertical sync ... :*
		// gl.setSwapInterval(1);
		// }
		screen = new Screen();
		screen.setWidth(drawable.getWidth());
		screen.setHeight(drawable.getHeight());
		filterChain.setScreen(screen);
		filterChain.init();
	}

	public void display(GLAutoDrawable drawable) {
		// System.out.println("[" + this.getClass().getName() +
		// "] clear color buffer");
		// gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
		// gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		filterChain.renderScene();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		drawable.getGL().glViewport(0, 0, width, height);
		screen.setWidth(width);
		screen.setHeight(height);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean inModeChanged, boolean inDeviceChanged) {
	}

	public void dispose(GLAutoDrawable drawable) {
		filterChain.cleanup();
	}

}
