package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class HardLight extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("hardLight.fp");
	}
}
