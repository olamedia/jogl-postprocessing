package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Add extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("add.fp");
	}
}
