package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Multiply extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("multiply.fp");
	}
}
