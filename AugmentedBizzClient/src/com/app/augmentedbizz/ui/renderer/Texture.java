package com.app.augmentedbizz.ui.renderer;

/**
 * Support class that holds texture information.
 * 
 * @author Miffels
 *
 */
public class Texture {
	
    private int width;      // The width of the texture.
    private int height;     // The height of the texture.
    private int numberOfChannels = 4;   // The number of channels.
    private byte[] RGBAdata;    // The pixel data.
    
    public Texture(int width, int height, byte[] RGBAdata) {
    	this.width = width;
    	this.height = height;
    	this.RGBAdata = RGBAdata;
    }
    
    /** Returns the raw data */
    public byte[] getData()
    {
        return this.RGBAdata;
    }

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	public byte[] getRGBAdata() {
		return RGBAdata;
	}
    
    
}