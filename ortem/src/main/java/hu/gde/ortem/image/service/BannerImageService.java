package hu.gde.ortem.image.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hu.gde.ortem.campaign.model.BannerCampaign;
import hu.gde.ortem.common.util.OrtemConverterUtil;
import hu.gde.ortem.image.model.BannerImage;
import hu.gde.ortem.image.model.BannerImageDto;
import hu.gde.ortem.image.repository.BannerImageRepository;

/*
* @author  Noel Solyom
*/

@Service
public class BannerImageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BannerImageService.class);

	@Value("${ortem.server.banner.image.base.url}")
	private String IMAGE_BASE_URL;

	@Autowired
	BannerImageRepository bannerImageRepository;

	public void storeUrls(BannerCampaign bannerCampaign, List<String> urls) {
		LOGGER.info("Storing banner campaign images.");

		try {
			for (int i = 0; i < urls.size(); i++) {
				BannerImage bannerImage = new BannerImage();
				bannerImage.setBannerCampaign(bannerCampaign);
				bannerImage.setUrl(IMAGE_BASE_URL + urls.get(i));
				bannerImage.setImageOrder(i);

				bannerImageRepository.save(bannerImage);
			}
			LOGGER.info("Storing banner campaign images complete.");
		} catch (Exception e) {
			LOGGER.error("Storing banner campaign images failed. {}", e.getMessage());
		}
	}

	public List<BannerImageDto> getBannerImagesByBannerId(Long bannerCampaignId) {
		LOGGER.info("Getting banner images by banner campaign id.");
		try {
			Iterable<BannerImage> bannerImages = bannerImageRepository.getBannerImagesByBannerId(bannerCampaignId);
			List<BannerImage> bannerImageList = new ArrayList<>();
			bannerImages.forEach(bannerImageList::add);

			List<BannerImageDto> bannerImageResponseList = OrtemConverterUtil
					.convertBannerImageListToBannerImageResponseList(bannerImageList);

			LOGGER.info("Getting banner images by banner campaign id complete.");

			return bannerImageResponseList;
		} catch (Exception e) {
			LOGGER.error("Getting banner images by banner campaign id failed. {}", e.getMessage());
			return new ArrayList<>();
		}

	}

	public void deleteCampaignImage(String url) {
		LOGGER.info("Deleting banner campaign image.");

		LOGGER.info("url: {}", url);

		try {
			Optional<BannerImage> bannerImage = bannerImageRepository.findBannerImageByUrl(url);

			if (bannerImage.isPresent()) {

				LOGGER.info("Banner image found. Deleting {}", bannerImage.get().getUrl());

				File imageFile = new File("ortem/img/" + bannerImage.get().getUrl().split("images/")[1]);
				if (!imageFile.isDirectory()) {
					if (imageFile.exists()) {
						imageFile.delete();
					}
				}
				bannerImageRepository.delete(bannerImage.get());
			}

		} catch (Exception e) {
			LOGGER.error("Deleting banner campaign image failed. {}", e.getMessage());
		}
	}

	public void deleteAllCampaignImages(Long bannerCampaignId) {
		LOGGER.info("Deleting all banner campaign images.");

		LOGGER.info("bannerCampaignId: {}", bannerCampaignId);

		try {
			Iterable<BannerImage> bannerImages = bannerImageRepository.getBannerImagesByBannerId(bannerCampaignId);
			List<BannerImage> bannerImageList = new ArrayList<>();
			bannerImages.forEach(bannerImageList::add);

			for (BannerImage bannerImage : bannerImageList) {
				bannerImageRepository.delete(bannerImage);
				File imageFile = new File("ortem/img/" + bannerImage.getUrl().split("images/")[1]);
				if (!imageFile.isDirectory()) {
					if (imageFile.exists()) {
						imageFile.delete();
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Deleting banner campaign images failed. {}", e.getMessage());
		}

	}

}
