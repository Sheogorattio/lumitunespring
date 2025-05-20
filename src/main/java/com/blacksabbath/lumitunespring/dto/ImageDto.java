package com.blacksabbath.lumitunespring.dto;

public class ImageDto {
	private String id;
	private String Url;
	private String ownerId;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return this.Url;
	}

	public void setUrl(String url) {
		this.Url = url;
	}

	public String getOwner() {
		return this.ownerId;
	}

	public void setOwner(String ownerId) {
		this.ownerId = ownerId;
	}
}
