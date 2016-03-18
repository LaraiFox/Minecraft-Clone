package laraifox.minecraft.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import laraifox.minecraft.enums.EWorldSize;
import laraifox.minecraft.interfaces.IGameManager;
import laraifox.minecraft.math.Matrix4f;
import laraifox.minecraft.math.Vector3f;
import laraifox.minecraft.world.Block;
import laraifox.minecraft.world.Chunk;
import laraifox.minecraft.world.ChunkUpdateQueue;
import laraifox.minecraft.world.Crawler;
import laraifox.minecraft.world.Stack;
import laraifox.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GameManager implements IGameManager {
	private static final Random RANDOM = new Random();

	private OpenGLDisplay display;

	private World world;
	private Camera camera;
	private Shader shader;
	private Texture2D texture;
	private Vector3f lightDirection;

	private List<Crawler> crawlers;

	public GameManager() {

	}

	@Override
	public void initialize(OpenGLDisplay display) {
		Block.registerBlocks();

		// 533 using chunks
		// 531 using stacks

		this.display = display;

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);

		this.world = new World(EWorldSize.GIGANTIC);
		this.camera = new Camera(Matrix4f.Projection(70, display.getWidth(), display.getHeight(), 0.01f, 512.0f));
		camera.translate(camera.getForward(), -5.0f);
		try {
			this.shader = new Shader("res/shaders/basic.vs", "res/shaders/basic.fs", true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.texture = Texture2D.getTextureFrom("res/textures/mc_blocks.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.lightDirection = new Vector3f(1.0f, -1.5f, 1.2f).normalize();

		this.crawlers = new ArrayList<Crawler>();
		// for (int i = 0; i < world.getSize() * 4; i++) {
		// crawlers.add(new Crawler(world, RANDOM, (short) 0, 64));
		// }
	}

	@Override
	public void cleanUp() {

	}

	@Override
	public void tick() {
		// Display.setTitle(display.getTitle() + " FPS: " + display.getCurrentFPS());
	}

	@Override
	public void update(float delta) {

		if (Mouse.isGrabbed()) {
			camera.rotate(Vector3f.Up(), Mouse.getDX() * 0.28f);
			camera.rotate(camera.getRight(), -Mouse.getDY() * 0.28f);
		}

		Mouse.setGrabbed(Mouse.isButtonDown(0));

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			camera.rotate(Vector3f.Up(), -1.0f);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			camera.rotate(Vector3f.Up(), 1.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && !Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			camera.rotate(camera.getRight(), -1.0f);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			camera.rotate(camera.getRight(), 1.0f);
		}

		final float speed = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 0.5f : 0.1f;

		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			camera.translate(Vector3f.Up(), -speed);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			camera.translate(Vector3f.Up(), speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
			camera.translate(camera.getRight(), -speed);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A)) {
			camera.translate(camera.getRight(), speed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_W)) {
			camera.translate(camera.getForward().projectToPlane(Vector3f.Up()).normalize(), -speed);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S)) {
			camera.translate(camera.getForward().projectToPlane(Vector3f.Up()).normalize(), speed);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			int id = RANDOM.nextInt(0);
			int x = RANDOM.nextInt(world.getSize() * Chunk.CHUNK_SIZE);
			int y = RANDOM.nextInt((Stack.STACK_SIZE - 4) * Chunk.CHUNK_SIZE) + 4 * Chunk.CHUNK_SIZE;
			int z = RANDOM.nextInt(world.getSize() * Chunk.CHUNK_SIZE);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			int id = RANDOM.nextInt(2);
			int x = RANDOM.nextInt(world.getSize() * Chunk.CHUNK_SIZE);
			int y = RANDOM.nextInt((Stack.STACK_SIZE - 4) * Chunk.CHUNK_SIZE) + 4 * Chunk.CHUNK_SIZE;
			int z = RANDOM.nextInt(world.getSize() * Chunk.CHUNK_SIZE);

			world.setBlock(id, x, y, z);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
			crawlers.add(new Crawler(world, RANDOM));
		}

		Iterator<Crawler> iterator = crawlers.iterator();
		while (iterator.hasNext()) {
			Crawler crawler = iterator.next();

			if (crawler.isAlive()) {
				crawler.update(world, RANDOM);
			} else {
				iterator.remove();
			}
		}

		ChunkUpdateQueue.update(world);

		Display.setTitle(display.getTitle() + " FPS: " + display.getCurrentFPS() + " | Updates: " + ChunkUpdateQueue.getChunkUpdatesQueued() + " ("
			+ ChunkUpdateQueue.getChunkUpdatesProcessing() + ")");

		world.update();

		lightDirection.rotate(Vector3f.Up(), 0.1f);
	}

	@Override
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		shader.bind();
		shader.setUniform("viewMatrix", camera.getViewMatrix());
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
		shader.setUniform("color", new Vector3f(1.0f, 1.0f, 1.0f));
		shader.setUniform("lightDirection", lightDirection);

		texture.bind();
		world.render(shader);
	}
}
