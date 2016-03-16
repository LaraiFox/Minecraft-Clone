package laraifox.minecraft.world;

import java.util.Random;

public class Crawler {
	private int x, y, z;
	private int blockID;
	private int lifespan;
	private int stepsTaken;
	private boolean alive;

	public Crawler(World world, Random random) {
		this.x = random.nextInt(world.getSize() * Chunk.CHUNK_SIZE);
		this.y = random.nextInt(Stack.STACK_SIZE * Chunk.CHUNK_SIZE);
		this.z = random.nextInt(world.getSize() * Chunk.CHUNK_SIZE);

		this.blockID = random.nextInt(2);

		this.lifespan = random.nextInt(1024 - 128) + 128;

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
