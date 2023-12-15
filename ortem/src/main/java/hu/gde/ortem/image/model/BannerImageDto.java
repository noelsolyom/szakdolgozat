package hu.gde.ortem.image.model;

public class BannerImageDto {

	private Long id;

	private String url;

	private Integer imageOrder;

	private String imageData;

	private String extension;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		return "BannerImageDto [id=" + id + ", url=" + url + ", imageOrder=" + imageOrder + ", imageData=" + imageData
				+ ", extension=" + extension + "]";
	}

}
