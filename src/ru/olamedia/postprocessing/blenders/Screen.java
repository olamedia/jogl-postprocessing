package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Screen extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("screen.fp");
	}
}
