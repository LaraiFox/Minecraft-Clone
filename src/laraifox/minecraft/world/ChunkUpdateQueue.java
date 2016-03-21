package laraifox.minecraft.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkUpdateQueue {
	private static final int UPDATE_THREAD_COUNT = 64;

	private static Map<Chunk, Integer> processingList = new HashMap<Chunk, Integer>();

	private static List<Chunk> updateQueue = new ArrayList<Chunk>();

	private static ChunkUpdateThread[] updateThreads = new ChunkUpdateThread[UPDATE_THREAD_COUNT];

	private static List<Integer> openUpdateThreads = new ArrayList<Integer>();

	private static List<Chunk> sortedList = new ArrayList<Chunk>();
	private static List<Integer> indices = new ArrayList<Integer>();

	static {
		for (int i = 0; i < UPDATE_THREAD_COUNT; i++) {
			openUpdateThreads.add(i);
		}
	}

	private ChunkUpdateQueue() {

	}

	public static void enqueueChunk(Chunk chunk) {
		if (!updateQueue.contains(chunk)) {
			updateQueue.add(chunk);
		}
	}

	public static void update(World world) {
		if (updateQueue.size() > 0 && openUpdateThreads.size() > 0) {
			while (updateQueue.size() > 0 && openUpdateThreads.size() > 0) {
				Integer updateThreadID = openUpdateThreads.remove(0);
				Chunk currentChunk = updateQueue.remove(0);
				//			updateQueue.remove(currentChunk);

				processingList.put(currentChunk, updateThreadID);

				updateThreads[updateThreadID] = new ChunkUpdateThread(world, currentChunk);
				updateThreads[updateThreadID].start();
			}
		} else if (openUpdateThreads.size() == UPDATE_THREAD_COUNT) {
			for (int i = 0; i < UPDATE_THREAD_COUNT; i++) {
				updateThreads[i] = null;
			}
		}
	}

	//	private static void updateSortedList(World world) {
	//		sortedList.clear();
	//		indices.clear();
	//
	//		int playerChunkX = world.getPlayerChunkX();
	//		int playerChunkZ = world.getPlayerChunkZ();
	//
	//		sortedList.add(updateQueue.get(0));
	//		indices.add((updateQueue.get(0).getX() - playerChunkX) + (updateQueue.get(0).getZ() - playerChunkZ));
	//
	//		for (int i = 1; i < updateQueue.size(); i++) {
	//			int distance = (updateQueue.get(i).getX() - playerChunkX) + (updateQueue.get(i).getZ() - playerChunkZ);
	//
	//			for (int j = 0; j < indices.size(); j++) {
	//				if (distance >= indices.get(j)) {
	//					indices.add(j, distance);
	//					sortedList.add(j, updateQueue.get(i));
	//				}
	//			}
	//		}
	//	}

	public static void releaseUpdateThread(Chunk chunk) {
		openUpdateThreads.add(processingList.remove(chunk));
	}

	public static int getChunkUpdatesQueued() {
		return updateQueue.size();
	}

	public static int getChunkUpdatesProcessing() {
		return processingList.size();
	}

}
