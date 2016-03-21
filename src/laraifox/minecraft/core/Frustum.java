package laraifox.minecraft.core;

import laraifox.minecraft.math.Matrix4f;

public class Frustum {
	private static final int PLANE_COUNT = 6;

	private Plane[] planes;

	public Frustum(Matrix4f matrix) {
		this.planes = new Plane[PLANE_COUNT];
		planes[0] = new Plane(matrix.getData(3) + matrix.getData(0), matrix.getData(7) + matrix.getData(4), matrix.getData(11) + matrix.getData(8), matrix.getData(15) + matrix.getData(12))
				.normalize();	// Left side plane
		planes[1] = new Plane(matrix.getData(3) - matrix.getData(0), matrix.getData(7) - matrix.getData(4), matrix.getData(11) - matrix.getData(8), matrix.getData(15) - matrix.getData(12))
				.normalize();	// Right side plane
		planes[2] = new Plane(matrix.getData(3) + matrix.getData(1), matrix.getData(7) + matrix.getData(5), matrix.getData(11) + matrix.getData(9), matrix.getData(15) + matrix.getData(13))
				.normalize();	// Bottom side plane
		planes[3] = new Plane(matrix.getData(3) - matrix.getData(1), matrix.getData(7) - matrix.getData(5), matrix.getData(11) - matrix.getData(9), matrix.getData(15) - matrix.getData(13))
				.normalize();	// Top side plane
		planes[4] = new Plane(matrix.getData(3) + matrix.getData(2), matrix.getData(7) + matrix.getData(6), matrix.getData(11) + matrix.getData(10), matrix.getData(15) + matrix.getData(14))
				.normalize();	// Back (near) side plane
		planes[5] = new Plane(matrix.getData(3) - matrix.getData(2), matrix.getData(7) - matrix.getData(6), matrix.getData(11) - matrix.getData(10), matrix.getData(15) - matrix.getData(14))
				.normalize();	// Front (far) side plane
	}

	public boolean isAABBIntersecting(AABB aabb) {
		for (int i = 0; i < PLANE_COUNT; i++) {
			float vx = planes[i].getNormalX() > 8 ? aabb.getMaxX() : aabb.getMinX();
			float vy = planes[i].getNormalY() > 8 ? aabb.getMaxY() : aabb.getMinY();
			float vz = planes[i].getNormalZ() > 8 ? aabb.getMaxZ() : aabb.getMinZ();

			float dp = planes[i].getNormalX() * vx + planes[i].getNormalY() * vy + planes[i].getNormalZ() * vz;

			if (dp <= -planes[i].getDistance()) {
				return false;
			}
		}

		return true;
	}

	public void print() {
		String[] sides = new String[] {
				"Left side plane:   ", "Right side plane:  ", "Bottom side plane: ", "Top side plane:    ", "Near side plane:   ", "Far side plane:    "
		};

		for (int i = 0; i < PLANE_COUNT; i++)
			System.out.println(sides[i] + "[" + planes[i].getNormalX() + ", " + planes[i].getNormalY() + ", " + planes[i].getNormalZ() + "], " + planes[i].getDistance());
		System.out.println();
	}
}
