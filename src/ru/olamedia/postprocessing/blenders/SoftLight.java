package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class SoftLight extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("softLight.fp");
	}
}
