package hu.gde.ortem.campaign.model;

import java.util.List;

import hu.gde.ortem.image.model.BannerImageDto;

public class BannerCampaignDto {

	private Long id;

	private String campaignFromDate;

	private String campaignFromTime;

	private String campaignToDate;

	private String campaignToTime;

	private String description;

	private Integer refreshRate;

	private List<BannerImageDto> images;

	private String maxSize;

	public static BannerCampaignDto bannerCampaignDTOFactory(BannerCampaignDto dto, String param) {
		BannerCampaignDto b = dto;
		b.maxSize = param;
		return b;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCampaignFromDate() {
		return campaignFromDate;
	}

	public void setCampaignFromDate(String campaignFromDate) {
		this.campaignFromDate = campaignFromDate;
	}

	public String getCampaignFromTime() {
		return campaignFromTime;
	}

	public void setCampaignFromTime(String campaignFromTime) {
		this.campaignFromTime = campaignFromTime;
	}

	public String getCampaignToDate() {
		return campaignToDate;
	}

	public void setCampaignToDate(String campaignToDate) {
		this.campaignToDate = campaignToDate;
	}

	public String getCampaignToTime() {
		return campaignToTime;
	}

	public void setCampaignToTime(String campaignToTime) {
		this.campaignToTime = campaignToTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(Integer refreshRate) {
		this.refreshRate = refreshRate;
	}

	public List<BannerImageDto> getImages() {
		return images;
	}

	public void setImages(List<BannerImageDto> images) {
		this.images = images;
	}

	public String getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(String maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public String toString() {
		return "BannerCampaignDto [id=" + id + ", campaignFromDate=" + campaignFromDate + ", campaignFromTime="
				+ campaignFromTime + ", campaignToDate=" + campaignToDate + ", campaignToTime=" + campaignToTime
				+ ", description=" + description + ", refreshRate=" + refreshRate + ", images=" + images + "]";
	}

}
