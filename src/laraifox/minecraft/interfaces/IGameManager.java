package laraifox.minecraft.interfaces;

import laraifox.minecraft.core.OpenGLDisplay;

public interface IGameManager {
	public void initialize(OpenGLDisplay display);

	public void cleanUp();

	public void tick();

	public void update(float delta);

	public void render();
}
