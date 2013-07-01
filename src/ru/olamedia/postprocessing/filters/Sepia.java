package ru.olamedia.postprocessing.filters;

import ru.olamedia.postprocessing.BaseShaderFilter;

public class Sepia extends BaseShaderFilter {

	public void init() {
		loadFragmentShader("sepia");
	}

	public void load() {
		compile();
		use();
		setUniform1i("sampler0", 0);
	}
}
