package com.xtravel.entity;

public class ReservationItem {

	public String dealId;
	public String sImageUrl;
	public String title;
	public String description;
	public double listPrice;
	public double currentPrice;
	public int distance;
	public String dealH5Url;
	public ReservationItem(String dealId, String sImageUrl, String title, String description, double listPrice, double currentPrice, int distance,
			String dealH5Url) {
		super();
		this.dealId = dealId;
		this.sImageUrl = sImageUrl;
		this.title = title;
		this.description = description;
		this.listPrice = listPrice;
		this.currentPrice = currentPrice;
		this.distance = distance;
		this.dealH5Url = dealH5Url;
	}

}
