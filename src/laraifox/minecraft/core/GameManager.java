package laraifox.minecraft.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import laraifox.minecraft.enums.EWorldSize;
import laraifox.minecraft.interfaces.IGameManager;
import laraifox.minecraft.math.Matrix4f;
import laraifox.minecraft.math.Vector3f;
import laraifox.minecraft.world.Block;
import laraifox.minecraft.world.Chunk;
import laraifox.minecraft.world.ChunkUpdateQueue;
import laraifox.minecraft.world.Stack;
import laraifox.minecraft.world.World;

public class GameManager implements IGameManager {
	private static final Random RANDOM = new Random();

	private OpenGLDisplay display;

	private World world;
	private Camera camera;
	private Shader shader;
	private Texture2D texture;

	private int vbo, ibo, ibo2;

	public GameManager() {

	}

	@Override
	public void initialize(OpenGLDisplay display) {
		this.display = display;

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		this.world = new World(EWorldSize.LARGE);
		this.camera = new Camera(Matrix4f.Projection(70, display.getWidth(), display.getHeight(), 0.01f, 512.0f));
		camera.translate(camera.getForward(), -5.0f);
		try {
			this.shader = new Shader("res/shaders/basic.vs", "res/shaders/basic.fs", true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.texture = Texture2D.getTextureFrom("res/textures/cube_texture.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.vbo = GL15.glGenBuffers();
		this.ibo = GL15.glGenBuffers();
		this.ibo2 = GL15.glGenBuffers();

		FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(Float.BYTES * Block.CUBE_VERTICES.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexBuffer.put(Block.CUBE_VERTICES);
		vertexBuffer.flip();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

		IntBuffer indexBuffer = ByteBuffer.allocateDirect(Integer.BYTES * Block.CUBE_INDICES.length).order(ByteOrder.nativeOrder()).asIntBuffer();
		indexBuffer.put(Block.CUBE_INDICES);
		indexBuffer.flip();

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
	}

	@Override
	public void cleanUp() {

	}

	@Override
	public void tick() {
		Display.setTitle(display.getTitle() + " FPS: " + display.getCurrentFPS());
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

		//		System.out.println(camera.getPosition().toString());

		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			int id = RANDOM.nextInt(2);
			int x = RANDOM.nextInt(world.getSize() * Chunk.CHUNK_SIZE);
			int y = RANDOM.nextInt((Stack.STACK_SIZE - 4) * Chunk.CHUNK_SIZE) + 4 * Chunk.CHUNK_SIZE;
			int z = RANDOM.nextInt(world.getSize() * Chunk.CHUNK_SIZE);

			world.setBlock(id, x, y, z);
		}

		ChunkUpdateQueue.update(world);

		world.update();
	}

	@Override
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		shader.bind();
		shader.setUniform("viewMatrix", camera.getViewMatrix());
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
		shader.setUniform("color", new Vector3f(1.0f, 1.0f, 1.0f));

		//		renderBlock();

		texture.bind();
		world.render(shader);

		//		GL11.glLineWidth(2.0f);
		//
		//		shader.setUniform("viewMatrix", Quaternion.conjugate(camera.getRotation()).toRotationMatrix());
		//		Vector3f origin = camera.getRotation().getForward();
		//
		//		shader.setUniform("color", new Vector3f(1.0f, 0.0f, 0.0f));
		//		GL11.glBegin(GL11.GL_LINES);
		//		GL11.glVertex3f(origin.getX(), origin.getY(), origin.getZ());
		//		GL11.glVertex3f(origin.getX() + 0.1f, origin.getY(), origin.getZ());
		//		GL11.glEnd();
		//
		//		shader.setUniform("color", new Vector3f(0.0f, 1.0f, 0.0f));
		//		GL11.glBegin(GL11.GL_LINES);
		//		GL11.glVertex3f(origin.getX(), origin.getY(), origin.getZ());
		//		GL11.glVertex3f(origin.getX(), origin.getY() + 0.1f, origin.getZ());
		//		GL11.glEnd();
		//
		//		shader.setUniform("color", new Vector3f(0.0f, 0.0f, 1.0f));
		//		GL11.glBegin(GL11.GL_LINES);
		//		GL11.glVertex3f(origin.getX(), origin.getY(), origin.getZ());
		//		GL11.glVertex3f(origin.getX(), origin.getY(), origin.getZ() + 0.1f);
		//		GL11.glEnd();
	}

	private void renderBlock() {
		texture.bind();

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * Float.BYTES, 0 * Float.BYTES); // Vertex Position : Vector3f[x, y, z];
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES); // Vertex Texcoord : Vector2f[x, y];

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLES, Block.CUBE_INDICES.length, GL11.GL_UNSIGNED_INT, 0);

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}

}
