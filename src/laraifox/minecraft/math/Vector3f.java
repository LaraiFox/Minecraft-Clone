package laraifox.minecraft.math;

/**
 * A three coordinate vector object with methods for basic math operations as well as vector specific operation. Coordinates are stored as three private float
 * components labeled x, y and z. Getter and setter methods can be used to access the components if needed.
 * 
 * @author Larai Fox
 * 
 */
public class Vector3f {
	/***
	 * Numeric index of the X component of the vector.
	 */
	public static final int COMPONENT_INDEX_X = 0;
	/***
	 * Numeric index of the Y component of the vector.
	 */
	public static final int COMPONENT_INDEX_Y = 1;
	/***
	 * Numeric index of the Z component of the vector.
	 */
	public static final int COMPONENT_INDEX_Z = 2;

	/***
	 * The number of components in the vector.
	 */
	public static final int COMPONENT_COUNT = 3;
	/***
	 * The total size in bytes of all components in the vector.
	 */
	public static final int BYTE_COUNT = COMPONENT_COUNT * 4;

	private float x, y, z;

	/**
	 * Constructs a new vector setting all components to zero.
	 */
	public Vector3f() {
		this(0.0f, 0.0f, 0.0f);
	}

	/**
	 * Constructs a new vector setting all components to value.
	 * 
	 * @param value
	 *            - the value used to set all components of the vector
	 */
	public Vector3f(float value) {
		this(value, value, value);
	}

	/**
	 * Constructs a new vector and sets the X, Y and Z components to the X, Y and Z parameters respectively.
	 * 
	 * @param x
	 *            - the X coordinate of the vector
	 * @param y
	 *            - the Y coordinate of the vector
	 * @param z
	 *            - the Z coordinate of the vector
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(Vector2f vector) {
		this(vector.getX(), vector.getY(), 0.0f);
	}
	
	public Vector3f(Vector2f vector, float z) {
		this(vector.getX(), vector.getY(), z);
	}

	/**
	 * Constructs a new vector that is a copy of the vector parameter with all components equal to those of the vector parameter. Note that further changes to
	 * this newly constructed vector will not have any effect on the original vector that was copied.
	 * 
	 * @param vector
	 *            - the vector to be copied
	 */
	public Vector3f(Vector3f vector) {
		this(vector.getX(), vector.getY(), vector.getZ());
	}

	/***
	 * Creates and returns a new vector of length zero.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Zero() {
		return new Vector3f(0.0f, 0.0f, 0.0f);
	}

	/***
	 * Creates and returns a new vector with all components set to one.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f One() {
		return new Vector3f(1.0f, 1.0f, 1.0f);
	}

	/***
	 * Creates and returns a new vector of length one pointing in the negative X direction.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Left() {
		return new Vector3f(-1.0f, 0.0f, 0.0f);
	}

	/***
	 * Creates and returns a new vector of length value pointing in the negative X direction. If value is negative the new vector will point in the opposite
	 * direction.
	 * 
	 * @param value
	 *            - the length of the returned vector.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Left(float value) {
		return new Vector3f(-value, 0.0f, 0.0f);
	}

	/***
	 * Creates and returns a new vector of length one pointing in the positive X direction.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Right() {
		return new Vector3f(1.0f, 0.0f, 0.0f);
	}

	/***
	 * Creates and returns a new vector of length value pointing in the positive X direction. If value is negative the new vector will point in the opposite
	 * direction.
	 * 
	 * @param value
	 *            - the length of the returned vector.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Right(float value) {
		return new Vector3f(value, 0.0f, 0.0f);
	}

	/***
	 * Creates and returns a new vector of length one pointing in the negative Y direction.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Down() {
		return new Vector3f(0.0f, -1.0f, 0.0f);
	}

	/***
	 * Creates and returns a new vector of length value pointing in the negative Y direction. If value is negative the new vector will point in the opposite
	 * direction.
	 * 
	 * @param value
	 *            - the length of the returned vector.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Down(float value) {
		return new Vector3f(0.0f, -value, 0.0f);
	}

	/***
	 * Creates and returns a new vector of length one pointing in the positive Y direction.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Up() {
		return new Vector3f(0.0f, 1.0f, 0.0f);
	}

	/***
	 * Creates and returns a new vector of length value pointing in the positive Y direction. If value is negative the new vector will point in the opposite
	 * direction.
	 * 
	 * @param value
	 *            - the length of the returned vector.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Up(float value) {
		return new Vector3f(0.0f, value, 0.0f);
	}

	/***
	 * Creates and returns a new vector of length one pointing in the negative Z direction.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Back() {
		return new Vector3f(0.0f, 0.0f, -1.0f);
	}

	/***
	 * Creates and returns a new vector of length value pointing in the negative Z direction. If value is negative the new vector will point in the opposite
	 * direction.
	 * 
	 * @param value
	 *            - the length of the returned vector.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Back(float value) {
		return new Vector3f(0.0f, 0.0f, -value);
	}

	/***
	 * Creates and returns a new vector of length one pointing in the positive Z direction.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Forward() {
		return new Vector3f(0.0f, 0.0f, 1.0f);
	}

	/***
	 * Creates and returns a new vector of length value pointing in the positive Z direction. If value is negative the new vector will point in the opposite
	 * direction.
	 * 
	 * @param value
	 *            - the length of the returned vector.
	 * 
	 * @return the newly created vector
	 */
	public static Vector3f Forward(float value) {
		return new Vector3f(0.0f, 0.0f, value);
	}

	/**
	 * Adds two vectors together in the same way that the non-static {@link #add(Vector3f)} method does however this static version creates a new vector to
	 * store the result and also does not change the values stored in the left or right parameters.
	 * 
	 * @param left
	 *            - the vector on the left side of the equation
	 * @param right
	 *            - the vector on the right side of the equation
	 * @return a new vector with the value of left + right
	 */
	public static Vector3f add(Vector3f left, Vector3f right) {
		return new Vector3f(left.getX() + right.getX(), left.getY() + right.getY(), left.getZ() + right.getZ());
	}

	/**
	 * Subtracts one vector from another in the same way that the non-static {@link #subtract(Vector3f)} method does however this static version creates a new
	 * vector to store the result and also does not change the values stored in the left or right parameters.
	 * 
	 * @param left
	 *            - the vector on the left side of the equation
	 * @param right
	 *            - the vector on the right side of the equation
	 * @return a new vector with the value of left - right
	 */
	public static Vector3f subtract(Vector3f left, Vector3f right) {
		return new Vector3f(left.getX() - right.getX(), left.getY() - right.getY(), left.getZ() - right.getZ());
	}

	/**
	 * Multiplies two vectors together in the same way that the non-static {@link #multiply(Vector3f)} method does however this static version creates a new
	 * vector to store the result and also does not change the values stored in the left or right parameters.
	 * 
	 * @param left
	 *            - the vector on the left side of the equation
	 * @param right
	 *            - the vector on the right side of the equation
	 * @return a new vector with the value of left * right
	 */
	public static Vector3f multiply(Vector3f left, Vector3f right) {
		return new Vector3f(left.getX() * right.getX(), left.getY() * right.getY(), left.getZ() * right.getZ());
	}

	/**
	 * Divides one vector by another in the same way that the non-static {@link #divide(Vector3f)} method does however this static version creates a new vector
	 * to store the result and also does not change the values stored in the left or right parameters.
	 * 
	 * @param left
	 *            - the vector on the left side of the equation
	 * @param right
	 *            - the vector on the right side of the equation
	 * @return a new vector with the value of left / right
	 */
	public static Vector3f divide(Vector3f left, Vector3f right) {
		return new Vector3f(left.getX() / right.getX(), left.getY() / right.getY(), left.getZ() / right.getZ());
	}

	public static Vector3f abs(Vector3f vector) {
		return new Vector3f(vector).abs();
	}

	public static Vector3f ceil(Vector3f vector) {
		return new Vector3f(vector).ceil();
	}

	public Vector3f clamp(Vector3f vector, Vector3f min, Vector3f max) {
		return new Vector3f(vector).min(max).max(min);
	}

	public Vector3f clamp(Vector3f vector, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		return new Vector3f(vector).min(maxX, maxY, maxZ).max(minX, minY, minZ);
	}

	public Vector3f clamp(Vector3f vector, float min, float max) {
		return new Vector3f(vector).min(max).max(min);
	}

	public static Vector3f clampLength(Vector3f vector, float length) {
		return new Vector3f(vector).clampLength(length);
	}

	public static Vector3f cross(Vector3f left, Vector3f right) {
		return new Vector3f(left).cross(right);
	}

	public static Vector3f floor(Vector3f vector) {
		return new Vector3f(vector).floor();
	}

	public static Vector3f lerp(Vector3f vector, Vector3f destination, float value) {
		return new Vector3f(vector).lerp(destination, value);
	}

	public static Vector3f max(Vector3f a, Vector3f b) {
		return new Vector3f(a).max(b);
	}

	public static Vector3f max(Vector3f a, float x, float y, float z) {
		return new Vector3f(a).max(x, y, z);
	}

	public static Vector3f max(Vector3f a, float value) {
		return new Vector3f(a).max(value);
	}

	public static Vector3f min(Vector3f a, Vector3f b) {
		return new Vector3f(a).min(b);
	}

	public static Vector3f min(Vector3f a, float x, float y, float z) {
		return new Vector3f(a).min(x, y, z);
	}

	public static Vector3f min(Vector3f a, float value) {
		return new Vector3f(a).min(value);
	}

	public static Vector3f moveTowards(Vector3f vector, Vector3f destination, float value) {
		return new Vector3f(vector).moveTowards(destination, value);
	}

	public static Vector3f negate(Vector3f vector) {
		return new Vector3f(vector).negate();
	}

	public static Vector3f nlerp(Vector3f vector, Vector3f destination, float value) {
		return new Vector3f(vector).nlerp(destination, value);
	}

	public static Vector3f normalize(Vector3f vector) {
		return new Vector3f(vector).normalize();
	}

	public static Vector3f orthoNormalize(Vector3f vector, Vector3f normal) {
		return new Vector3f(vector).orthoNormalize(normal);
	}

	public static Vector3f projectToVector(Vector3f a, Vector3f b) {
		return new Vector3f(a).projectToVector(b);
	}

	public static Vector3f projectToPlane(Vector3f vector, Vector3f normal) {
		return new Vector3f(vector).projectToPlane(normal);
	}

	public static Vector3f reflect(Vector3f vector, Vector3f normal) {
		return new Vector3f(vector).reflect(normal);
	}

	public static Vector3f rotate(Vector3f vector, Vector3f axis, float angle) {
		return new Vector3f(vector).rotate(axis, angle);
	}

	public static Vector3f rotate(Vector3f vector, float x, float y, float z, float angle) {
		return new Vector3f(vector).rotate(x, y, z, angle);
	}

	public static Vector3f rotate(Vector3f vector, Quaternion quaternion) {
		return new Vector3f(vector).rotate(quaternion);
	}

	public static Vector3f rotateTowards(Vector3f vector, Vector3f destination, float value) {
		return new Vector3f(vector).rotateTowards(destination, value);
	}

	public static Vector3f round(Vector3f vector) {
		return new Vector3f(vector).round();
	}

	public static Vector3f scale(Vector3f vector, float scalar) {
		return new Vector3f(vector).scale(scalar);
	}

	public static Vector3f slerp(Vector3f vector, Vector3f destination, float value) {
		return new Vector3f(vector).slerp(destination, value);
	}

	// public static Vector3f transform(Vector3f vector, Matrix4f matrix) {
	// // TODO: Add functionality to this method;
	//
	// return new Vector3f(vector).transform(matrix);
	// }

	public static float angle(Vector3f a, Vector3f b) {
		return a.angle(b);
	}

	public static float distance(Vector3f a, Vector3f b) {
		return a.distance(b);
	}

	public static float distanceSq(Vector3f a, Vector3f b) {
		return a.distanceSq(b);
	}

	public static float dot(Vector3f left, Vector3f right) {
		return left.dot(right);
	}

	public static float length(Vector3f vector) {
		return vector.length();
	}

	public static float lengthSq(Vector3f vector) {
		return vector.lengthSq();
	}

	/**
	 * Adds each component of the vector parameter to each component of the current vector and returns the modified vector.
	 * 
	 * @param vector
	 *            - the vector to add to the current vector
	 * @return this vector after being modified
	 */
	public Vector3f add(Vector3f vector) {
		return this.add(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	/**
	 * Subtracts each component of the vector parameter from each component of the current vector and returns the modified vector.
	 * 
	 * @param vector
	 *            - the vector to subtract from the current vector
	 * @return this vector after being modified
	 */
	public Vector3f subtract(Vector3f vector) {
		return this.subtract(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f subtract(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;

		return this;
	}

	/**
	 * Multiplies each component of the current vector with each component of the vector parameter and returns the modified vector.
	 * 
	 * @param vector
	 *            - the vector to multiply the current vector by
	 * @return this vector after being modified
	 */
	public Vector3f multiply(Vector3f vector) {
		return this.multiply(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f multiply(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;

		return this;
	}

	/**
	 * Divides each component of the current vector by each component of the vector parameter and returns the modified vector.
	 * 
	 * @param vector
	 *            - the vector to divide the current vector by
	 * @return this vector after being modified
	 */
	public Vector3f divide(Vector3f vector) {
		return this.divide(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f divide(float x, float y, float z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;

		return this;
	}

	public Vector3f abs() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
		this.z = Math.abs(this.z);

		return this;
	}

	public Vector3f ceil() {
		this.x = (float) Math.ceil(this.x);
		this.y = (float) Math.ceil(this.y);
		this.z = (float) Math.ceil(this.z);

		return this;
	}

	public Vector3f clamp(Vector3f min, Vector3f max) {
		return this.min(max).max(min);
	}

	public Vector3f clamp(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		return this.min(maxX, maxY, maxZ).max(minX, minY, minZ);
	}

	public Vector3f clamp(float min, float max) {
		return this.min(max).max(min);
	}

	public Vector3f clampLength(float clampLength) {
		float length = this.length();
		if (length > clampLength) {
			float scalar = clampLength / length;

			return this.scale(scalar);
		}

		return this;
	}

	public Vector3f cross(Vector3f vector) {
		float x_ = this.getY() * vector.getZ() - this.getZ() * vector.getY();
		float y_ = this.getZ() * vector.getX() - this.getX() * vector.getZ();
		float z_ = this.getX() * vector.getY() - this.getY() * vector.getX();

		this.x = x_;
		this.y = y_;
		this.z = z_;

		return this;
	}

	public Vector3f floor() {
		this.x = (float) Math.floor(this.x);
		this.y = (float) Math.floor(this.y);
		this.z = (float) Math.floor(this.z);

		return this;
	}

	public Vector3f lerp(Vector3f vector, float value) {
		return this.add(Vector3f.subtract(vector, this).scale(value));
	}

	public Vector3f max(Vector3f vector) {
		this.x = Math.max(this.x, vector.getX());
		this.y = Math.max(this.y, vector.getY());
		this.z = Math.max(this.z, vector.getZ());

		return this;
	}

	public Vector3f max(float x, float y, float z) {
		this.x = Math.max(this.x, x);
		this.y = Math.max(this.y, y);
		this.z = Math.max(this.z, z);

		return this;
	}

	public Vector3f max(float value) {
		this.x = Math.max(this.x, value);
		this.y = Math.max(this.y, value);
		this.z = Math.max(this.z, value);

		return this;
	}

	public Vector3f min(Vector3f vector) {
		this.x = Math.min(this.x, vector.getX());
		this.y = Math.min(this.y, vector.getY());
		this.z = Math.min(this.z, vector.getZ());

		return this;
	}

	public Vector3f min(float x, float y, float z) {
		this.x = Math.min(this.x, x);
		this.y = Math.min(this.y, y);
		this.z = Math.min(this.z, z);

		return this;
	}

	public Vector3f min(float value) {
		this.x = Math.min(this.x, value);
		this.y = Math.min(this.y, value);
		this.z = Math.min(this.z, value);

		return this;
	}

	public Vector3f moveTowards(Vector3f vector, float value) {
		if (this.distance(vector) <= value) {
			this.set(vector);
		} else {
			this.add(Vector3f.subtract(vector, this).normalize().scale(value));
		}

		return this;
	}

	public Vector3f negate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;

		return this;
	}

	public Vector3f nlerp(Vector3f vector, float value) {
		return this.lerp(vector, value).normalize();
	}

	public Vector3f normalize() {
		if (this.isZero())
			return this;

		float length = this.length();

		this.x /= length;
		this.y /= length;
		this.z /= length;

		return this;
	}

	public Vector3f orthoNormalize(Vector3f normal) {
		return projectToPlane(normal).normalize();
	}

	public Vector3f projectToVector(Vector3f vector) {
		Vector3f normal = Vector3f.normalize(vector);

		return this.set(Vector3f.scale(normal, this.dot(normal)));
	}

	public Vector3f projectToPlane(Vector3f normal) {
		return this.subtract(Vector3f.projectToVector(this, normal));
	}

	public Vector3f reflect(Vector3f normal) {
		return this.subtract(Vector3f.scale(normal, this.dot(normal) * 2.0f));
	}

	public Vector3f rotate(Vector3f axis, float angle) {
		return this.rotate(axis.getX(), axis.getY(), axis.getZ(), angle);
	}

	public Vector3f rotate(float x, float y, float z, float angle) {
		float sineHalfTheta = (float) Math.sin(Math.toRadians(angle / 2.0f));
		float cosineHalfTheta = (float) Math.cos(Math.toRadians(angle / 2.0f));

		float rx = x * sineHalfTheta;
		float ry = y * sineHalfTheta;
		float rz = z * sineHalfTheta;
		float rw = cosineHalfTheta;

		Quaternion rotation = new Quaternion(rx, ry, rz, rw);
		Quaternion conjugate = Quaternion.conjugate(rotation);
		Quaternion result = rotation.multiply(this).multiply(conjugate);

		this.x = result.getX();
		this.y = result.getY();
		this.z = result.getZ();

		return this;
	}

	public Vector3f rotate(Quaternion quaternion) {
		Quaternion conjugate = Quaternion.conjugate(quaternion);

		Quaternion result = quaternion.multiply(this).multiply(conjugate);

		this.x = result.getX();
		this.y = result.getY();
		this.z = result.getZ();

		return this;
	}

	public Vector3f rotateTowards(Vector3f vector, float value) {
		if (this.angle(vector) <= value) {
			this.set(vector);
		} else {
			this.rotate(Vector3f.cross(this, vector).normalize(), value);
		}

		return this;
	}

	public Vector3f round() {
		this.x = (float) Math.round(this.x);
		this.y = (float) Math.round(this.y);
		this.z = (float) Math.round(this.z);

		return this;
	}

	public Vector3f scale(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;

		return this;
	}

	public Vector3f slerp(Vector3f vector, float value) {
		float dot = this.dot(vector);
		if (dot < -1.0f)
			dot = -1.0f;
		else if (dot > 1.0f)
			dot = 1.0f;

		float theta = (float) (Math.acos(dot) * value);
		Vector3f RelativeVec = Vector3f.subtract(vector, Vector3f.scale(this, dot)).normalize();

		return this.scale((float) Math.cos(theta)).add((RelativeVec.scale((float) Math.sin(theta))));
	}

	// public Vector3f transform(Matrix4f matrix) {
	// // TODO: Add functionality to this method;
	//
	// return this;
	// }

	public float angle(Vector3f vector) {
		return (float) Math.toDegrees(Math.acos(this.dot(vector) / Vector3f.dot(Vector3f.normalize(this), Vector3f.normalize(vector))));
	}

	public float distance(Vector3f vector) {
		return (float) Math.sqrt(this.distanceSq(vector));
	}

	public float distanceSq(Vector3f vector) {
		float dx = vector.getX() - this.x;
		float dy = vector.getY() - this.y;
		float dz = vector.getZ() - this.z;

		return dx * dx + dy * dy + dz * dz;
	}

	public float dot(Vector3f vector) {
		return this.x * vector.getX() + this.y * vector.getY() + this.z * vector.getZ();
	}

	public float length() {
		return (float) Math.sqrt(this.lengthSq());
	}

	public float lengthSq() {
		return x * x + y * y + z * z;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Vector3f) {
			return this.isEqual((Vector3f) object);
		}

		return false;
	}

	public boolean isEqual(Vector3f vector) {
		return (this.x == vector.getX() && this.y == vector.getY() && this.z == vector.getZ());
	}

	public boolean isNormalized() {
		float length = this.length();

		return (length >= 0.99999f && length <= 1.00001f);
	}

	public boolean isZero() {
		return this.isEqual(Vector3f.Zero());
	}

	public float[] toArray() {
		return new float[] {
				this.x, this.y, this.z
		};
	}

	@Override
	public String toString() {
		return new String("[ " + x + ", " + y + ", " + z + " ]");
	}

	public Vector3f get() {
		return new Vector3f(this);
	}

	public float get(int i) {
		if (i == COMPONENT_INDEX_X)
			return x;
		else if (i == COMPONENT_INDEX_Y)
			return y;
		else if (i == COMPONENT_INDEX_Z)
			return z;
		else
			throw new ArrayIndexOutOfBoundsException("No component exists at index " + i + ".");
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public Vector2f getXY() {
		return new Vector2f(x, y);
	}

	public Vector2f getXZ() {
		return new Vector2f(x, z);
	}

	public Vector2f getYX() {
		return new Vector2f(y, x);
	}

	public Vector2f getYZ() {
		return new Vector2f(y, z);
	}

	public Vector2f getZX() {
		return new Vector2f(z, x);
	}

	public Vector2f getZY() {
		return new Vector2f(z, y);
	}

	public Vector3f getXYZ() {
		return new Vector3f(x, y, z);
	}

	public Vector3f getXZY() {
		return new Vector3f(x, z, y);
	}

	public Vector3f getYXZ() {
		return new Vector3f(y, x, z);
	}

	public Vector3f getYZX() {
		return new Vector3f(y, z, x);
	}

	public Vector3f getZXY() {
		return new Vector3f(z, x, y);
	}

	public Vector3f getZYX() {
		return new Vector3f(z, y, x);
	}

	public Vector3f set(Vector3f vector) {
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();

		return this;
	}

	public Vector3f set(int i, float value) {
		if (i == COMPONENT_INDEX_X)
			this.x = value;
		else if (i == COMPONENT_INDEX_Y)
			this.y = value;
		else if (i == COMPONENT_INDEX_Z)
			this.z = value;
		else
			throw new ArrayIndexOutOfBoundsException("No component exists at index " + i + ".");

		return this;
	}

	public Vector3f setLength(float length) {
		return this.scale(length / this.length());
	}

	public Vector3f setX(float x) {
		this.x = x;

		return this;
	}

	public Vector3f setY(float y) {
		this.y = y;

		return this;
	}

	public Vector3f setZ(float z) {
		this.z = z;

		return this;
	}

	public Vector3f setXY(Vector2f vector) {
		return this.setXY(vector.getX(), vector.getY());
	}

	public Vector3f setXY(float x, float y) {
		this.x = x;
		this.y = y;

		return this;
	}

	public Vector3f setXZ(Vector2f vector) {
		return this.setXZ(vector.getX(), vector.getY());
	}

	public Vector3f setXZ(float x, float z) {
		this.x = x;
		this.z = z;

		return this;
	}

	public Vector3f setYX(Vector2f vector) {
		return this.setYX(vector.getX(), vector.getY());
	}

	public Vector3f setYX(float y, float x) {
		this.y = y;
		this.x = x;

		return this;
	}

	public Vector3f setYZ(Vector2f vector) {
		return this.setYZ(vector.getX(), vector.getY());
	}

	public Vector3f setYZ(float y, float z) {
		this.y = y;
		this.z = z;

		return this;
	}

	public Vector3f setZX(Vector2f vector) {
		return this.setZX(vector.getX(), vector.getY());
	}

	public Vector3f setZX(float z, float x) {
		this.z = z;
		this.x = x;

		return this;
	}

	public Vector3f setZY(Vector2f vector) {
		return this.setZY(vector.getX(), vector.getY());
	}

	public Vector3f setZY(float z, float y) {
		this.z = z;
		this.y = y;

		return this;
	}

	public Vector3f setXYZ(Vector3f vector) {
		return this.setXYZ(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f setXYZ(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;

		return this;
	}

	public Vector3f setXZY(Vector3f vector) {
		return this.setXZY(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f setXZY(float x, float z, float y) {
		this.x = x;
		this.z = z;
		this.y = y;

		return this;
	}

	public Vector3f setYXZ(Vector3f vector) {
		return this.setYXZ(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f setYXZ(float y, float x, float z) {
		this.y = y;
		this.x = x;
		this.z = z;

		return this;
	}

	public Vector3f setYZX(Vector3f vector) {
		return this.setYZX(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f setYZX(float y, float z, float x) {
		this.y = y;
		this.z = z;
		this.x = x;

		return this;
	}

	public Vector3f setZXY(Vector3f vector) {
		return this.setZXY(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f setZXY(float z, float x, float y) {
		this.z = z;
		this.x = x;
		this.y = y;

		return this;
	}

	public Vector3f setZYX(Vector3f vector) {
		return this.setZYX(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f setZYX(float z, float y, float x) {
		this.z = z;
		this.y = y;
		this.x = x;

		return this;
	}
}
