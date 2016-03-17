package laraifox.minecraft.world;

import java.util.Random;

public class Crawler {
	private int x, y, z;
	private short blockID;
	private int lifespan;
	private int stepsTaken;
	private boolean alive;

	public Crawler(World world, Random random) {
		this(world, random, (short) random.nextInt(5), Stack.STACK_SIZE * Chunk.CHUNK_SIZE);
	}

	public Crawler(World world, Random random, short blockID, int maxHeight) {
		this.x = random.nextInt(world.getSize() * Chunk.CHUNK_SIZE);
		this.y = random.nextInt(maxHeight);
		this.z = random.nextInt(world.getSize() * Chunk.CHUNK_SIZE);

		this.blockID = blockID;

		this.lifespan = random.nextInt(2048 - 128) + 128;

		this.alive = true;

		world.setBlock(blockID, x, y, z);
	}

	public void update(World world, Random random) {
		int direction = random.nextInt(Block.CUBE_FACE_COUNT);

		this.x += Block.CUBE_FACE_NORMALS[direction * 3 + 0];
		this.y += Block.CUBE_FACE_NORMALS[direction * 3 + 1];
		this.z += Block.CUBE_FACE_NORMALS[direction * 3 + 2];

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					world.setBlock(blockID, x + i, y + j, z + k);
				}
			}
		}

		stepsTaken++;

		if (stepsTaken >= lifespan) {
			alive = false;
		}
	}

	public boolean isAlive() {
		return alive;
	}
}
