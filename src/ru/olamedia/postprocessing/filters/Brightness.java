package ru.olamedia.postprocessing.filters;

import ru.olamedia.postprocessing.BaseShaderFilter;

public class Brightness extends BaseShaderFilter {

	private float alpha;

	public void init() {
		loadFragmentShader("brightness.fs");
	}

	public void load() {
		super.compile();
		setUniform1i("sampler0", 0);
		setUniform1f("alpha", alpha);
		super.use();
	}

	public void setBrightness(float value) {
		alpha = value;
	}

	public float getBrightness() {
		return alpha;
	}

}
