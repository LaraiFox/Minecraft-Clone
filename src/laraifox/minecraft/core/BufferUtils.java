package laraifox.minecraft.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import laraifox.minecraft.math.Matrix4f;

public class BufferUtils {
	private static final int SHORT_SIZE = Short.SIZE / Byte.SIZE;
	private static final int INTEGER_SIZE = Integer.SIZE / Byte.SIZE;
	private static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;

	public static ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
	}

	public static ByteBuffer createShortBuffer(byte[] data, boolean flipped) {
		ByteBuffer buffer = createByteBuffer(data.length);
		buffer.put(data);

		if (flipped)
			buffer.flip();

		return buffer;
	}

	public static ShortBuffer createShortBuffer(int size) {
		return createByteBuffer(size * SHORT_SIZE).asShortBuffer();
	}

	public static ShortBuffer createShortBuffer(short[] data, boolean flipped) {
		ShortBuffer buffer = createShortBuffer(data.length);
		buffer.put(data);

		if (flipped)
			buffer.flip();

		return buffer;
	}

	public static IntBuffer createIntBuffer(int size) {
		return createByteBuffer(size * INTEGER_SIZE).asIntBuffer();
	}

	public static IntBuffer createIntBuffer(int[] indices, boolean flipped) {
		IntBuffer buffer = createIntBuffer(indices.length);
		buffer.put(indices);

		if (flipped)
			buffer.flip();

		return buffer;
	}

	public static FloatBuffer createFloatBuffer(int size) {
		return createByteBuffer(size * FLOAT_SIZE).asFloatBuffer();
	}

	public static FloatBuffer createFloatBuffer(float[] data, boolean flipped) {
		FloatBuffer buffer = createFloatBuffer(data.length);
		buffer.put(data);

		if (flipped)
			buffer.flip();

		return buffer;
	}

	public static FloatBuffer createFloatBuffer(Matrix4f matrix, boolean flipped) {
		FloatBuffer buffer = createFloatBuffer(16);
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				buffer.put(matrix.getDataAt(i, j));
			}
		}

		if (flipped)
			buffer.flip();

		return buffer;
	}
}
