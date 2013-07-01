package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class Exclusion extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("exclusion.fp");
	}
}
