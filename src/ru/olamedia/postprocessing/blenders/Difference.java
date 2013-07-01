package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Difference extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("difference.fp");
	}
}
