package hu.gde.ortem.campaign.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;

import hu.gde.ortem.campaign.model.BannerCampaign;
import hu.gde.ortem.campaign.model.BannerCampaignDto;
import hu.gde.ortem.campaign.model.BannerCampaignResponse;
import hu.gde.ortem.campaign.repository.BannerCampaignRepository;
import hu.gde.ortem.common.model.ErrorResponse;
import hu.gde.ortem.common.util.OrtemConverterUtil;
import hu.gde.ortem.image.model.BannerImage;
import hu.gde.ortem.image.model.BannerImageDto;
import hu.gde.ortem.image.service.BannerImageService;

/*
* @author  Noel Solyom
*/

@Service
public class BannerCampaignService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BannerCampaignService.class);

	@Value("${ortem.banner.image.max.size}")
	private String maxSize;

	@Autowired
	private BannerCampaignRepository bannerCampaignRepository;

	@Autowired
	private BannerImageService bannerImageService;

	public BannerCampaignResponse<BannerCampaignDto> getCurrentCampaign() {
		LOGGER.info("Getting current banner campaign.");
		BannerCampaignResponse<BannerCampaignDto> response = new BannerCampaignResponse<>();
		try {
			Optional<BannerCampaign> currentCampaign = bannerCampaignRepository.getCurrentBannerCampaign();

			if (currentCampaign.isPresent()) {
				response.setResponse(Optional.of(OrtemConverterUtil.convertCampaignToDto(currentCampaign)));
			}
		} catch (Exception e) {
			response.setError(Optional.of(new ErrorResponse("Error getting current campaign.")));
		}

		return response;
	}

	public BannerCampaignResponse<BannerCampaignDto> createBannerCampaign(BannerCampaignDto bannerCampaignDto) {
		LOGGER.info("Creating banner campaign.");

		BannerCampaignResponse<BannerCampaignDto> response = new BannerCampaignResponse<>();

		bannerCampaignDto = BannerCampaignDto.bannerCampaignDTOFactory(bannerCampaignDto, maxSize);

		BannerCampaign bannerCampaign = OrtemConverterUtil.convertBannerCampaignDtoToBannerCampaign(bannerCampaignDto);

		List<BannerCampaign> overLappingCampaign = bannerCampaignRepository
				.bannerCampaignOverlap(bannerCampaign.getCampaignFrom(), bannerCampaign.getCampaignTo());

		if (overLappingCampaign.size() > 0) {
			LOGGER.info("Another overlapping banner campaign peresent.");
			response.setError(Optional.of(new ErrorResponse("Another overlapping campaign present.")));
			return response;
		} else {
			LOGGER.info("Trying to store banner images.");

			List<String> urls = null;
			try {
				urls = storeImages(bannerCampaignDto.getImages());
			} catch (Exception e) {
				LOGGER.error("Cannot store images.");
				response.setError(Optional.of(new ErrorResponse("Cannto store images.")));
				e.printStackTrace();
				return response;
			}

			try {
				LOGGER.info("Trying to save banner campaign. {}", bannerCampaign.toString());
				bannerCampaignRepository.save(bannerCampaign);
			} catch (Exception e) {
				LOGGER.error("Cannot store camaign.");
				response.setError(Optional.of(new ErrorResponse("Cannto store campaign.")));
				return response;
			}

			try {
				LOGGER.info("Trying to store banner image urls.");
				bannerImageService.storeUrls(bannerCampaign, urls);
			} catch (Exception e) {
				LOGGER.error("Cannot store image urls.");
				response.setError(Optional.of(new ErrorResponse("Cannto store imgage urls.")));
				return response;
			}

		}

		response.setResponse(Optional.of(bannerCampaignDto));
		return response;
	}

	public List<String> storeImages(List<BannerImageDto> encodedImageDataList) throws Exception {
		LOGGER.info("Storing banner images.");

		try {
			List<String> urls = new ArrayList<>();

			File imageStoreDirectory = new File("ortem/img");
			if (!imageStoreDirectory.isDirectory()) {
				if (imageStoreDirectory.exists()) {
					imageStoreDirectory.delete();
				}
				imageStoreDirectory.mkdirs();
			}

			for (BannerImageDto encodedImageData : encodedImageDataList) {
				if (encodedImageData.getImageData() != null && !encodedImageData.getImageData().isBlank()) {
					String imageString = encodedImageData.getImageData();
					// data:image/png;base64,

					String extension = "." + encodedImageData.getExtension();

					File imageFile = File.createTempFile("ortem", extension, imageStoreDirectory);

					Files.write(Base64.decodeBase64(imageString), imageFile);
					urls.add(imageFile.getName());
				}
			}

			LOGGER.info("Storing banner images complete.");
			return urls;
		} catch (Exception e) {
			LOGGER.error("Storing banner images failed. {}", e.getMessage());
			throw e;
		}
	}

	public BannerCampaignResponse<List<BannerCampaignDto>> getBannerCampaigns() {
		LOGGER.info("Listing banner campaigns.");

		BannerCampaignResponse<List<BannerCampaignDto>> result = new BannerCampaignResponse<>();
		try {
			Iterable<BannerCampaign> bannerCampaigns = bannerCampaignRepository.findAllActive();
			List<BannerCampaign> bannerCampaignList = new ArrayList<>();
			bannerCampaigns.forEach(bannerCampaignList::add);

			List<BannerCampaignDto> bannerCampaignResponses = OrtemConverterUtil
					.convertBannerCampaignListToBannerCampaignDtoList(bannerCampaignList);

			for (BannerCampaignDto bannerCampaignDto : bannerCampaignResponses) {
				bannerCampaignDto.setImages(bannerImageService.getBannerImagesByBannerId(bannerCampaignDto.getId()));
			}

			result.setResponse(Optional.of(bannerCampaignResponses));
		} catch (Exception e) {
			LOGGER.error("Listing banner campaigns failed. {}", e.getMessage());
			result.setError(Optional.of(new ErrorResponse("Listing banner campaigns failed.")));
		}
		return result;
	}

	public BannerCampaignResponse<BannerCampaignDto> updateBannerCampaign(BannerCampaignDto updateBannerCampaignDto) {
		LOGGER.info("Updating banner campaign.");

		BannerCampaignResponse<BannerCampaignDto> result = new BannerCampaignResponse<>();

		try {

			LOGGER.info("Trying to get current banner campaign.");
			Optional<BannerCampaign> currentBannerCampaign = bannerCampaignRepository
					.findActiveById((updateBannerCampaignDto.getId()));
			LOGGER.info("currentBannerCampaign: {}", currentBannerCampaign);

			if (currentBannerCampaign.isEmpty()) {
				LOGGER.info("Banner campaign not found.");
				result.setError(Optional.of(new ErrorResponse("Banner campaign not found.")));
				return result;

			}

			updateBannerCampaignDto = BannerCampaignDto.bannerCampaignDTOFactory(updateBannerCampaignDto, maxSize);

			BannerCampaign bannerCampaign = OrtemConverterUtil
					.convertBannerCampaignDtoToBannerCampaign(updateBannerCampaignDto);

			LOGGER.info("Checking period.");
			List<BannerCampaign> overLappingCampaigns = bannerCampaignRepository
					.bannerCampaignOverlap(bannerCampaign.getCampaignFrom(), bannerCampaign.getCampaignTo());
			LOGGER.info("overLappingCampaign: {}", overLappingCampaigns);

			if (overLappingCampaigns.size() > 0) {
				for (BannerCampaign overLappingCampaign : overLappingCampaigns) {
					if (overLappingCampaign.getId() != bannerCampaign.getId()) {
						LOGGER.info("Another overlapping banner campaign peresent.");
						result.setError(Optional.of(new ErrorResponse("Another overlapping banner campaign peresent: "
								+ overLappingCampaign.getId() + " - " + bannerCampaign.getId())));
						return result;
					}
				}
			}

			List<BannerImageDto> newImages = new ArrayList<>();

			List<String> deletableImages = new ArrayList<>();
			for (BannerImage bannerImage : currentBannerCampaign.get().getBannerImageList()) {
				deletableImages.add(bannerImage.getUrl());
			}

			LOGGER.info("deletableImages: ALL {}", deletableImages);

			for (BannerImageDto bannerImageDetail : updateBannerCampaignDto.getImages()) {
				LOGGER.info("Processing bannerImageDetail: {}", bannerImageDetail);
				if (bannerImageDetail.getId() != null && bannerImageDetail.getImageData() == null) {
					LOGGER.info("Preservable image arrived.");
					deletableImages.remove(bannerImageDetail.getUrl());
				}
				if (bannerImageDetail.getId() != null && bannerImageDetail.getImageData() != null
						&& !bannerImageDetail.getImageData().isBlank()) {
					LOGGER.info("Updated banner image arrived.");
					newImages.add(bannerImageDetail);
				}
				if (bannerImageDetail.getId() == null && bannerImageDetail.getImageData() != null
						&& !bannerImageDetail.getImageData().isBlank()) {
					LOGGER.info("New banner image arrived. Trying to store.");
					newImages.add(bannerImageDetail);
				}
			}

			LOGGER.info("deletableImages: TODELETE{}", deletableImages);
			for (String url : deletableImages) {
				LOGGER.info("Deleting {}.", url);
				bannerImageService.deleteCampaignImage(url);
			}

			LOGGER.info("Trying to store banner images.");
			List<String> urls = storeImages(updateBannerCampaignDto.getImages());

			LOGGER.info("Trying to save updated banner campaign. {}", bannerCampaign.toString());
			bannerCampaignRepository.save(bannerCampaign);

			LOGGER.info("Trying to store banner image urls.");
			bannerImageService.storeUrls(bannerCampaign, urls);

			result.setResponse(Optional.of(updateBannerCampaignDto));
		} catch (Exception e) {
			LOGGER.error("Updating banner campaign failed. {}", e.getMessage());
			result.setError(Optional.of(new ErrorResponse("Updating banner campaign failed.")));
		}

		return result;
	}

	public BannerCampaignResponse<String> deleteBannerCampaign(String bannerCampaignId) {
		LOGGER.info("Deleting banner campaign.");

		BannerCampaignResponse<String> result = new BannerCampaignResponse<>();

		try {

			LOGGER.info("Trying to get current banner campaign.");
			Optional<BannerCampaign> currentBannerCampaign = bannerCampaignRepository
					.findActiveById(Long.parseLong(bannerCampaignId));
			LOGGER.info("currentBannerCampaign: {}", currentBannerCampaign);

			if (currentBannerCampaign.isEmpty()) {
				LOGGER.info("Banner campaign not found.");
				result.setError(Optional.of(new ErrorResponse("Banner campaign not found.")));
				return result;
			}

			LOGGER.info("Trying to delete banner campaign images.");
			bannerImageService.deleteAllCampaignImages(Long.parseLong(bannerCampaignId));

			LOGGER.info("Trying to delete banner campaign.");
			bannerCampaignRepository.deleteCampaign(Long.parseLong(bannerCampaignId));

			result.setResponse(Optional.of(bannerCampaignId));
		} catch (Exception e) {
			LOGGER.error("Deleting banner campaign failed. {}", e.getMessage());
			result.setError(Optional.of(new ErrorResponse("Deleting banner campaign failed.")));
		}

		return result;
	}
}
