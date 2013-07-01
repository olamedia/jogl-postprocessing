package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Opacity extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("opacity.fp");
	}
}
