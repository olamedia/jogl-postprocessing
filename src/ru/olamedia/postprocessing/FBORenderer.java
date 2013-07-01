package ru.olamedia.postprocessing;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.FBObject;
import com.jogamp.opengl.util.PMVMatrix;

public class FBORenderer implements IRenderer {
	private FBObject fbo;
	private int fboId = 0;
	private int colorTextureID;

	private int depthTextureID;
	private Screen outerSize;
	private Screen innerSize;
	private boolean isFlipped = false;
	private IRenderer renderer;
	private PMVMatrix defaultMatrix;

	/**
	 * @return the colorTextureID
	 */
	public int getColorTextureID() {
		return colorTextureID;
	}

	/**
	 * @return the isFlipped
	 */
	public boolean isFlipped() {
		return isFlipped;
	}

	/**
	 * @param isFlipped
	 *            the isFlipped to set
	 */
	public void setFlipped(boolean isFlipped) {
		this.isFlipped = isFlipped;
	}

	/**
	 * @return the renderer
	 */
	public IRenderer getRenderer() {
		return renderer;
	}

	/**
	 * @param renderer
	 *            the renderer to set
	 */
	public void setRenderer(IRenderer renderer) {
		this.renderer = renderer;
	}

	public Screen getScreen() {
		return outerSize;
	}

	public void setScreen(Screen screen) {
		this.outerSize = screen;
	}

	public Screen getTextureSize() {
		return innerSize;
	}

	public void setTextureSize(Screen screen) {
		this.innerSize = screen;
	}

	public void renderToFrameBuffer() {
		bindFrameBuffer();
		if (renderer != null) {
			renderer.renderScene();
		}
		unbindFrameBuffer();
	}

	public void bindFrameBuffer() {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		System.out.println("[" + this.getClass().getName() + "] bind FBO");
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fboId);
		gl.glViewport(0, 0, innerSize.getWidth(), innerSize.getHeight());
	}

	public void unbindFrameBuffer() {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		gl.glFinish();
		System.out.println("[" + this.getClass().getName() + "] unbind FBO");
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
	}

	public void renderScene() {
		renderAsFullscreenBillboard();
	}

	public FBObject getFrameBuffer() {
		return fbo;
	}

	public void bindTextureAsSource(int textureUnitID) {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		gl.glPushAttrib(GL2.GL_TEXTURE_BIT);
		gl.glActiveTexture(textureUnitID);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, colorTextureID);
		// set the texture up to be used for painting a surface ...
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
	}

	public void bindTextureAsTarget(int textureUnitID) {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		gl.glActiveTexture(textureUnitID);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, colorTextureID);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		// gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
		// GL2.GL_REPLACE);
	}

	public void unbindTextureUnit() {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		// restore the active texture ...
		gl.glPopAttrib();
	}

	public void resetFrustumToDefaultState() {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		final float aspect = (float) outerSize.getWidth() / (float) outerSize.getHeight();
		gl.glViewport(0, 0, outerSize.getWidth(), outerSize.getHeight());
		defaultMatrix.glMatrixMode(GL2.GL_PROJECTION);
		defaultMatrix.glLoadIdentity();
		defaultMatrix.gluPerspective(45.0f, aspect, 0.01f, 200.0f);
		defaultMatrix.glMatrixMode(GL2.GL_MODELVIEW);
		defaultMatrix.glLoadIdentity();
		defaultMatrix.gluLookAt(0f, 0f, 70f, 0f, 0f, 0f, 0.0f, 1.0f, 0.0f);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadMatrixf(defaultMatrix.glGetPMatrixf());
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadMatrixf(defaultMatrix.glGetMvMatrixf());
	}

	public void renderAsFullscreenBillboard() { // render TEXTURE 0
		System.out.println("[" + this.getClass().getName() + "] renderAsFullscreenBillboard using GL_TEXTURE0");
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		// reset frustum to default state ...
		resetFrustumToDefaultState();
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glDisable(GL2.GL_CULL_FACE);
		// disable depth test so that billboards can be rendered on top of each
		// other ...
		gl.glDisable(GL2ES2.GL_DEPTH_TEST);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, outerSize.getWidth(), outerSize.getHeight(), 0, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		// bindTextureAsTarget(GL2.GL_TEXTURE0); // bind colortexture as
		// TEXTURE0
		// gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
		// GL2.GL_REPLACE);
		// gl.glClearColor(0.7f, 0f, 0f, 0f);

		// gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		// System.out.println("[" + this.getClass().getName() +
		// "] clear color buffer [blue]");
		gl.glBegin(GL2.GL_QUADS);
		// gl.glEnable(GL2.GL_BLEND);
		// gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE);
		gl.glColor4f(1, 1, 1, 1);
		if (isFlipped) {
			// flipped billboard
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex2f(0.0f, 0.0f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex2f(outerSize.getWidth(), 0.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex2f(outerSize.getWidth(), outerSize.getHeight());
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex2f(0.0f, outerSize.getHeight());
		} else {
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex2f(0.0f, 0.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex2f(outerSize.getWidth(), 0.0f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex2f(outerSize.getWidth(), outerSize.getHeight());
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex2f(0.0f, outerSize.getHeight());
		}
		gl.glEnd();
		// unbindTextureUnit();
	}

	@Override
	public void init() {
		System.out.println("[" + this.getClass().getName() + "] init");
		if (null == outerSize) {
			throw new RuntimeException("Setup screen first!");
		}
		if (null == innerSize) {
			throw new RuntimeException("Setup screen first!");
		}
		defaultMatrix = new PMVMatrix();

		final GL2 gl = GLContext.getCurrentGL().getGL2();
		int[] result = new int[1];
		gl.glGenFramebuffers(1, result, 0);
		fboId = result[0];
		gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fboId);
		// allocate the colour texture ...
		gl.glGenTextures(1, result, 0);
		colorTextureID = result[0];
		gl.glBindTexture(GL2.GL_TEXTURE_2D, colorTextureID);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA8, innerSize.getWidth(), innerSize.getHeight(), 0,
				GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, null);
		// allocate the depth texture ...
		gl.glGenTextures(1, result, 0);
		depthTextureID = result[0];
		gl.glBindTexture(GL2.GL_TEXTURE_2D, depthTextureID);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_DEPTH_COMPONENT32, innerSize.getWidth(), innerSize.getHeight(), 0,
				GL2.GL_DEPTH_COMPONENT, GL2.GL_UNSIGNED_INT, null);
		// attach the textures to the framebuffer
		gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_TEXTURE_2D, colorTextureID, 0);
		gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_DEPTH_ATTACHMENT, GL2.GL_TEXTURE_2D, depthTextureID, 0);
		gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
		// check if fbo is set up correctly ...
		if (gl.glCheckFramebufferStatus(GL2.GL_FRAMEBUFFER) != GL2.GL_FRAMEBUFFER_COMPLETE) {
			int tError = gl.glCheckFramebufferStatus(GL2.GL_FRAMEBUFFER);
			switch (tError) {
			case GL2.GL_FRAMEBUFFER_COMPLETE:
				System.out.println("[" + this.getClass().getName()
						+ "FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_COMPLETE_EXT");
				break;
			case GL2.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
				System.out.println("[" + this.getClass().getName()
						+ "FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT");
				break;
			case GL2.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
				System.out.println("[" + this.getClass().getName()
						+ "FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT");
				break;
			case GL2.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
				System.out.println("[" + this.getClass().getName()
						+ "FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT");
				break;
			case GL2.GL_FRAMEBUFFER_INCOMPLETE_FORMATS:
				System.out.println("[" + this.getClass().getName()
						+ "FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT");
				break;
			case GL2.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
				System.out.println("[" + this.getClass().getName()
						+ "FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT");
				break;
			case GL2.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
				System.out.println("[" + this.getClass().getName()
						+ "FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT");
				break;
			case GL2.GL_FRAMEBUFFER_UNSUPPORTED:
				System.out.println("[" + this.getClass().getName()
						+ "FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_UNSUPPORTED_EXT");
				break;
			default:
				System.out.println("[" + this.getClass().getName() + "FRAMEBUFFER CHECK RETURNED UNKNOWN RESULT ...");
			}
			throw new RuntimeException("FBO incomplete");
		}
		if (null != renderer) {
			// initialize the assigned fbo renderer ...
			renderer.init();
		}
	}

	@Override
	public void cleanup() {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		gl.glDeleteFramebuffers(1, Buffers.newDirectIntBuffer(fboId));
		gl.glDeleteTextures(1, Buffers.newDirectIntBuffer(colorTextureID));
		gl.glDeleteTextures(1, Buffers.newDirectIntBuffer(depthTextureID));
		if (null != renderer) {
			renderer.cleanup();
		}

	}
}
