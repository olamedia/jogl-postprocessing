package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Darken extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("darken.fp");
	}
}
