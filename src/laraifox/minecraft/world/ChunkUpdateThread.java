package laraifox.minecraft.world;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ChunkUpdateThread extends Thread {
	//	private static int test = 0;

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
		//		System.out.println("Thread #" + test++ + " Running...");

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

					boolean addVertices = false;

					for (int l = 0; l < Block.CUBE_FACE_COUNT; l++) {
						int x = k + Block.CUBE_FACE_NORMALS[l * 3 + 0] + chunkX * Chunk.CHUNK_SIZE;
						int y = j + Block.CUBE_FACE_NORMALS[l * 3 + 1] + chunkY * Chunk.CHUNK_SIZE;
						int z = i + Block.CUBE_FACE_NORMALS[l * 3 + 2] + chunkZ * Chunk.CHUNK_SIZE;

						Block adjacentBlock = world.getBlock(x, y, z);
						
						if (adjacentBlock == null || !adjacentBlock.isOpaque()) {
							for (int m = 0; m < Block.CUBE_INDICES_PER_FACE; m++) {
								indexDataLists.get(l).add(Block.CUBE_INDICES[l * Block.CUBE_INDICES_PER_FACE + m] + currentCubeIndex * Block.CUBE_VERTEX_COUNT);
							}
							
							addVertices = true;
						}
					}

					if (addVertices) {
						currentCubeIndex++;

						for (int l = 0; l < Block.CUBE_VERTEX_COUNT; l++) {
							// VERTEX POSITION
							vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 0] + k);
							vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 1] + j);
							vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 2] + i);
							// VERTEX TEXTURE UV
							vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 3]);
							vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 4]);
							// VERTEX NORMAL
							vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 5]);
							vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 6]);
							vertexData.add(Block.CUBE_VERTICES[l * Block.CUBE_FLOATS_PER_VERTEX + 7]);
						}
					}

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
