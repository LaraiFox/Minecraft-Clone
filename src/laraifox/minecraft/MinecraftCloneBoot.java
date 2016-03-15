package laraifox.minecraft;

import java.io.File;

import laraifox.minecraft.core.GameManager;
import laraifox.minecraft.core.OpenGLDisplay;

public class MinecraftCloneBoot {
	private static final String PROGRAM_NAME = new String("Minecraft Clone");
	private static final String VERSION = new String("0.0.1 alpha");
	private static final String TITLE = new String(PROGRAM_NAME + " " + VERSION);

	public static void main(String[] args) {
		String operatingSystem = System.getProperty("os.name").toLowerCase();
		String username = System.getProperty("user.name");

		if (operatingSystem.contains("win")) {
			System.setProperty("org.lwjgl.librarypath", new File("lib/lwjgl/native/windows").getAbsolutePath());
		} else if (operatingSystem.contains("mac")) {
			System.setProperty("org.lwjgl.librarypath", new File("lib/lwjgl/native/macosx").getAbsolutePath());
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", PROGRAM_NAME);
		} else if (operatingSystem.contains("linux")) {
			System.setProperty("org.lwjgl.librarypath", new File("lib/lwjgl/native/linux").getAbsolutePath());
		} else {
			System.err.println("Your Operating System (" + operatingSystem + ") is unrecognised or unsupported");
			new Exception().printStackTrace();
			System.exit(1);
		}

		GameManager gameManager = new GameManager();

		OpenGLDisplay display = new OpenGLDisplay(TITLE, 1280, 720, gameManager);
		display.start();
	}
}
