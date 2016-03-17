package laraifox.minecraft.world;

import laraifox.minecraft.core.Shader;

public class Stack {
	public static final int STACK_SIZE = 16;

	private final Chunk[] chunks;

	public Stack(int x, int z) {
		this.chunks = new Chunk[STACK_SIZE];
		for (int i = 0; i < chunks.length; i++) {
			chunks[i] = new Chunk(x, i, z);
		}
	}

	public void invalidate() {
		for (Chunk chunk : chunks) {
			chunk.invalidate();
		}
	}

	public void update(World world) {
		for (Chunk chunk : chunks) {
			chunk.update(world);
		}
	}

	public void render(Shader shader) {
		for (int i = 0; i < STACK_SIZE; i++) {
			shader.setUniform("chunkHeight", (float) (i * Chunk.CHUNK_SIZE));

			chunks[i].render();
		}
	}

	public Block getBlock(int x, int y, int z) {
		return chunks[y / Chunk.CHUNK_SIZE].getBlock(x, (int) (y % Chunk.CHUNK_SIZE), z);
	}

	public void setBlock(World world, short id, int x, int y, int z) {
		chunks[y / Chunk.CHUNK_SIZE].setBlock(world, id, x, (int) (y % Chunk.CHUNK_SIZE), z);
	}

	public Chunk getChunk(int y) {
		return chunks[y];
	}
}
