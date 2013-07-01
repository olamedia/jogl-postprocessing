package ru.olamedia.postprocessing.filters;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;

import ru.olamedia.postprocessing.BaseShaderFilter;

/**
 * Postprocessing filter inheritance root implementing no specific filter but
 * providing a common implementation of all interface methods suitable for most
 * convolution filter implementations.
 **/
public class BaseConvolution extends BaseShaderFilter {
	protected FloatBuffer mTextureCoordinateOffsets;

	public void init() {
		mTextureCoordinateOffsets = generate3x3TextureCoordinateOffsets();
	}

	public void load() {
		compile();
		use();
		setUniform1i("sampler0", 0);
		setUniform2fv("tc_offset", mTextureCoordinateOffsets);
	}

	protected FloatBuffer generate3x3TextureCoordinateOffsets() {
		float[] tTextureCoordinateOffsets = new float[18];
		float tXIncrease = 1.0f / ((float) getScreen().getWidth());
		float tYIncrease = 1.0f / ((float) getScreen().getHeight());
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				tTextureCoordinateOffsets[(((i * 3) + j) * 2) + 0] = (-1.0f * tXIncrease) + ((float) i * tXIncrease);
				tTextureCoordinateOffsets[(((i * 3) + j) * 2) + 1] = (-1.0f * tYIncrease) + ((float) j * tYIncrease);
			}
		}
		FloatBuffer buf = Buffers.newDirectFloatBuffer(tTextureCoordinateOffsets.length);
		buf.put(tTextureCoordinateOffsets);
		buf.rewind();
		return buf;
	}
}
