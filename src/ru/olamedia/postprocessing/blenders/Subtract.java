package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Subtract extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("subtract.fp");
	}
}
