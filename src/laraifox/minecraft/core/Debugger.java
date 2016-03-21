package laraifox.minecraft.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import laraifox.minecraft.math.Matrix4f;
import laraifox.minecraft.math.Vector3f;

public class Debugger {
	private static Shader debugShader2D;
	private static Shader debugShader3D;

	private static int frustumVBO;

	public static void initialize() throws Exception {
		debugShader3D = new Shader("res/shaders/debug3d.vs", "res/shaders/debug3d.fs", true);

		frustumVBO = GL15.glGenBuffers();
	}

	public static void render(Camera mainCamera, Camera debugCamera, PerspectiveProjection projection) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		debugShader3D.bind();
		debugShader3D.setUniform("projectionMatrix", mainCamera.getProjectionMatrix());
		debugShader3D.setUniform("viewMatrix", mainCamera.getViewMatrix());

		debugShader3D.setUniform("modelMatrix", Matrix4f.Translation(debugCamera.getPosition()).multiply(debugCamera.getRotation().toRotationMatrix()));
		debugShader3D.setUniform("color", new Vector3f(1.0f, 1.0f, 0.5f));

		GL20.glEnableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, frustumVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Debugger.getFrustumBuffer(debugCamera, projection), GL15.GL_STREAM_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vector3f.COMPONENT_COUNT * Float.BYTES, 0 * Float.BYTES);
		GL11.glDrawArrays(GL11.GL_LINES, 0, 24);
		GL11.glPointSize(10.0f);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vector3f.COMPONENT_COUNT * 2 * Float.BYTES, 0 * Float.BYTES);
		GL11.glDrawArrays(GL11.GL_POINTS, 0, 8);
		GL20.glDisableVertexAttribArray(0);
	}

	private static FloatBuffer getFrustumBuffer(Camera debugCamera, PerspectiveProjection projection) {
		float halfFOV = projection.getFOV() / 2.0f;
		float apsect = projection.getWidth() / projection.getHeight();

		Vector3f bottomLeftNormal = Vector3f.Forward().rotate(Vector3f.Up(), apsect * -halfFOV).rotate(Vector3f.Right(), halfFOV);
		Vector3f bottomRightNormal = Vector3f.Forward().rotate(Vector3f.Up(), apsect * halfFOV).rotate(Vector3f.Right(), halfFOV);
		Vector3f topRightNormal = Vector3f.Forward().rotate(Vector3f.Up(), apsect * halfFOV).rotate(Vector3f.Right(), -halfFOV);
		Vector3f topLeftNormal = Vector3f.Forward().rotate(Vector3f.Up(), apsect * -halfFOV).rotate(Vector3f.Right(), -halfFOV);

		Vector3f[] frustumVertices = new Vector3f[] {
				Vector3f.scale(bottomLeftNormal, (float) (projection.getZNear() * Math.cos(Math.toRadians(halfFOV)))),	//
				Vector3f.scale(bottomRightNormal, (float) (projection.getZNear() * Math.cos(Math.toRadians(halfFOV)))),	//
				Vector3f.scale(topRightNormal, (float) (projection.getZNear() * Math.cos(Math.toRadians(halfFOV)))),	//
				Vector3f.scale(topLeftNormal, (float) (projection.getZNear() * Math.cos(Math.toRadians(halfFOV)))),	//

				Vector3f.scale(bottomLeftNormal, (float) (projection.getZFar() * Math.cos(Math.toRadians(halfFOV)))),	//
				Vector3f.scale(bottomRightNormal, (float) (projection.getZFar() * Math.cos(Math.toRadians(halfFOV)))),	//
				Vector3f.scale(topRightNormal, (float) (projection.getZFar() * Math.cos(Math.toRadians(halfFOV)))),	//
				Vector3f.scale(topLeftNormal, (float) (projection.getZFar() * Math.cos(Math.toRadians(halfFOV)))),	//
				
//				Vector3f.projectToVector(Vector3f.Forward(projection.getZNear()), bottomRightNormal),	//
//				Vector3f.projectToVector(Vector3f.Forward(projection.getZNear()), topRightNormal),		//
//				Vector3f.projectToVector(Vector3f.Forward(projection.getZNear()), topLeftNormal),		//
//
//				Vector3f.projectToVector(Vector3f.Forward(projection.getZFar()), bottomLeftNormal),		//
//				Vector3f.projectToVector(Vector3f.Forward(projection.getZFar()), bottomRightNormal),	//
//				Vector3f.projectToVector(Vector3f.Forward(projection.getZFar()), topRightNormal),		//
//				Vector3f.projectToVector(Vector3f.Forward(projection.getZFar()), topLeftNormal),		//
		};

		FloatBuffer buffer = ByteBuffer.allocateDirect(Float.BYTES * 24 * 3).order(ByteOrder.nativeOrder()).asFloatBuffer();
		Debugger.addToBuffer(buffer, frustumVertices[0]);
		Debugger.addToBuffer(buffer, frustumVertices[1]);
		Debugger.addToBuffer(buffer, frustumVertices[1]);
		Debugger.addToBuffer(buffer, frustumVertices[2]);
		Debugger.addToBuffer(buffer, frustumVertices[2]);
		Debugger.addToBuffer(buffer, frustumVertices[3]);
		Debugger.addToBuffer(buffer, frustumVertices[3]);
		Debugger.addToBuffer(buffer, frustumVertices[0]);

		Debugger.addToBuffer(buffer, frustumVertices[4]);
		Debugger.addToBuffer(buffer, frustumVertices[5]);
		Debugger.addToBuffer(buffer, frustumVertices[5]);
		Debugger.addToBuffer(buffer, frustumVertices[6]);
		Debugger.addToBuffer(buffer, frustumVertices[6]);
		Debugger.addToBuffer(buffer, frustumVertices[7]);
		Debugger.addToBuffer(buffer, frustumVertices[7]);
		Debugger.addToBuffer(buffer, frustumVertices[4]);

		Debugger.addToBuffer(buffer, frustumVertices[0]);
		Debugger.addToBuffer(buffer, frustumVertices[4]);
		Debugger.addToBuffer(buffer, frustumVertices[1]);
		Debugger.addToBuffer(buffer, frustumVertices[5]);
		Debugger.addToBuffer(buffer, frustumVertices[2]);
		Debugger.addToBuffer(buffer, frustumVertices[6]);
		Debugger.addToBuffer(buffer, frustumVertices[3]);
		Debugger.addToBuffer(buffer, frustumVertices[7]);
		buffer.flip();

		return buffer;
	}

	private static void addToBuffer(FloatBuffer buffer, Vector3f vector) {
		buffer.put(vector.getX());
		buffer.put(vector.getY());
		buffer.put(vector.getZ());
	}
}
