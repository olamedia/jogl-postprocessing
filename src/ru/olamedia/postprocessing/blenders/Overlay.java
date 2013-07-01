package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Overlay extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("overlay.fp");
	}
}
