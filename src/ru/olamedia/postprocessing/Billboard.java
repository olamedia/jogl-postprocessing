package ru.olamedia.postprocessing;

import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;

public class Billboard {
	static int width = 1;
	static int height = 1;

	public static void setup() {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		final GLDrawable gld = GLContext.getCurrent().getGLDrawable();
		width = gld.getWidth();
		height = gld.getHeight();
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, width, height, 0, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public static void render(boolean isFlipped) {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		gl.glColor4f(1, 1, 1, 1);
		gl.glBegin(GL2.GL_QUADS);
		if (isFlipped) {
			// flipped billboard
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex2f(0.0f, 0.0f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex2f(width, 0.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex2f(width, height);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex2f(0.0f, height);
		} else {
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex2f(0.0f, 0.0f);
			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex2f(width, 0.0f);
			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex2f(width, height);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex2f(0.0f, height);
		}
		gl.glEnd();
	}
}
