package hu.gde.ortem.campaign.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import hu.gde.ortem.common.model.BaseModel;
import hu.gde.ortem.image.model.BannerImage;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/*
* @author  Noel Solyom
*/

@Entity(name = "BannerCampaign")
@Table(name = "banner_campaign")
public class BannerCampaign extends BaseModel {

	private LocalDateTime campaignFrom;

	private LocalDateTime campaignTo;

	private String description;

	private Integer refreshRate;

	@OneToMany(mappedBy = "bannerCampaign")
	private List<BannerImage> bannerImageList = new ArrayList<>();

	public LocalDateTime getCampaignFrom() {
		return campaignFrom;
	}

	public void setCampaignFrom(LocalDateTime campaignFrom) {
		this.campaignFrom = campaignFrom;
	}

	public LocalDateTime getCampaignTo() {
		return campaignTo;
	}

	public void setCampaignTo(LocalDateTime campaignTo) {
		this.campaignTo = campaignTo;
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

	public List<BannerImage> getBannerImageList() {
		return bannerImageList;
	}

	public void setBannerImageList(List<BannerImage> bannerImageList) {
		this.bannerImageList = bannerImageList;
	}

	@Override
	public String toString() {
		return "BannerCampaign [campaignFrom=" + campaignFrom + ", campaignTo=" + campaignTo + ", description="
				+ description + ", refreshRate=" + refreshRate + ", bannerImageList=" + bannerImageList + "]";
	}

}
