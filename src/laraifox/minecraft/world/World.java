package laraifox.minecraft.world;

import org.lwjgl.input.Keyboard;

import laraifox.minecraft.core.Shader;
import laraifox.minecraft.enums.EWorldSize;
import laraifox.minecraft.math.Vector2f;

public class World {
	private final Stack[] stacks;
	private final EWorldSize worldSize;

	public World(EWorldSize worldSize) {
		this.stacks = new Stack[worldSize.getSize() * worldSize.getSize()];
		for (int i = 0; i < stacks.length; i++) {
			stacks[i] = new Stack(i % worldSize.getSize(), i / worldSize.getSize());
		}
		this.worldSize = worldSize;
	}

	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			for (Stack stack : stacks) {
				stack.invalidate();
			}
		}

		for (Stack stack : stacks) {
			stack.update(this);
		}
	}

	public void render(Shader shader) {
		for (int i = 0; i < worldSize.getSize(); i++) {
			for (int j = 0; j < worldSize.getSize(); j++) {
				shader.setUniform("stackPosition", new Vector2f(j * Chunk.CHUNK_SIZE, i * Chunk.CHUNK_SIZE));

				stacks[j + i * worldSize.getSize()].render(shader);
			}
		}
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
		if (x < 0 || x >= worldSize.getSize() * Chunk.CHUNK_SIZE || y < 0 || y >= Stack.STACK_SIZE * Chunk.CHUNK_SIZE || z < 0 || z >= worldSize.getSize() * Chunk.CHUNK_SIZE) {
			return;
		}
		
		stacks[(x / Chunk.CHUNK_SIZE) + (z / Chunk.CHUNK_SIZE) * worldSize.getSize()].setBlock(id, (int) (x % Chunk.CHUNK_SIZE), y, (int) (z % Chunk.CHUNK_SIZE));
	}
}
