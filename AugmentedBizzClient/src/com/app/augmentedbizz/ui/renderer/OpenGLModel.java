package com.app.augmentedbizz.ui.renderer;

public class OpenGLModel {
	
	private int id;
	private int modelVersion;
	private float[] vertices;
	private float[] normals;
	private float[] textureCoordinates;
	private short[] indices;
	private Texture texture;
	
	public OpenGLModel(
			int id,
			int modelVersion,
			float[] vertices,
			float[] normals,
			float[] textureCoordinates,
			short[] indices,
			Texture texture) {
		this.id = id;
		this.modelVersion = modelVersion;
		this.vertices = vertices;
		this.normals = normals;
		this.textureCoordinates = textureCoordinates;
		this.indices = indices;
		this.texture = texture;
	}

	public int getId() {
		return id;
	}

	public int getModelVersion() {
		return modelVersion;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getNormals() {
		return normals;
	}

	public float[] getTextureCoordinates() {
		return textureCoordinates;
	}

	public short[] getIndices() {
		return indices;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public float getXAxisBoundingLength() {
		if(vertices == null || 
		   vertices.length == 0 ||
		   vertices.length % 3 != 0) {
			return 0.0f;
		}
		float minValue = -0.0f;
		float maxValue = 0.0f;
		for(int i = 0; i < vertices.length / 3; ++i) {
			float verticeX = vertices[i * 3];
			if(verticeX < minValue) {
				minValue = verticeX;
			} else if (verticeX > maxValue) {
				maxValue = verticeX;
			}
		}
		return Math.abs(minValue) + Math.abs(maxValue);
	}
	
	public float getYAxisBoundingLength() {
		if(vertices == null || 
		   vertices.length == 0 ||
		   vertices.length % 3 != 0) {
			return 0.0f;
		}
		float minValue = -0.0f;
		float maxValue = 0.0f;
		for(int i = 0; i < vertices.length / 3; ++i) {
			float verticeY = vertices[i * 3 + 1];
			if(verticeY < minValue) {
				minValue = verticeY;
			} else if (verticeY > maxValue) {
				maxValue = verticeY;
			}
		}
		return Math.abs(minValue) + Math.abs(maxValue);
	}
	
	public float getZAxisBoundingLength() {
		if(vertices == null || 
		   vertices.length == 0 ||
		   vertices.length % 3 != 0) {
			return 0.0f;
		}
		float minValue = -0.0f;
		float maxValue = 0.0f;
		for(int i = 0; i < vertices.length / 3; ++i) {
			float verticeZ = vertices[i * 3 + 2];
			if(verticeZ < minValue) {
				minValue = verticeZ;
			} else if (verticeZ > maxValue) {
				maxValue = verticeZ;
			}
		}
		return Math.abs(minValue) + Math.abs(maxValue);
	}
}

