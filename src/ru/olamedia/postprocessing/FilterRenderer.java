package ru.olamedia.postprocessing;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

import com.jogamp.opengl.FBObject;
import com.jogamp.opengl.FBObject.Attachment;
import com.jogamp.opengl.FBObject.TextureAttachment;

public class FilterRenderer implements IRenderer {
	private FBObject primaryFBO;
	private FBObject secondaryFBO;
	private IShaderFilter shader;
	private boolean wasInitialized = false;

	private Screen outerSize;

	private IRenderer sourceRenderer;

	public void setSource(IRenderer renderer) {
		sourceRenderer = renderer;
	}

	public void setShader(IShaderFilter shader) {
		this.shader = shader;
	}

	private FBObject createFBO() {
		final FBObject fbo = new FBObject();
		final GL2ES2 gl = GLContext.getCurrentGL().getGL2ES2();
		fbo.reset(gl, getScreen().getWidth(), getScreen().getHeight());
		fbo.attachTexture2D(gl, 0, true);
		fbo.attachRenderbuffer(gl, Attachment.Type.DEPTH, 32);
		fbo.unbind(gl);
		return fbo;
	}

	@Override
	public void init() {
		if (wasInitialized) {
			throw new RuntimeException("Already initialized!");
		}
		System.out.println("[" + this.getClass().getName() + "] init");
		if (null == outerSize) {
			throw new RuntimeException("Setup screen first!");
		}
		primaryFBO = createFBO();
		secondaryFBO = createFBO();

		sourceRenderer.setScreen(getScreen());
		sourceRenderer.init();
		reset();
	}

	private boolean usingPrimaryTarget = true;

	public void reset() {
		if (null == primaryFBO) {
			throw new RuntimeException("Init first!");
		}
		// at first, we using original as source, primary as target
		usingPrimaryTarget = true;
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		getTargetFBO().reset(gl, outerSize.getWidth(), outerSize.getHeight());
		getSourceFBO().reset(gl, outerSize.getWidth(), outerSize.getHeight());
		// source.setRenderer(sourceRenderer);
	}

	public void swap() {
		// System.out.println("[" + this.getClass().getName() + "] swap");
		// then swapping target and source
		usingPrimaryTarget = !usingPrimaryTarget;
	}

	public FBObject getTargetFBO() {
		return usingPrimaryTarget ? primaryFBO : secondaryFBO;
	}

	public FBObject getSourceFBO() {
		return usingPrimaryTarget ? secondaryFBO : primaryFBO;
	}

	/**
	 * Rendering to the texture is straightforward.
	 * Simply bind your framebuffer, and draw your scene as usual.
	 */
	public void renderFBOToFBO() {
		// System.out.println("[" + this.getClass().getName() +
		// "] renderFBOToFBO");
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		gl.glPushAttrib(GL2.GL_TRANSFORM_BIT | GL2.GL_ENABLE_BIT | GL2.GL_COLOR_BUFFER_BIT | GL2.GL_VIEWPORT_BIT
				| GL2.GL_TEXTURE_BIT);//
		Billboard.setup();// getScreen().getWidth(), getScreen().getHeight()
		gl.glDisable(GL2ES2.GL_DEPTH_TEST);
		// gl.glActiveTexture(GL2.GL_TEXTURE1);
		// gl.glEnable(GL2.GL_TEXTURE_2D);
		// targetFBO.use(gl, (TextureAttachment) targetFBO.getColorbuffer(0));

		// gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
		// GL2.GL_MODULATE);
		getTargetFBO().bind(gl);
		final TextureAttachment tex0 = (TextureAttachment) getSourceFBO().getColorbuffer(0);
		if (null != shader) {
			// System.out.println("[" + this.getClass().getName() +
			// "] shader.load");
			shader.load();
		}
		gl.glActiveTexture(GL2.GL_TEXTURE0);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex0.getName());
		gl.glClearColor(0f, 1f, 0.5f, 0.5f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		Billboard.render(false);// getScreen().getWidth(),
								// getScreen().getHeight(),
		if (null != shader) {
			shader.unload();
		}
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		gl.glFinish();
		getTargetFBO().syncSamplingSink(gl);
		//gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		gl.glPopAttrib();

		swap();
	}

	@Override
	public void cleanup() {

	}

	@Override
	public Screen getScreen() {
		return outerSize;
	}

	@Override
	public void setScreen(Screen screen) {
		outerSize = screen;
	}

	/**
	 * Rendering to the texture is straightforward.
	 * Simply bind your framebuffer, and draw your scene as usual.
	 */
	public void renderSourceToFBO() {
		// System.out.println("[" + this.getClass().getName() +
		// "] renderSourceToFBO");
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		getSourceFBO().bind(gl);
		sourceRenderer.renderScene();
		gl.glFinish();
		getSourceFBO().syncSamplingSink(gl);
		//gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
	}

	public void renderSourceToScreen() {
		// System.out.println("[" + this.getClass().getName() +
		// "] renderSourceToFBO");
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		sourceRenderer.renderScene();
		gl.glFinish();
	}

	@Override
	public void renderScene() {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		// System.out.println("[" + this.getClass().getName() +
		// "] renderScene");
		final TextureAttachment tex0 = (TextureAttachment) getSourceFBO().getColorbuffer(0);
		// gl.glClearColor(0f, 0f, 0f, 0f);
		// gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glPushAttrib(GL2.GL_TRANSFORM_BIT | GL2.GL_ENABLE_BIT | GL2.GL_COLOR_BUFFER_BIT | GL2.GL_VIEWPORT_BIT
				| GL2.GL_TEXTURE_BIT);
		Billboard.setup();// outerSize.getWidth(), outerSize.getHeight()
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glClearColor(0, 0, 0, 1f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex0.getName());
		// sourceFBO.use(gl, (TextureAttachment) sourceFBO.getColorbuffer(0));
		// gl.glBindTexture(GL.GL_TEXTURE_2D, source.getColorTextureID());
		if (null != shader) {
			shader.load();
		}
		Billboard.render(false);// outerSize.getWidth(), outerSize.getHeight(),
		if (null != shader) {
			shader.unload();
		}
		gl.glPopAttrib();
		gl.glFinish();
	}

}