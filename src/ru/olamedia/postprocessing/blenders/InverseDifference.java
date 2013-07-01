package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class InverseDifference extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("inverseDifference.fp");
	}
}
