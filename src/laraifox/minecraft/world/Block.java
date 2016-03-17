package laraifox.minecraft.world;

public class Block {
	public static final int CUBE_FACE_LEFT = 0;
	public static final int CUBE_FACE_RIGHT = 1;
	public static final int CUBE_FACE_BOTTOM = 2;
	public static final int CUBE_FACE_TOP = 3;
	public static final int CUBE_FACE_BACK = 4;
	public static final int CUBE_FACE_FRONT = 5;
	public static final int CUBE_FACE_COUNT = 6;

	public static final int CUBE_FLOATS_PER_VERTEX = 8;
	public static final int CUBE_VERTEX_COUNT = 24;
	//	public static final float[] CUBE_VERTICES = new float[] {
	//			0.0f, 0.0f, 0.0f,		// (0) BACK  BOTTOM LEFT
	//			1.0f, 0.0f, 0.0f,		// (1) BACK  BOTTOM RIGHT
	//			1.0f, 1.0f, 0.0f,		// (2) BACK  TOP    RIGHT
	//			0.0f, 1.0f, 0.0f,		// (3) BACK  TOP    LEFT
	//			0.0f, 0.0f, 1.0f,		// (4) FRONT BOTTOM LEFT
	//			1.0f, 0.0f, 1.0f,		// (5) FRONT BOTTOM RIGHT
	//			1.0f, 1.0f, 1.0f,		// (6) FRONT TOP    RIGHT
	//			0.0f, 1.0f, 1.0f		// (7) FRONT TOP    LEFT
	//	};

	//	public static final float[] CUBE_VERTICES = new float[] {
	//			0.0f, 0.0f, 0.0f, 0.0f, 0.0f,		// (0) BACK  BOTTOM LEFT
	//			1.0f, 0.0f, 0.0f, 1.0f, 0.0f,		// (1) BACK  BOTTOM RIGHT
	//			1.0f, 1.0f, 0.0f, 1.0f, 1.0f,		// (2) BACK  TOP    RIGHT
	//			0.0f, 1.0f, 0.0f, 0.0f, 1.0f,		// (3) BACK  TOP    LEFT
	//			0.0f, 0.0f, 1.0f, 0.0f, 0.0f,		// (4) FRONT BOTTOM LEFT
	//			1.0f, 0.0f, 1.0f, 1.0f, 0.0f,		// (5) FRONT BOTTOM RIGHT
	//			1.0f, 1.0f, 1.0f, 1.0f, 1.0f,		// (6) FRONT TOP    RIGHT
	//			0.0f, 1.0f, 1.0f, 0.0f, 1.0f		// (7) FRONT TOP    LEFT
	//	};

	public static final float[] CUBE_VERTICES = new float[] {
			// LEFT SIDE VERTICES { 0, 1, 2, 0, 2, 2 }
			0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,	// (0)  
			0.0f, 0.0f, 0.0f, 0.0625f, 0.0f, -1.0f, 0.0f, 0.0f,	// (1)  
			0.0f, 1.0f, 0.0f, 0.0625f, 0.0625f, -1.0f, 0.0f, 0.0f,	// (2)  
			0.0f, 1.0f, 1.0f, 0.0f, 0.0625f, -1.0f, 0.0f, 0.0f,	// (3)  
			// RIGHT SIDE VERTICES
			1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,		// (4)  
			1.0f, 0.0f, 1.0f, 0.0625f, 0.0f, 1.0f, 0.0f, 0.0f,		// (5)  
			1.0f, 1.0f, 1.0f, 0.0625f, 0.0625f, 1.0f, 0.0f, 0.0f,		// (6)  
			1.0f, 1.0f, 0.0f, 0.0f, 0.0625f, 1.0f, 0.0f, 0.0f,		// (7)  
			// BOTTOM SIDE VERTICES
			0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f,	// (8)  
			1.0f, 0.0f, 1.0f, 0.0625f, 0.0f, 0.0f, -1.0f, 0.0f,	// (9)  
			1.0f, 0.0f, 0.0f, 0.0625f, 0.0625f, 0.0f, -1.0f, 0.0f,	// (10) 
			0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, 0.0f, -1.0f, 0.0f,	// (11) 
			// TOP SIDE VERTICES
			0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,		// (12) 
			1.0f, 1.0f, 0.0f, 0.0625f, 0.0f, 0.0f, 1.0f, 0.0f,		// (13) 
			1.0f, 1.0f, 1.0f, 0.0625f, 0.0625f, 0.0f, 1.0f, 0.0f,		// (14) 
			0.0f, 1.0f, 1.0f, 0.0f, 0.0625f, 0.0f, 1.0f, 0.0f,		// (15) 
			// BACK SIDE VERTICES
			0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f,	// (16) 
			1.0f, 0.0f, 0.0f, 0.0625f, 0.0f, 0.0f, 0.0f, -1.0f,	// (17) 
			1.0f, 1.0f, 0.0f, 0.0625f, 0.0625f, 0.0f, 0.0f, -1.0f,	// (18) 
			0.0f, 1.0f, 0.0f, 0.0f, 0.0625f, 0.0f, 0.0f, -1.0f,	// (19) 
			// FRONT SIDE VERTICES
			1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,		// (20) 
			0.0f, 0.0f, 1.0f, 0.0625f, 0.0f, 0.0f, 0.0f, 1.0f,		// (21) 
			0.0f, 1.0f, 1.0f, 0.0625f, 0.0625f, 0.0f, 0.0f, 1.0f,		// (22) 
			1.0f, 1.0f, 1.0f, 0.0f, 0.0625f, 0.0f, 0.0f, 1.0f		// (23) 
	};

	public static final int CUBE_INDICES_PER_FACE = 6;
	public static final int[] CUBE_INDICES = new int[] {
			0, 1, 2, 0, 2, 3,			// LEFT FACE
			4, 5, 6, 4, 6, 7,			// RIGHT FACE
			8, 9, 10, 8, 10, 11,		// BOTTOM FACE
			12, 13, 14, 12, 14, 15,		// TOP FACE
			16, 17, 18, 16, 18, 19,		// BACK FACE
			20, 21, 22, 20, 22, 23		// FRONT FACE
	};
	//	public static final int[] CUBE_INDICES = new int[] {
	//			4, 0, 3, 4, 3, 7,		// LEFT FACE
	//			1, 5, 6, 1, 6, 2,		// RIGHT FACE
	//			4, 5, 1, 4, 1, 0,		// BOTTOM FACE
	//			3, 2, 6, 3, 6, 7,		// TOP FACE
	//			0, 1, 2, 0, 2, 3,		// BACK FACE
	//			5, 4, 7, 5, 7, 6,		// FRONT FACE
	//	};

	public static final int[] CUBE_FACE_NORMALS = new int[] {
			-1, 0, 0,				// LEFT FACE
			1, 0, 0,				// RIGHT FACE
			0, -1, 0,				// BOTTOM FACE
			0, 1, 0,				// TOP FACE
			0, 0, -1,				// BACK FACE
			0, 0, 1,				// FRONT FACE
	};

	public static final int[] CUBE_OUTLINE_INDICES = new int[] {
			0, 1, 1, 2, 2, 3, 3, 0,	// BACK OUTLINE
			4, 5, 5, 6, 6, 7, 7, 4,	// FRONT OUTLINE
			0, 4, 1, 5, 2, 6, 3, 7	// EDGE OUTLINE
	};

	public static final int CUBE_VERTICES_PER_FACE = 4;

	public int id;
	public byte metadata;

	private boolean opaque;
	
	static {
		BlockRegistry.registerBlock(0, new Block(0));
		BlockRegistry.registerBlock(1, new Block(1));
		BlockRegistry.registerBlock(2, new Block(2));
		BlockRegistry.registerBlock(3, new Block(3));
		BlockRegistry.registerBlock(4, new Block(4));
		BlockRegistry.registerBlock(5, new Block(5));
	}

	public Block(int id) {
		this.id = id;
		this.opaque = id != 0;
	}

	public float[] getCubeVertices() {
		return CUBE_VERTICES;
	}

	public float getCubeIndex(int i) {
		return CUBE_INDICES[i];
	}

	public boolean isOpaque() {
		return opaque;
	}

	public int getID() {
		return id;
	}
}
