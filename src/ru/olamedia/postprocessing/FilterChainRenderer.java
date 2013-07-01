package ru.olamedia.postprocessing;

import java.util.ArrayList;

import javax.media.opengl.GLContext;

public class FilterChainRenderer implements IRenderer {
	private FilterRenderer filterRenderer;
	private ArrayList<IShaderFilter> shaders = new ArrayList<IShaderFilter>();
	private Screen outerSize;
	private boolean wasInitialized = false;

	public FilterChainRenderer() {
		filterRenderer = new FilterRenderer();
	}

	public void setSource(IRenderer sourceRenderer) {
		filterRenderer.setSource(sourceRenderer);
	}

	public void addFilter(IShaderFilter filter) {
		shaders.add(filter);
	}

	@Override
	public void init() {
		System.out.println("[" + this.getClass().getName() + "] init");
		if (wasInitialized) {
			throw new RuntimeException("Already initialized!");
		}
		if (null == outerSize) {
			throw new RuntimeException("Setup screen first!");
		}
		wasInitialized = true;
		filterRenderer.setScreen(outerSize);
		filterRenderer.init();
		if (shaders.size() > 0) {
			for (int i = 0; i < shaders.size(); i++) {
				final IShaderFilter shader = shaders.get(i);
				shader.setScreen(outerSize);
				shader.init();
			}
		}
	}

	@Override
	public void renderScene() {
		// System.out.println("\r\n" + "[" + this.getClass().getName() +
		// "] renderScene");
		if (!wasInitialized) {
			throw new RuntimeException("Init first!");
		}
		filterRenderer.reset();
		filterRenderer.setShader(null);
		final int size = shaders.size();
		if (size > 0) {
			filterRenderer.renderSourceToFBO();
			for (int i = 0; i < size; i++) {
				final IShaderFilter shader = shaders.get(i);
				filterRenderer.setShader(shader);
				if (i == size - 1) {
					// final pass, render to screen
					filterRenderer.renderScene();
				} else {
					filterRenderer.renderFBOToFBO();
				}
				// System.exit(0);
			}
			// final GL2 gl = GLContext.getCurrentGL().getGL2();
			// gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
			// filterRenderer.setShader(null);
			// filterRenderer.renderScene();
		} else {
			// no shaders, just render to screen
			filterRenderer.renderSourceToScreen();
		}
		GLContext.getCurrent().getGLDrawable().swapBuffers();
	}

	@Override
	public void cleanup() {
		filterRenderer.cleanup();
	}

	public Screen getScreen() {
		return outerSize;
	}

	public void setScreen(Screen screen) {
		this.outerSize = screen;
	}
}
