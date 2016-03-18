package laraifox.minecraft.world;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ChunkUpdateThread extends Thread {
	// private static int test = 0;

	private FloatBuffer vertexBuffer;
	private IntBuffer[] indexBuffers;

	private int[] bufferSizes;

	private World world;
	private Chunk chunk;
	private final int chunkX, chunkY, chunkZ;

	private boolean finished;

	public ChunkUpdateThread(World world, Chunk chunk) {
		this.world = world;
		this.chunk = chunk;
		this.chunkX = chunk.getX();
		this.chunkY = chunk.getY();
		this.chunkZ = chunk.getZ();

		this.finished = false;
	}

	public void run() {
		// TODO: Change the rendering system to only send vertex data of block faces that are visible rather than all faces to the GPU.
		// Currently the rendering system demands far too much video memory from the GPU and on gigantic sized worlds begins eating though main system RAM.
		// By sending only the vertex data for block faces visible video memory usage can be reduced significantly
		//
		// 128 bytes of vertex data per block face
		// 301,989,888 bytes (0.28 GB) of vertex data theoretically with the new method
		// 1,805,457,408 bytes (1.68 GB) of vertex data using the current method
		//
		// Approximately an 83% reduction in video memory usage can be achieved on the base worlds if this can be implemented.

		List<Float> vertexData = new ArrayList<Float>();
		List<List<Integer>> indexDataLists = new ArrayList<List<Integer>>();
		for (int i = 0; i < Block.CUBE_FACE_COUNT; i++) {
			indexDataLists.add(new ArrayList<Integer>());
		}

		int currentCubeIndex = 0;

		for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
			for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
				for (int k = 0; k < Chunk.CHUNK_SIZE; k++) {
					if (chunk.getBlock(k, j, i).getID() == 0)
						continue;

					int blockID = chunk.getBlock(k, j, i).getID();

					for (int l = 0; l < Block.CUBE_FACE_COUNT; l++) {
						int x = (int) (k + Block.CUBE_FACE_NORMALS[l * 3 + 0] + chunkX * Chunk.CHUNK_SIZE);
						int y = (int) (j + Block.CUBE_FACE_NORMALS[l * 3 + 1] + chunkY * Chunk.CHUNK_SIZE);
						int z = (int) (i + Block.CUBE_FACE_NORMALS[l * 3 + 2] + chunkZ * Chunk.CHUNK_SIZE);

						Block adjacentBlock = world.getBlock(x, y, z);

						if (adjacentBlock == null || !adjacentBlock.isOpaque()) {
							for (int m = 0; m < Block.CUBE_INDICES_PER_FACE; m++) {
								indexDataLists.get(l).add(Block.CUBE_INDICES[m] + currentCubeIndex);
							}

							currentCubeIndex += Block.CUBE_VERTICES_PER_FACE;

							for (int m = 0; m < Block.CUBE_VERTICES_PER_FACE; m++) {
								final int OFFSET = l * Block.CUBE_FLOATS_PER_FACE + m * Block.CUBE_FLOATS_PER_VERTEX;
								
								// VERTEX POSITION
								vertexData.add(Block.CUBE_VERTICES[OFFSET + 0] + k);
								vertexData.add(Block.CUBE_VERTICES[OFFSET + 1] + j);
								vertexData.add(Block.CUBE_VERTICES[OFFSET + 2] + i);
								// VERTEX TEXTURE UV
								vertexData.add(Block.CUBE_VERTICES[OFFSET + 3] + (blockID % 16) * 0.0625f);
								vertexData.add(Block.CUBE_VERTICES[OFFSET + 4] + (blockID / 16) * 0.0625f);
								// VERTEX NORMAL
								vertexData.add(Block.CUBE_VERTICES[OFFSET + 5]);
								vertexData.add(Block.CUBE_VERTICES[OFFSET + 6]);
								vertexData.add(Block.CUBE_VERTICES[OFFSET + 7]);
							}
						}
					}

					// boolean addVertices = false;
					//
					// for (int l = 0; l < Block.CUBE_FACE_COUNT; l++) {
					// int x = k + Block.CUBE_FACE_NORMALS[l * 3 + 0] + chunkX * Chunk.CHUNK_SIZE;
					// int y = j + Block.CUBE_FACE_NORMALS[l * 3 + 1] + chunkY * Chunk.CHUNK_SIZE;
					// int z = i + Block.CUBE_FACE_NORMALS[l * 3 + 2] + chunkZ * Chunk.CHUNK_SIZE;
					//
					// Block adjacentBlock = world.getBlock(x, y, z);
					//
					// if (adjacentBlock == null || !adjacentBlock.isOpaque()) {
					// for (int m = 0; m < Block.CUBE_INDICES_PER_FACE; m++) {
					// indexDataLists.get(l).add(Block.CUBE_INDICES[l * Block.CUBE_INDICES_PER_FACE + m] + currentCubeIndex * Block.CUBE_VERTEX_COUNT);
					// }
					//
					// addVertices = true;
					// }
					// }
					//
					// if (addVertices) {
					// currentCubeIndex++;
					//
					// int blockID = chunk.getBlock(k, j, i).getID();
					//
					// for (int l = 0; l < Block.CUBE_VERTEX_COUNT; l++) {
					// // VERTEX POSITION
					// vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 0] + k);
					// vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 1] + j);
					// vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 2] + i);
					// // VERTEX TEXTURE UV
					// vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 3] + (blockID % 16) * 0.0625f);
					// vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 4] + (blockID / 16) * 0.0625f);
					// // VERTEX NORMAL
					// vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 5]);
					// vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 6]);
					// vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 7]);
					// }
					// }

				}
			}
		}

		this.vertexBuffer = ByteBuffer.allocateDirect(Float.SIZE * vertexData.size()).order(ByteOrder.nativeOrder()).asFloatBuffer();
		for (Float value : vertexData) {
			vertexBuffer.put(value);
		}
		vertexBuffer.flip();

		this.indexBuffers = new IntBuffer[Block.CUBE_FACE_COUNT];
		this.bufferSizes = new int[Block.CUBE_FACE_COUNT];
		for (int i = 0; i < Block.CUBE_FACE_COUNT; i++) {
			List<Integer> indexDataList = indexDataLists.get(i);

			indexBuffers[i] = ByteBuffer.allocateDirect(Integer.SIZE * indexDataList.size()).order(ByteOrder.nativeOrder()).asIntBuffer();
			for (Integer value : indexDataList) {
				indexBuffers[i].put(value);
			}
			indexBuffers[i].flip();

			bufferSizes[i] = indexDataList.size();
		}

		finished = true;

		chunk.setUpdateThread(this);
	}

	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	public IntBuffer getIndexBuffer(int side) {
		return indexBuffers[side];
	}

	public int getBufferSize(int side) {
		return bufferSizes[side];
	}

	public boolean isFinished() {
		return finished;
	}
}
