package com.app.augmentedbizz.application.data;


/**
 * Holds the target information.
 * 
 * @author Miffels
 *
 */
public class Target {
	
	private String targetName;
	private int modelId;
	private int latestModelVersion;
	
	public Target(String targetName, int modelId, int latestModelVersion)
	{
		this.targetName = targetName;
		this.modelId = modelId;
		this.latestModelVersion = latestModelVersion;
	}
	
	public String getTargetName() 
	{
		return targetName;
	}
	
	public int getModelId() 
	{
		return modelId;
	}
	
	public int getLatestModelVersion()
	{
		return latestModelVersion;
	}

}
