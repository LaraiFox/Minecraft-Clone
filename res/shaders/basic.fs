#version 120

uniform sampler2D diffuse;

varying vec4 v_Color;
varying vec2 v_Texcoord;

void main() {
	gl_FragColor = v_Color * texture2D(diffuse, v_Texcoord.st);
}