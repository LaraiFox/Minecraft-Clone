package laraifox.minecraft.world;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class Chunk {
	private static final int GENERATION_LIMIT = 4;

	public static final int CHUNK_SIZE = 16;

	private static final Random RANDOM = new Random();

	private final Block[][][] blocks;
	private final int x, y, z;

	private int vbo;
	private int[] ibos;
	private int[] bufferSizes;

	private ChunkUpdateThread thread;
	private ChunkUpdateThread updateThread;

	private boolean empty;

	private boolean initialized;
	private boolean dirty;
	private boolean processing;

	public Chunk(int x, int y, int z) {
		this.blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];

		final int BLOCK_ID = y < GENERATION_LIMIT ? 1 : 0;// RANDOM.nextInt(64) == 0 ? 0 : 1

		for (int i = 0; i < CHUNK_SIZE; i++) {
			for (int j = 0; j < CHUNK_SIZE; j++) {
				for (int k = 0; k < CHUNK_SIZE; k++) {
					blocks[i][j][k] = new Block(BLOCK_ID);
				}
			}
		}

		this.x = x;
		this.y = y;
		this.z = z;

		this.vbo = GL15.glGenBuffers();
		this.ibos = new int[Block.CUBE_FACE_COUNT];
		for (int i = 0; i < ibos.length; i++) {
			ibos[i] = GL15.glGenBuffers();
		}
		this.bufferSizes = new int[Block.CUBE_FACE_COUNT];

		this.initialized = false;
		this.dirty = false;
		this.processing = true;

		ChunkUpdateQueue.enqueueChunkUpdate(this);
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

			ChunkUpdateQueue.enqueueChunkUpdate(this);
			for (int i = 0; i < Block.CUBE_FACE_COUNT; i++) {
				Chunk chunk = world.getChunk(x + Block.CUBE_FACE_NORMALS[i * 3 + 0], y + Block.CUBE_FACE_NORMALS[i * 3 + 1], z + Block.CUBE_FACE_NORMALS[i * 3 + 2]);
				if (chunk != null) {
					chunk.processing = true;
					ChunkUpdateQueue.enqueueChunkUpdate(chunk);
				}
			}
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
		return blocks[x][y][z];
	}

	public void setBlock(int id, int x, int y, int z) {
		this.blocks[x][y][z] = new Block(id);
		this.dirty = true;
	}

	public Block[][][] getBlocks() {
		return blocks;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setUpdateThread(ChunkUpdateThread updateThread) {
		this.updateThread = updateThread;
	}
}
