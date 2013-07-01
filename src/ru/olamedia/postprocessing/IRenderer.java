package ru.olamedia.postprocessing;

public interface IRenderer {
	public void init();
	public void renderScene();
	public void cleanup();
	public Screen getScreen();
	public void setScreen(Screen screen);
}
