package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class ColorBurn extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("colorBurn.fp");
	}
}
