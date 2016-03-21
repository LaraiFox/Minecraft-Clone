package laraifox.minecraft.core;

public class PerspectiveProjection {
	private final float fov;
	private final float width;
	private final float height;
	private final float zNear;
	private final float zFar;

	public PerspectiveProjection(float fov, float width, float height, float zNear, float zFar) {
		super();
		this.fov = fov;
		this.width = width;
		this.height = height;
		this.zNear = zNear;
		this.zFar = zFar;
	}

	public float getFOV() {
		return fov;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getZNear() {
		return zNear;
	}

	public float getZFar() {
		return zFar;
	}
}
