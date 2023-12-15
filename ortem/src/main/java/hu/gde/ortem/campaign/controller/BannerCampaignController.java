package hu.gde.ortem.campaign.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import hu.gde.ortem.campaign.model.BannerCampaignDto;
import hu.gde.ortem.campaign.model.BannerCampaignResponse;
import hu.gde.ortem.campaign.service.BannerCampaignService;
import hu.gde.ortem.common.auth.AdminRole;
import hu.gde.ortem.common.auth.Authenticated;
import hu.gde.ortem.common.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

/*
* @author  Noel Solyom
*/

@RestController
@RequestMapping(value = "/api/admin")
public class BannerCampaignController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BannerCampaignController.class);

	@Value("${ortem.server.image.url.path}")
	private String IMAGE_URL_PATH;

	@Value("${ortem.server.image.store.path}")
	private String IMAGE_STORE_PATH;

	@Autowired
	private BannerCampaignService bannerCampaignService;

	@Authenticated
	@AdminRole
	@PostMapping(value = "/banner", consumes = "application/json", produces = "application/json")
	public ResponseEntity<BannerCampaignResponse<BannerCampaignDto>> createBannerCampaign(
			@RequestBody BannerCampaignDto bannerCampaignDto) {

		LOGGER.info("Create new banner camaign request arrived: {}", bannerCampaignDto);
		BannerCampaignResponse<BannerCampaignDto> response = bannerCampaignService
				.createBannerCampaign(bannerCampaignDto);

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Authenticated
	@GetMapping(value = "/banner", produces = "application/json")
	public ResponseEntity<BannerCampaignResponse<List<BannerCampaignDto>>> getBannerCampaigns() {
		LOGGER.info("List banner campaign request arrived.");
		BannerCampaignResponse<List<BannerCampaignDto>> response = bannerCampaignService.getBannerCampaigns();

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Authenticated
	@AdminRole
	@PatchMapping(value = "/banner", consumes = "application/json", produces = "application/json")
	public ResponseEntity<BannerCampaignResponse<BannerCampaignDto>> updateBannerCampaign(
			@RequestBody BannerCampaignDto updateBannerCampaignDto) {
		LOGGER.info("Update banner campaign request arrived.");
		BannerCampaignResponse<BannerCampaignDto> response = bannerCampaignService
				.updateBannerCampaign(updateBannerCampaignDto);

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Authenticated
	@AdminRole
	@DeleteMapping(value = "/banner/{bannerCampaignId}", produces = "application/json")
	public ResponseEntity<BannerCampaignResponse<String>> deleteBannerCampaign(@PathVariable String bannerCampaignId) {
		LOGGER.info("Delete banner campaign by id request arrived.");
		BannerCampaignResponse<String> response = bannerCampaignService.deleteBannerCampaign(bannerCampaignId);

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping(value = "/banner/current", produces = "application/json")
	public ResponseEntity<BannerCampaignResponse<BannerCampaignDto>> getCurrentCampaign() {

		LOGGER.info("Trying to get current bannerCampaign.");

		BannerCampaignResponse<BannerCampaignDto> response = new BannerCampaignResponse<>();

		try {
			response = bannerCampaignService.getCurrentCampaign();
		} catch (Exception e) {
			LOGGER.error("Getting current banner campaign failed. {}", e.getMessage());
			response.setError(Optional.of(new ErrorResponse("Getting current campaign failed.")));
			return ResponseEntity.internalServerError().body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping(path = "images/**")
	public ResponseEntity<byte[]> getImages(HttpServletRequest request) {

		LOGGER.info("Trying to send image.");

		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

		if (restOfTheUrl != null) {
			LOGGER.info("restOfTheUrl: {}", restOfTheUrl);

			String imageName = restOfTheUrl.substring(restOfTheUrl.indexOf(IMAGE_URL_PATH) + IMAGE_URL_PATH.length());

			LOGGER.info("imageName: {}", imageName);
			byte[] image = null;
			try {
				image = Files.readAllBytes(Paths.get(IMAGE_STORE_PATH + "/" + imageName));
				String type = Files.probeContentType(Paths.get(IMAGE_STORE_PATH + "/" + imageName));
				MediaType mType = MediaType.valueOf(type);

				return ResponseEntity.ok().contentType(mType).body(image);

			} catch (IOException e) {
				LOGGER.warn("Can load image: " + restOfTheUrl + " " + e.getMessage());
				return ResponseEntity.notFound().build();
			}

		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
