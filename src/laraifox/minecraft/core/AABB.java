package laraifox.minecraft.core;

import laraifox.minecraft.math.Vector3f;

public class AABB {
	private Vector3f min;
	private Vector3f max;

	public AABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.min = new Vector3f(minX, minY, minZ);
		this.max = new Vector3f(maxX, maxY, maxZ);
	}

	public AABB(Vector3f min, Vector3f max) {
		this.min = min;
		this.max = max;
	}

	public float getMinX() {
		return min.getX();
	}

	public float getMinY() {
		return min.getY();
	}

	public float getMinZ() {
		return min.getZ();
	}

	public float getMaxX() {
		return max.getX();
	}

	public float getMaxY() {
		return max.getY();
	}

	public float getMaxZ() {
		return max.getZ();
	}
}
