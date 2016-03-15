package laraifox.minecraft.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import laraifox.minecraft.interfaces.IGameManager;

public final class OpenGLDisplay {
	private static final String DEFAULT_TITLE = new String("OpenGL Display");
	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 600;
	private static final boolean DEFAULT_FULLSCREEN = false;
	private static final boolean DEFAULT_RESIZABLE = false;
	private static final boolean DEFAULT_VSYNC = false;
	private static final int DEFAULT_FRAMERATE = 60;
	private static final int DEFAULT_UPDATERATE = 60;
	private static final int DEFAULT_TICKRATE = 1;

	protected String title;
	protected int width, height;
	protected boolean fullscreen;
	protected boolean resizable;
	protected boolean vSyncEnabled;

	protected PixelFormat pixelFormat;
	protected ContextAttribs contextAttribs;

	private boolean initialized;
	private boolean running;

	private int framerate, updaterate, tickrate;
	private int updates, ups;
	private int frames, fps;

	private IGameManager gameManager;
	private boolean resized;

	public OpenGLDisplay(IGameManager gameManager) {
		this(DEFAULT_TITLE, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_RESIZABLE, DEFAULT_VSYNC, DEFAULT_FRAMERATE, DEFAULT_UPDATERATE, DEFAULT_TICKRATE, gameManager);
	}

	public OpenGLDisplay(String title, IGameManager gameManager) {
		this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_RESIZABLE, DEFAULT_VSYNC, DEFAULT_FRAMERATE, DEFAULT_UPDATERATE, DEFAULT_TICKRATE, gameManager);
	}

	public OpenGLDisplay(int width, int height, IGameManager gameManager) {
		this(DEFAULT_TITLE, width, height, DEFAULT_FULLSCREEN, DEFAULT_RESIZABLE, DEFAULT_VSYNC, DEFAULT_FRAMERATE, DEFAULT_UPDATERATE, DEFAULT_TICKRATE, gameManager);
	}

	public OpenGLDisplay(String title, int width, int height, IGameManager gameManager) {
		this(title, width, height, DEFAULT_FULLSCREEN, DEFAULT_RESIZABLE, DEFAULT_VSYNC, DEFAULT_FRAMERATE, DEFAULT_UPDATERATE, DEFAULT_TICKRATE, gameManager);
	}

	public OpenGLDisplay(String title, int width, int height, boolean fullscreen, IGameManager gameManager) {
		this(title, width, height, fullscreen, DEFAULT_RESIZABLE, DEFAULT_VSYNC, DEFAULT_FRAMERATE, DEFAULT_UPDATERATE, DEFAULT_TICKRATE, gameManager);
	}

	public OpenGLDisplay(String title, int width, int height, boolean fullscreen, boolean resizable, boolean vSync, int framerate, int updaterate, int tickrate, IGameManager gameManager) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.resizable = resizable;
		this.vSyncEnabled = vSync;

		this.pixelFormat = new PixelFormat();
		this.contextAttribs = new ContextAttribs();

		this.initialized = false;
		this.running = false;

		this.framerate = framerate;
		this.updaterate = updaterate;
		this.tickrate = tickrate;
		this.updates = 0;
		this.frames = 0;
		this.ups = 0;
		this.fps = 0;

		this.gameManager = gameManager;

		try {
			this.initialize();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public final void initialize() throws LWJGLException {
		if (initialized)
			return;

		initialized = true;

		Display.setTitle(title);
		this.setDisplayMode((int) width, (int) height, fullscreen);
		Display.setFullscreen(fullscreen);
		Display.setResizable(resizable);
		Display.setVSyncEnabled(vSyncEnabled);
		Display.create(pixelFormat, contextAttribs);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		gameManager.initialize(this);
	}

	private void setDisplayMode(int width, int height, boolean fullscreen) {
		if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen)) {
			return;
		}

		try {
			DisplayMode targetDisplayMode = null;

			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++) {
					DisplayMode current = modes[i];

					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);
		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}
	}

	public final void start() {
		if (running)
			return;

		if (initialized) {
			running = true;
			gameLoop();
		}
	}

	public final void stop() {
		if (!running)
			return;

		running = false;
	}

	private final void gameLoop() {
		long previousDeltaTime = System.nanoTime();
		long previousTick = System.nanoTime();
		long previousUpdate = System.nanoTime();
		long nanosecondsPerTick = (long) ((float) 1000000000 / (float) tickrate);
		long nanosecondsPerUpdate = (long) ((float) 1000000000 / (float) updaterate);

		while (!Display.isCloseRequested() && running) {
			long currentTime = System.nanoTime();

			if (currentTime - previousTick >= nanosecondsPerTick) {
				previousTick += nanosecondsPerTick;
				ups = (int) (updates / (1.0f / tickrate));
				updates = 0;
				fps = (int) (frames / (1.0f / tickrate));
				frames = 0;
				tick();
			}

			if (currentTime - previousUpdate >= nanosecondsPerUpdate) {
				previousUpdate += nanosecondsPerUpdate;
				float delta = (float) (currentTime - previousDeltaTime) / (float) nanosecondsPerUpdate;
				previousDeltaTime = currentTime;
				update(delta);
				updates++;
			}

			render();
			frames++;

			Display.update();
			Display.sync(framerate);
		}

		initialized = false;
		this.cleanUp();
		Display.destroy();
	}

	private final void cleanUp() {
		gameManager.cleanUp();
	}

	private final void tick() {
		gameManager.tick();
	}

	private final void update(float delta) {
		if (this.resized) {
			this.resized = false;
			this.setDisplayMode(width, height, fullscreen);
		}

		gameManager.update(delta);
	}

	private final void render() {
		gameManager.render();
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final int getWidth() {
		return width;
	}

	public final void setWidth(int width) {
		this.resized = true;
		this.width = width;
	}

	public final int getHeight() {
		return height;
	}

	public final void setHeight(int height) {
		this.resized = true;
		this.height = height;
	}

	public final boolean isFullscreen() {
		return fullscreen;
	}

	public final void setFullscreen(boolean isFullscreen) {
		this.resized = true;
		this.fullscreen = isFullscreen;
	}

	public final boolean isResizable() {
		return resizable;
	}

	public final void setResizable(boolean isResizable) {
		this.resizable = isResizable;
	}

	public final boolean isVSyncEnabled() {
		return vSyncEnabled;
	}

	public final void setVSyncEnabled(boolean isVSyncEnabled) {
		this.vSyncEnabled = isVSyncEnabled;
	}

	public final PixelFormat getPixelFormat() {
		return pixelFormat;
	}

	public final void setPixelFormat(PixelFormat pixelFormat) {
		this.pixelFormat = pixelFormat;
	}

	public final ContextAttribs getContextAttribs() {
		return contextAttribs;
	}

	public final void setContextAttribs(ContextAttribs contextAttribs) {
		this.contextAttribs = contextAttribs;
	}

	public final boolean isInitialized() {
		return initialized;
	}

	public final void setInitialized(boolean isInitialized) {
		this.initialized = isInitialized;
	}

	public final boolean isRunning() {
		return running;
	}

	public final void setRunning(boolean isRunning) {
		this.running = isRunning;
	}

	public final int getFramerate() {
		return framerate;
	}

	public final void setFramerate(int framerate) {
		this.framerate = framerate;
	}

	public final int getUpdaterate() {
		return updaterate;
	}

	public final void setUpdaterate(int updaterate) {
		this.updaterate = updaterate;
	}

	public final int getCurrentUPS() {
		return ups;
	}

	public final int getCurrentFPS() {
		return fps;
	}
}
