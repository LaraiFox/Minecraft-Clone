package laraifox.minecraft.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import laraifox.minecraft.enums.EWorldSize;
import laraifox.minecraft.interfaces.IGameManager;
import laraifox.minecraft.math.Vector3f;
import laraifox.minecraft.world.Block;
import laraifox.minecraft.world.Chunk;
import laraifox.minecraft.world.ChunkUpdateQueue;
import laraifox.minecraft.world.Crawler;
import laraifox.minecraft.world.Stack;
import laraifox.minecraft.world.World;

public class GameManager implements IGameManager {
	private static final Random RANDOM = new Random();

	private OpenGLDisplay display;

	private World world;
	private Shader shader;
	private Texture2D texture;
	private Vector3f lightDirection;

	private List<Crawler> crawlers;

	public GameManager() {

	}

	@Override
	public void initialize(OpenGLDisplay display) {
		try {
			Debugger.initialize();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Block.registerBlocks();

		// 533 using chunks
		// 531 using stacks

		this.display = display;

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//		GL11.glEnable(GL11.GL_CULL_FACE);

		this.world = new World(EWorldSize.LARGE);
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

		Display.setTitle(display.getTitle() + " FPS: " + display.getCurrentFPS() + " | Updates: " + ChunkUpdateQueue.getChunkUpdatesQueued() + " (" + ChunkUpdateQueue.getChunkUpdatesProcessing()
			+ ")");

		world.update();

		lightDirection.rotate(Vector3f.Up(), 0.1f);
	}

	@Override
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		shader.bind();
		shader.setUniform("color", new Vector3f(1.0f, 1.0f, 1.0f));
		shader.setUniform("lightDirection", lightDirection);

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		texture.bind();
		world.render(shader);
	}
}
