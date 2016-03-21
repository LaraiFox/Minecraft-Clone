package laraifox.minecraft.world;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import laraifox.minecraft.core.Camera;
import laraifox.minecraft.core.Debugger;
import laraifox.minecraft.core.Frustum;
import laraifox.minecraft.core.PerspectiveProjection;
import laraifox.minecraft.core.Shader;
import laraifox.minecraft.enums.EWorldSize;
import laraifox.minecraft.math.Matrix4f;
import laraifox.minecraft.math.Quaternion;
import laraifox.minecraft.math.Vector2f;
import laraifox.minecraft.math.Vector3f;

public class World {
	private PerspectiveProjection projection;
	private Camera camera;
	private Camera debugCamera;

	private final Stack[] stacks;
	private final EWorldSize worldSize;

	public World(EWorldSize worldSize) {
		this.projection = new PerspectiveProjection(70.0f, Display.getWidth(), Display.getHeight(), 0.01f, 512.0f);
		this.camera = new Camera(Matrix4f.Projection(70.0f, projection.getWidth(), projection.getHeight(), projection.getZNear(), projection.getZFar()));
		camera.translate(camera.getForward(), -5.0f);
		this.debugCamera = new Camera(Matrix4f.Projection(projection.getFOV(), projection.getWidth(), projection.getHeight(), projection.getZNear(), projection.getZFar()));
		debugCamera.setPosition(new Vector3f(camera.getPosition()));
		debugCamera.setRotation(new Quaternion(camera.getRotation()));

		this.stacks = new Stack[worldSize.getSize() * worldSize.getSize()];
		for (int i = 0; i < stacks.length; i++) {
			stacks[i] = new Stack(i % worldSize.getSize(), i / worldSize.getSize());
		}
		this.worldSize = worldSize;
	}

	public void update() {
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

		final float speed = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 0.5f : 0.001f;

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

		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			for (Stack stack : stacks) {
				stack.invalidate();
			}
		}

		if (Mouse.isButtonDown(1)) {
			debugCamera.setPosition(new Vector3f(camera.getPosition()));
			debugCamera.setRotation(new Quaternion(camera.getRotation()));
		}

		for (Stack stack : stacks) {
			stack.update(this);
		}
	}

	public void render(Shader shader) {
		shader.setUniform("viewMatrix", camera.getViewMatrix());
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix());

		Frustum viewFrustum = new Frustum(debugCamera.getViewProjectionMatrix());

		viewFrustum.print();

		//		for (Stack stack : stacks) {
		//			stack.performCulling(viewFrustum);
		//		}

		for (int i = 0; i < stacks.length; i++) {
			shader.setUniform("stackPosition", new Vector2f((i % worldSize.getSize()) * Chunk.CHUNK_SIZE, (i / worldSize.getSize()) * Chunk.CHUNK_SIZE));

			stacks[i].render(shader, viewFrustum);
		}

		ChunkRenderQueue.render(shader);

		Debugger.render(camera, debugCamera, projection);

		//				for (int i = 0; i < worldSize.getSize(); i++) {
		//					for (int j = 0; j < worldSize.getSize(); j++) {
		//						shader.setUniform("stackPosition", new Vector2f(j * Chunk.CHUNK_SIZE, i * Chunk.CHUNK_SIZE));
		//		
		//						stacks[j + i * worldSize.getSize()].render(shader);
		//					}
		//				}
	}

	public int getSize() {
		return worldSize.getSize();
	}

	public Chunk getChunk(int x, int y, int z) {
		if (x < 0 || x >= worldSize.getSize() || y < 0 || y >= Stack.STACK_SIZE || z < 0 || z >= worldSize.getSize()) {
			return null;
		}

		return stacks[x + z * worldSize.getSize()].getChunk(y);
	}

	public Block getBlock(int x, int y, int z) {
		if (x < 0 || x >= worldSize.getSize() * Chunk.CHUNK_SIZE || y < 0 || y >= Stack.STACK_SIZE * Chunk.CHUNK_SIZE || z < 0 || z >= worldSize.getSize() * Chunk.CHUNK_SIZE) {
			return null;
		}

		return stacks[(x / Chunk.CHUNK_SIZE) + (z / Chunk.CHUNK_SIZE) * worldSize.getSize()].getBlock((int) (x % Chunk.CHUNK_SIZE), y, (int) (z % Chunk.CHUNK_SIZE));
	}

	public void setBlock(int id, int x, int y, int z) {
		if (x < 0 || x >= worldSize.getSize() * Chunk.CHUNK_SIZE || y < 1 || y >= Stack.STACK_SIZE * Chunk.CHUNK_SIZE || z < 0 || z >= worldSize.getSize() * Chunk.CHUNK_SIZE) {
			return;
		}

		stacks[(x / Chunk.CHUNK_SIZE) + (z / Chunk.CHUNK_SIZE) * worldSize.getSize()].setBlock(this, id, (int) (x % Chunk.CHUNK_SIZE), y, (int) (z % Chunk.CHUNK_SIZE));
	}
}
