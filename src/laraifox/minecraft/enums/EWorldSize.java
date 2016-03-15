package laraifox.minecraft.enums;

public enum EWorldSize {
	NONE(0), 
	DEBUG(1), 
	TINY(2), 
	SMALL(4), 
	MEDIUM(8), 
	LARGE(16), 
	HUGE(32), 
	GIGANTIC(64);

	private final int WORLD_SIZE;

	private EWorldSize(int size) {
		this.WORLD_SIZE = size;
	}

	public int getSize() {
		return WORLD_SIZE;
	}
}
