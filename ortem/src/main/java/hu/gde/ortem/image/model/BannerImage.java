package hu.gde.ortem.image.model;

import hu.gde.ortem.campaign.model.BannerCampaign;
import hu.gde.ortem.common.model.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
* @author  Noel Solyom
*/

@Entity(name = "BannerImage")
@Table(name = "banner_image")
public class BannerImage extends BaseModel {

	private String url;

	private Integer imageOrder;

	@ManyToOne
	@JoinColumn(name = "banner_campaign_id", nullable = false)
	private BannerCampaign bannerCampaign;

	public BannerCampaign getBannerCampaign() {
		return bannerCampaign;
	}

	public void setBannerCampaign(BannerCampaign bannerCampaign) {
		this.bannerCampaign = bannerCampaign;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getImageOrder() {
		return imageOrder;
	}

	public void setImageOrder(Integer imageOrder) {
		this.imageOrder = imageOrder;
	}

	@Override
	public String toString() {
		return "BannerImage [url=" + url + ", imageOrder=" + imageOrder + "]";
	}

}
