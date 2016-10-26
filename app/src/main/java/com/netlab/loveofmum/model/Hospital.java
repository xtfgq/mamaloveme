package com.netlab.loveofmum.model;

import com.netlab.loveofmum.api.MMloveConstants;

public class Hospital
{
	public String HospitalID;
	public String HospitalName;
	public String SoundFlag;
	private String ImageURL;

	public String getImageURL() {
		return ImageURL;
	}

	public void setImageURL(String imageURL) {
		ImageURL = MMloveConstants.JE_BASE_URL +imageURL;
	}
}
