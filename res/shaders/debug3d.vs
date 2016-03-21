#version 120

attribute vec3 positionAttrib;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 color;

varying vec4 v_Color;

void main() {
	v_Color = vec4(color, 1.0);
	
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(positionAttrib, 1.0);
}