package ru.olamedia.postprocessing;

import java.nio.FloatBuffer;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

public abstract class BaseShaderFilter implements IShaderFilter {
	private ShaderCode fragmentShader;
	private ShaderCode vertexShader;

	private ShaderState state;
	private ShaderProgram program;

	private Screen outerSize;

	protected String getSourcePath() {
		return "filters/shader";
	}

	public ShaderState getState() {
		return state;
	}

	public void setUniform1i(String name, int value) {
		GL2ES2 gl = GLContext.getCurrentGL().getGL2ES2();
		final int location = state.getUniformLocation(gl, name);
		gl.glUniform1i(location, value);
	}

	public void setUniform1f(String name, float value) {
		GL2ES2 gl = GLContext.getCurrentGL().getGL2ES2();
		final int location = state.getUniformLocation(gl, name);
		gl.glUniform1f(location, value);
	}

	public void setUniform2fv(String name, FloatBuffer value) {
		GL2ES2 gl = GLContext.getCurrentGL().getGL2ES2();
		final int location = state.getUniformLocation(gl, name);
		gl.glUniform2fv(location, value.capacity() / 2, value);
	}

	abstract public void init();

	abstract public void load();

	public void use() {
		System.out.println("[" + this.getClass().getName() + "] use");
		GL2ES2 gl = GLContext.getCurrentGL().getGL2ES2();
		gl.glUseProgram(program.program());
	}

	public boolean compile() {
		if (null == state) {
			GL2ES2 gl = GLContext.getCurrentGL().getGL2ES2();
			state = new ShaderState();
			state.setVerbose(true);
			program = new ShaderProgram();
			if (fragmentShader != null) {
				program.add(fragmentShader);
			}
			if (vertexShader != null) {
				program.add(vertexShader);
			}
			program.link(gl, System.err);
			program.validateProgram(gl, System.err);
			state.attachShaderProgram(gl, program, true);
			if (!program.linked()) {
				throw new RuntimeException("can't link shader");
			} else {
				System.out.println("\r\nShader is linked");
			}
			return true;
		}
		return false;
	}

	public void loadFragmentShader(String filename) {
		GL2ES2 gl = GLContext.getCurrentGL().getGL2ES2();
		fragmentShader = ShaderCode.create(gl, GL2ES2.GL_FRAGMENT_SHADER, BaseShaderFilter.class, getSourcePath(),
				getSourcePath() + "/bin", filename, true);
	}

	public void loadVertexShader(String filename) {
		GL2ES2 gl = GLContext.getCurrentGL().getGL2ES2();
		vertexShader = ShaderCode.create(gl, GL2ES2.GL_VERTEX_SHADER, BaseShaderFilter.class, getSourcePath(),
				getSourcePath() + "/bin", filename, true);
	}

	public void unload() {
		final GL2ES2 gl = GLContext.getCurrentGL().getGL2ES2();
		gl.glUseProgram(0);
	}

	public void cleanup() {
		final GL2ES2 gl = GLContext.getCurrentGL().getGL2ES2();
		if (fragmentShader != null) {
			fragmentShader.destroy(gl);
		}
		if (vertexShader != null) {
			vertexShader.destroy(gl);
		}
	}

	public Screen getScreen() {
		return outerSize;
	}

	public void setScreen(Screen outerSize) {
		this.outerSize = outerSize;
	}
}
