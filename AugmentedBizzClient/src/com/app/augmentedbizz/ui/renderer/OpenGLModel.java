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
	
}

