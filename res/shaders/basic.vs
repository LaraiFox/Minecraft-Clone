#version 120

attribute vec3 positionAttrib;
attribute vec2 texcoordAttrib;
attribute vec3 normalAttrib;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform vec2 stackPosition;
uniform float chunkHeight;

uniform vec3 color;

varying vec4 v_Color;
varying vec2 v_Texcoord;

void main() {
	if (normalAttrib.x == -1.0) {
		v_Color = vec4(color * 0.8, 1.0);
	} else if (normalAttrib.x == 1.0) {
		v_Color = vec4(color * 0.7, 1.0);
	} else if (normalAttrib.y == -1.0) {
		v_Color = vec4(color * 0.5, 1.0);
	} else if (normalAttrib.y == 1.0) {
		v_Color = vec4(color, 1.0);
	} else if (normalAttrib.z == -1.0) {
		v_Color = vec4(color * 0.9, 1.0);
	} else if (normalAttrib.z == 1.0) {
		v_Color = vec4(color * 0.6, 1.0);
	}
	
	v_Texcoord = texcoordAttrib;
	gl_Position = projectionMatrix * viewMatrix * vec4(positionAttrib + vec3(stackPosition.x, chunkHeight, stackPosition.y), 1.0);
}