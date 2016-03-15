package laraifox.minecraft.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Texture2D {
	private static final int BYTES_PER_PIXEL = 4;

	private int id;
	private int width, height;

	public Texture2D() {
		this(0, 0, 0);
	}

	//	public Texture(Vector4f color) {
	//		int[] pixels = new int[] {
	//				(int) (color.getX() * 0xFF), (int) (color.getY() * 0xFF), (int) (color.getZ() * 0xFF), (int) (color.getW() * 0xFF)
	//		};
	//
	//		this.textureID = Texture.generateTexture(Texture.createByteBuffer(pixels), 1, 1);
	//		this.width = 1;
	//		this.height = 1;
	//	}

	public Texture2D(Texture2D texture) {
		this(texture.getTextureID(), texture.getWidth(), texture.getHeight());
	}

	private Texture2D(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	public void setTexture(Texture2D texture) {
		this.id = texture.getTextureID();
		this.width = texture.getWidth();
		this.height = texture.getHeight();
	}

	public int getTextureID() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static Texture2D getTextureFrom(BufferedImage image) {
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		return Texture2D.getTextureFrom(pixels, image.getWidth(), image.getHeight());
	}

	public static Texture2D getTextureFrom(ByteBuffer buffer, int width, int height) {
		return new Texture2D(Texture2D.generateTexture(buffer, width, height), width, height);
	}

	public static Texture2D getTextureFrom(int[] pixels, int width, int height) {
		return Texture2D.getTextureFrom(Texture2D.createByteBuffer(pixels), width, height);
	}

	public static Texture2D getTextureFrom(String filename) throws IOException {
		return Texture2D.getTextureFrom(ImageIO.read(new File(filename)));
	}

	private static ByteBuffer createByteBuffer(int[] pixels) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * BYTES_PER_PIXEL);
		for (int i = 0; i < pixels.length; i++) {
			int pixel = pixels[i];

			buffer.put((byte) ((pixel >> 16) & 0xFF));
			buffer.put((byte) ((pixel >> 8) & 0xFF));
			buffer.put((byte) (pixel & 0xFF));
			buffer.put((byte) ((pixel >> 24) & 0xFF));
		}

		buffer.flip();

		return buffer;
	}

	private static int generateTexture(ByteBuffer buffer, int width, int height) {
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

		return textureID;
	}

	public static void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}
