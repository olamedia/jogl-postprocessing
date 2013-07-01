package ru.olamedia.postprocessing.filters;

import ru.olamedia.postprocessing.BaseShaderFilter;

public class GrayScale extends BaseShaderFilter {

	public void init() {
		loadFragmentShader("grayScale");
	}

	public void load() {
		compile();
		use();
		setUniform1i("sampler0", 0);
	}
}
