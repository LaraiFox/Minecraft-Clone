package laraifox.minecraft.core;

import laraifox.minecraft.math.Vector3f;

public class Plane {
	private Vector3f normal;
	private float distance;

	public Plane(float nx, float ny, float nz, float distance) {
		this.distance = distance;
		this.normal = new Vector3f(nx, ny, nz);
	}

	public Plane(Vector3f normal, float distance) {
		this.distance = distance;
		this.normal = normal;
	}

	public Plane normalize() {
		normal.normalize();

		return this;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public float getNormalX() {
		return normal.getX();
	}

	public float getNormalY() {
		return normal.getY();
	}

	public float getNormalZ() {
		return normal.getZ();
	}

	public float getDistance() {
		return distance;
	}
}
