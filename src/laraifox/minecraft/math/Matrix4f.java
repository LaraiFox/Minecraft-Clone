package laraifox.minecraft.math;

public class Matrix4f {
	private float[][] data;

	public Matrix4f() {
		this.data = new float[4][4];
	}

	public Matrix4f(Matrix4f matrix) {
		this.data = matrix.data;
	}

	public Vector3f multiply(Vector3f vector) {
		Vector3f result = new Vector3f();
		result.setX(data[0][0] * vector.getX() + data[1][0] * vector.getY() + data[2][0] * vector.getZ() + data[3][0]);
		result.setY(data[0][1] * vector.getX() + data[1][1] * vector.getY() + data[2][1] * vector.getZ() + data[3][1]);
		result.setZ(data[0][2] * vector.getX() + data[1][2] * vector.getY() + data[2][2] * vector.getZ() + data[3][2]);

		return result;
	}

	public Matrix4f multiply(Matrix4f matrix) {
		Matrix4f result = new Matrix4f();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.setDataAt(i, j, data[i][0] * matrix.getData(0, j) + data[i][1] * matrix.getData(1, j) + data[i][2] * matrix.getData(2, j) + data[i][3] * matrix.getData(3, j));
			}
		}

		return result;
	}

	public Matrix4f transpose() {
		Matrix4f result = new Matrix4f();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.setDataAt(i, j, data[j][i]);
			}
		}

		return result;
	}

	public String toString() {
		return new String("{ [" + data[0][0] + ", " + data[0][1] + ", " + data[0][2] + ", " + data[0][3] + ", " + "]\n" + //
			"  [" + data[1][0] + ", " + data[1][1] + ", " + data[1][2] + ", " + data[1][3] + ", " + "]\n" + //
			"  [" + data[2][0] + ", " + data[2][1] + ", " + data[2][2] + ", " + data[2][3] + ", " + "]\n" + //
			"  [" + data[3][0] + ", " + data[3][1] + ", " + data[3][2] + ", " + data[3][3] + ", " + "] }");
	}

	public float[][] getData() {
		float[][] result = new float[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result[i][j] = data[i][j];
			}
		}

		return result;
	}

	public float getData(int x, int y) {
		return data[x][y];
	}

	public float getData(int i) {
		return data[i % 4][i / 4];
	}

	public void setData(float[][] data) {
		this.data = data;
	}

	public void setDataAt(int x, int y, float value) {
		this.data[x][y] = value;
	}

	public static Matrix4f Identity() {
		Matrix4f result = new Matrix4f();

		result.data[0][0] = 1;
		result.data[0][1] = 0;
		result.data[0][2] = 0;
		result.data[0][3] = 0;

		result.data[1][0] = 0;
		result.data[1][1] = 1;
		result.data[1][2] = 0;
		result.data[1][3] = 0;

		result.data[2][0] = 0;
		result.data[2][1] = 0;
		result.data[2][2] = 1;
		result.data[2][3] = 0;

		result.data[3][0] = 0;
		result.data[3][1] = 0;
		result.data[3][2] = 0;
		result.data[3][3] = 1;

		return result;
	}

	public static Matrix4f Translation(Vector3f translation) {
		return Matrix4f.Translation(translation.getX(), translation.getY(), translation.getZ());
	}

	public static Matrix4f Translation(float x, float y, float z) {
		Matrix4f result = new Matrix4f();

		result.data[0][0] = 1;
		result.data[0][1] = 0;
		result.data[0][2] = 0;
		result.data[0][3] = x;

		result.data[1][0] = 0;
		result.data[1][1] = 1;
		result.data[1][2] = 0;
		result.data[1][3] = y;

		result.data[2][0] = 0;
		result.data[2][1] = 0;
		result.data[2][2] = 1;
		result.data[2][3] = z;

		result.data[3][0] = 0;
		result.data[3][1] = 0;
		result.data[3][2] = 0;
		result.data[3][3] = 1;

		return result;
	}

	public static Matrix4f Rotation(float x, float y, float z) {
		Matrix4f result = new Matrix4f();

		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();

		x = (float) Math.toRadians(x);
		y = (float) Math.toRadians(y);
		z = (float) Math.toRadians(z);

		// Rotation around the Z axis
		rz.data[0][0] = (float) Math.cos(z);
		rz.data[0][1] = (float) -Math.sin(z);
		rz.data[0][2] = 0;
		rz.data[0][3] = 0;

		rz.data[1][0] = (float) Math.sin(z);
		rz.data[1][1] = (float) Math.cos(z);
		rz.data[1][2] = 0;
		rz.data[1][3] = 0;

		rz.data[2][0] = 0;
		rz.data[2][1] = 0;
		rz.data[2][2] = 1;
		rz.data[2][3] = 0;

		rz.data[3][0] = 0;
		rz.data[3][1] = 0;
		rz.data[3][2] = 0;
		rz.data[3][3] = 1;

		// Rotation around the X axis
		rx.data[0][0] = 1;
		rx.data[0][1] = 0;
		rx.data[0][2] = 0;
		rx.data[0][3] = 0;

		rx.data[1][0] = 0;
		rx.data[1][1] = (float) Math.cos(x);
		rx.data[1][2] = (float) -Math.sin(x);
		rx.data[1][3] = 0;

		rx.data[2][0] = 0;
		rx.data[2][1] = (float) Math.sin(x);
		rx.data[2][2] = (float) Math.cos(x);
		rx.data[2][3] = 0;

		rx.data[3][0] = 0;
		rx.data[3][1] = 0;
		rx.data[3][2] = 0;
		rx.data[3][3] = 1;

		// Rotation around the Y axis
		ry.data[0][0] = (float) Math.cos(y);
		ry.data[0][1] = 0;
		ry.data[0][2] = (float) -Math.sin(y);
		ry.data[0][3] = 0;

		ry.data[1][0] = 0;
		ry.data[1][1] = 1;
		ry.data[1][2] = 0;
		ry.data[1][3] = 0;

		ry.data[2][0] = (float) Math.sin(y);
		ry.data[2][1] = 0;
		ry.data[2][2] = (float) Math.cos(y);
		ry.data[2][3] = 0;

		ry.data[3][0] = 0;
		ry.data[3][1] = 0;
		ry.data[3][2] = 0;
		ry.data[3][3] = 1;

		result = rz.multiply(ry.multiply(rx));

		return result;
	}

	public static Matrix4f Rotation(Vector3f forward, Vector3f upward) {
		Vector3f zAxis = Vector3f.normalize(forward);
		Vector3f xAxis = Vector3f.normalize(upward).cross(zAxis);
		Vector3f yAxis = Vector3f.cross(zAxis, xAxis).normalize();

		return Matrix4f.Rotation(zAxis, yAxis, xAxis);
	}

	public static Matrix4f Rotation(Vector3f forward, Vector3f upward, Vector3f right) {
		Matrix4f result = new Matrix4f();

		Vector3f xAxis = Vector3f.normalize(right);
		Vector3f yAxis = Vector3f.normalize(upward);
		Vector3f zAxis = Vector3f.normalize(forward);

		result.data[0][0] = xAxis.getX();
		result.data[0][1] = xAxis.getY();
		result.data[0][2] = xAxis.getZ();
		result.data[0][3] = 0;

		result.data[1][0] = yAxis.getX();
		result.data[1][1] = yAxis.getY();
		result.data[1][2] = yAxis.getZ();
		result.data[1][3] = 0;

		result.data[2][0] = zAxis.getX();
		result.data[2][1] = zAxis.getY();
		result.data[2][2] = zAxis.getZ();
		result.data[2][3] = 0;

		result.data[3][0] = 0;
		result.data[3][1] = 0;
		result.data[3][2] = 0;
		result.data[3][3] = 1;

		return result;
	}

	public static Matrix4f ViewRotation(Vector3f forward, Vector3f upward, Vector3f right) {
		Matrix4f result = new Matrix4f();

		Vector3f xAxis = Vector3f.normalize(right);
		Vector3f yAxis = Vector3f.normalize(upward);
		Vector3f zAxis = Vector3f.normalize(forward);

		result.data[0][0] = xAxis.getX();
		result.data[0][1] = yAxis.getX();
		result.data[0][2] = zAxis.getX();
		result.data[0][3] = 0;

		result.data[1][0] = xAxis.getY();
		result.data[1][1] = yAxis.getY();
		result.data[1][2] = zAxis.getY();
		result.data[1][3] = 0;

		result.data[2][0] = xAxis.getZ();
		result.data[2][1] = yAxis.getZ();
		result.data[2][2] = zAxis.getZ();
		result.data[2][3] = 0;

		result.data[3][0] = 0;
		result.data[3][1] = 0;
		result.data[3][2] = 0;
		result.data[3][3] = 1;

		return result;
	}

	public static Matrix4f Scale(float x, float y, float z) {
		Matrix4f result = new Matrix4f();

		result.data[0][0] = x;
		result.data[0][1] = 0;
		result.data[0][2] = 0;
		result.data[0][3] = 0;

		result.data[1][0] = 0;
		result.data[1][1] = y;
		result.data[1][2] = 0;
		result.data[1][3] = 0;

		result.data[2][0] = 0;
		result.data[2][1] = 0;
		result.data[2][2] = z;
		result.data[2][3] = 0;

		result.data[3][0] = 0;
		result.data[3][1] = 0;
		result.data[3][2] = 0;
		result.data[3][3] = 1;

		return result;
	}

	public static Matrix4f Projection(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f result = new Matrix4f();

		float xRange = right - left;
		float yRange = top - bottom;
		float zRange = far - near;

		result.data[0][0] = 2.0f / xRange;
		result.data[0][1] = 0;
		result.data[0][2] = 0;
		result.data[0][3] = 0;

		result.data[1][0] = 0;
		result.data[1][1] = 2.0f / yRange;
		result.data[1][2] = 0;
		result.data[1][3] = 0;

		result.data[2][0] = 0;
		result.data[2][1] = 0;
		result.data[2][2] = -2.0f / zRange;
		result.data[2][3] = 0;

		result.data[3][0] = -((right + left) / xRange);
		result.data[3][1] = -((top + bottom) / yRange);
		result.data[3][2] = -((far + near) / zRange);
		result.data[3][3] = 1;

		return result;
	}

	public static Matrix4f Projection(float fov, float width, float height, float zNear, float zFar) {
		Matrix4f result = new Matrix4f();

		float aspect = width / height;
		float tanHalfFOV = (float) Math.tan(Math.toRadians(fov / 2.0f));
		float zRange = zNear - zFar;

		result.data[0][0] = 1.0f / (tanHalfFOV * aspect);
		result.data[0][1] = 0;
		result.data[0][2] = 0;
		result.data[0][3] = 0;

		result.data[1][0] = 0;
		result.data[1][1] = 1.0f / tanHalfFOV;
		result.data[1][2] = 0;
		result.data[1][3] = 0;

		result.data[2][0] = 0;
		result.data[2][1] = 0;
		result.data[2][2] = (-zNear - zFar) / zRange;
		result.data[2][3] = (2.0f * zFar * zNear) / zRange;

		result.data[3][0] = 0;
		result.data[3][1] = 0;
		result.data[3][2] = 1;
		result.data[3][3] = 0;

		//		 float aspect = width / height;
		//		 float tanHalfFOV = (float) Math.tan(Math.toRadians(fov / 2.0f));
		//		 float zRange = zNear - zFar;
		//		
		//		 result.data[0][0] = 1.0f / (tanHalfFOV * aspect);
		//		 result.data[0][1] = 0;
		//		 result.data[0][2] = 0;
		//		 result.data[0][3] = 0;
		//		
		//		 result.data[1][0] = 0;
		//		 result.data[1][1] = 1.0f / tanHalfFOV;
		//		 result.data[1][2] = 0;
		//		 result.data[1][3] = 0;
		//		
		//		 result.data[2][0] = 0;
		//		 result.data[2][1] = 0;
		//		 result.data[2][2] = (-zNear - zFar) / zRange;
		//		 result.data[2][3] = 1;
		//		
		//		 result.data[3][0] = 0;
		//		 result.data[3][1] = 0;
		//		 result.data[3][2] = (2.0f * zFar * zNear) / zRange;
		//		 result.data[3][3] = 0;

		return result;
	}
}
