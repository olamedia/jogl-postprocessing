package ru.olamedia.postprocessing;

public interface IShaderFilter extends IFilter {
	public void load(); // useProgram()

	public void unload();

	public Screen getScreen();

	public void setScreen(Screen outerSize);
}
