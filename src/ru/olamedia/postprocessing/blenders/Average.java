package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Average extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("average.fp");
	}
}
