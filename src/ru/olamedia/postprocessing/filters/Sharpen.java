package ru.olamedia.postprocessing.filters;

public class Sharpen extends BaseConvolution {

	public void init() {
		super.init();
		loadFragmentShader("sharpen");
	}

}
