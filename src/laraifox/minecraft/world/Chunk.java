package laraifox.minecraft.world;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import laraifox.minecraft.core.AABB;

public class Chunk {
	private static final int GENERATION_LIMIT = 4;

	public static final int CHUNK_SIZE = 16;

	private static final Random RANDOM = new Random();

	private final AABB aabb;
	private final int[][][] blocks;
	private final int chunkX, chunkY, chunkZ;

	private int vbo;
	private int[] ibos;
	private int[] bufferSizes;

	private ChunkUpdateThread thread;
	private ChunkUpdateThread updateThread;

	private boolean empty;

	private boolean initialized;
	private boolean dirty;
	private boolean processing;

	public Chunk(int chunkX, int chunkY, int chunkZ) {
		this.aabb = new AABB(chunkX * CHUNK_SIZE, chunkY * CHUNK_SIZE, chunkZ * CHUNK_SIZE, (chunkX + 1) * CHUNK_SIZE, (chunkY + 1) * CHUNK_SIZE, (chunkZ + 1) * CHUNK_SIZE);
		this.blocks = new int[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];

		//		final int BLOCK_ID = y < GENERATION_LIMIT ? y == 0 ? 6 : y == 63 ? 3 : 1 : 0;// RANDOM.nextInt(64) == 0 ? 0 : 1

		for (int i = 0; i < CHUNK_SIZE; i++) {
			for (int j = 0; j < CHUNK_SIZE; j++) {
				for (int k = 0; k < CHUNK_SIZE; k++) {
					short blockID = 0;

					int yLevel = chunkY * Chunk.CHUNK_SIZE + j;

					if (yLevel < GENERATION_LIMIT * Chunk.CHUNK_SIZE) {
						if (yLevel == 0) {
							blockID = 5;
						} else if (yLevel == GENERATION_LIMIT * Chunk.CHUNK_SIZE - 1) {
							blockID = 3;
						} else if (yLevel < 55) {
							blockID = 1;
						} else if (yLevel < 59) {
							blockID = 4;
						} else {
							blockID = 2;
						}
					}

					blocks[i][j][k] = blockID;
				}
			}
		}

		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;

		this.vbo = GL15.glGenBuffers();
		this.ibos = new int[Block.CUBE_FACE_COUNT];
		for (int i = 0; i < ibos.length; i++) {
			ibos[i] = GL15.glGenBuffers();
		}
		this.bufferSizes = new int[Block.CUBE_FACE_COUNT];

		this.initialized = false;
		this.dirty = false;
		this.processing = true;

		ChunkUpdateQueue.enqueueChunk(this);
	}

	public void invalidate() {
		this.initialized = false;
		this.dirty = true;

		GL15.glDeleteBuffers(vbo);
		for (int i = 0; i < ibos.length; i++) {
			GL15.glDeleteBuffers(ibos[i]);
		}

		this.vbo = GL15.glGenBuffers();
		this.ibos = new int[Block.CUBE_FACE_COUNT];
		for (int i = 0; i < ibos.length; i++) {
			ibos[i] = GL15.glGenBuffers();
		}
		this.bufferSizes = new int[Block.CUBE_FACE_COUNT];
	}

	public void update(World world) {
		if (this.isDirty() && !this.isProcessing()) {
			dirty = false;
			processing = true;

			ChunkUpdateQueue.enqueueChunk(this);
			//			for (int i = 0; i < Block.CUBE_FACE_COUNT; i++) {
			//				Chunk chunk = world.getChunk(chunkX + Block.CUBE_FACE_NORMALS[i * 3 + 0], chunkY + Block.CUBE_FACE_NORMALS[i * 3 + 1], chunkZ + Block.CUBE_FACE_NORMALS[i * 3 + 2]);
			//				if (chunk != null) {
			//					chunk.processing = true;
			//					ChunkUpdateQueue.enqueueChunkUpdate(chunk);
			//				}
			//			}
		} else if (this.isProcessing() && this.isFinished()) {
			initialized = true;
			processing = false;

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, updateThread.getVertexBuffer(), GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

			for (int i = 0; i < Block.CUBE_FACE_COUNT; i++) {
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibos[i]);
				GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, updateThread.getIndexBuffer(i), GL15.GL_STATIC_DRAW);
			}
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

			for (int i = 0; i < Block.CUBE_FACE_COUNT; i++) {
				bufferSizes[i] = updateThread.getBufferSize(i);
			}

			updateThread = null;

			ChunkUpdateQueue.releaseUpdateThread(this);
		}
	}

	public void render() {
		if (this.isInitialized()) {
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Block.CUBE_FLOATS_PER_VERTEX * Float.BYTES, 0 * Float.BYTES); // Vertex Position :
																																// Vector3f[x, y, z];
			GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, Block.CUBE_FLOATS_PER_VERTEX * Float.BYTES, 3 * Float.BYTES);
			GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, Block.CUBE_FLOATS_PER_VERTEX * Float.BYTES, 5 * Float.BYTES);

			for (int i = 0; i < Block.CUBE_FACE_COUNT; i++) {
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibos[i]);
				GL11.glDrawElements(GL11.GL_TRIANGLES, bufferSizes[i], GL11.GL_UNSIGNED_INT, 0);
			}

			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
		}
	}

	private boolean isInitialized() {
		return initialized;
	}

	public boolean isDirty() {
		return dirty;
	}

	public boolean isProcessing() {
		return processing;
	}

	public boolean isFinished() {
		if (updateThread != null) {
			return updateThread.isFinished();
		}

		return false;
	}

	public Block getBlock(int x, int y, int z) {
		return BlockRegistry.getBlock(blocks[x][y][z]);
	}

	public void setBlock(World world, int id, int x, int y, int z) {
		this.blocks[x][y][z] = id;
		this.dirty = true;

		for (int i = 0; i < Block.CUBE_FACE_COUNT; i++) {
			int adjacentChunkX = (int) ((this.chunkX * CHUNK_SIZE + Block.CUBE_FACE_NORMALS[i * 3 + 0] + x) / CHUNK_SIZE);
			int adjacentChunkY = (int) ((this.chunkY * CHUNK_SIZE + Block.CUBE_FACE_NORMALS[i * 3 + 1] + y) / CHUNK_SIZE);
			int adjacentChunkZ = (int) ((this.chunkZ * CHUNK_SIZE + Block.CUBE_FACE_NORMALS[i * 3 + 2] + z) / CHUNK_SIZE);

			//			if (adjacentChunkX < 0 || adjacentChunkX >= world.getSize() || adjacentChunkY < 0 || adjacentChunkY >= Stack.STACK_SIZE || adjacentChunkZ < 0 || adjacentChunkZ > world.getSize())
			//				continue;

			if (adjacentChunkX != this.chunkX || adjacentChunkY != this.chunkY || adjacentChunkZ != this.chunkZ) {
				Chunk chunk = world.getChunk(adjacentChunkX, adjacentChunkY, adjacentChunkZ);
				if (chunk != null) {
					chunk.dirty = true;
				}
			}
		}
	}

	public AABB getAABB() {
		return aabb;
	}

	public int[][][] getBlocks() {
		return blocks;
	}

	public int getX() {
		return chunkX;
	}

	public int getY() {
		return chunkY;
	}

	public int getZ() {
		return chunkZ;
	}

	public void setUpdateThread(ChunkUpdateThread updateThread) {
		this.updateThread = updateThread;
	}
}
