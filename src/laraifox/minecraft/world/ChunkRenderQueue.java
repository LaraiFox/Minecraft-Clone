package laraifox.minecraft.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import laraifox.minecraft.core.Shader;
import laraifox.minecraft.math.Vector2f;

public class ChunkRenderQueue {
	private static Map<Vector2f, List<Chunk>> renderQueue = new HashMap<Vector2f, List<Chunk>>();

	private ChunkRenderQueue() {

	}

	public static void enqueueChunk(Chunk chunk) {
		Vector2f chunkXZ = new Vector2f(chunk.getX(), chunk.getZ());

		if (!renderQueue.containsKey(chunkXZ)) {
			renderQueue.put(chunkXZ, new ArrayList<Chunk>());
		}

		List<Chunk> stack = renderQueue.get(chunkXZ);
		if (!stack.contains(chunk)) {
			stack.add(chunk);
		}
	}

	public static void render(Shader shader) {
		for (Vector2f key : renderQueue.keySet()) {
			shader.setUniform("stackPosition", Vector2f.scale(key, Chunk.CHUNK_SIZE));

			List<Chunk> stack = renderQueue.get(key);
			for (Chunk chunk : stack) {
				shader.setUniform("chunkHeight", (float) (chunk.getY() * Chunk.CHUNK_SIZE));

				chunk.render();
			}
		}

		renderQueue.clear();
	}
}
