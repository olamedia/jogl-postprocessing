package ru.olamedia.postprocessing;

abstract public class BaseShaderBlender extends BaseShaderFilter {
	protected int primaryTextureUnitNumber;
	protected int secondaryTextureUnitNumber;
	protected float opacity;

	protected String getSourcePath() {
		return "blenders/shader";
	}

	public void uniform() {
		setUniform1i("sampler0", primaryTextureUnitNumber);
		setUniform1i("sampler1", secondaryTextureUnitNumber);
		setUniform1f("opacity", opacity);
	}

	@Override
	public void load() {
		compile();
		uniform();
		use();
	}

	public void setPrimary(int inTextureUnitNumber) {
		primaryTextureUnitNumber = inTextureUnitNumber;
	}

	public void setSecondary(int inTextureUnitNumber) {
		secondaryTextureUnitNumber = inTextureUnitNumber;
	}

	public void setOpacity(float inValue) {
		opacity = inValue;
	}

	public float getOpacity() {
		return opacity;
	}
}
