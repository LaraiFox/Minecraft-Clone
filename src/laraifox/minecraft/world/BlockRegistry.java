package laraifox.minecraft.world;

import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {
	private static final Map<Integer, Block> blockRegistry = new HashMap<Integer, Block>();

	public static void registerBlock(int id, Block block) {
		if (!blockRegistry.containsKey(id)) {
			blockRegistry.put(id, block);
		}
	}

	public static Block getBlock(int id) {
		return blockRegistry.get(id);
	}
}
