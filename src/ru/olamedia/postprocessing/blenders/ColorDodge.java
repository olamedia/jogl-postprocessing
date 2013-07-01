package ru.olamedia.postprocessing.blenders;

import ru.olamedia.postprocessing.BaseShaderBlender;

public class ColorDodge extends BaseShaderBlender {
	@Override
	public void init() {
		loadFragmentShader("colorDodge.fp");
	}
}
