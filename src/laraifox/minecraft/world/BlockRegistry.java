package laraifox.minecraft.world;

import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {
	private static final Map<Integer, Block> blockRegistry = new HashMap<Integer, Block>();

	public static void registerBlock(Block block) {
		if (!blockRegistry.containsKey(block.getID())) {
			blockRegistry.put(block.getID(), block);
		}
	}

	public static int getBlockCount() {
		return blockRegistry.size();
	}

	public static Block getBlock(int id) {
		return blockRegistry.get(id);
	}

	public static String getBlockName(int id) {
		return blockRegistry.get(id).getName();
	}

	public static boolean isBlockOpaque(int id) {
		return blockRegistry.get(id).isOpaque();
	}
}
