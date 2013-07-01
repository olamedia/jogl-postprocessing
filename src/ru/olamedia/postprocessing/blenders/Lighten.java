package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Lighten extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("lighten.fp");
	}
}
