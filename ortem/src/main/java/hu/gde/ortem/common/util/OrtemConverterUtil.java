package hu.gde.ortem.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.gde.ortem.auth.model.AuthUserDto;
import hu.gde.ortem.campaign.model.BannerCampaign;
import hu.gde.ortem.campaign.model.BannerCampaignDto;
import hu.gde.ortem.image.model.BannerImage;
import hu.gde.ortem.image.model.BannerImageDto;
import hu.gde.ortem.user.model.User;
import hu.gde.ortem.user.model.UserDto;
import hu.gde.ortem.userrole.model.UserRole;
import hu.gde.ortem.userrole.model.UserRoleDto;

public class OrtemConverterUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrtemConverterUtil.class);

	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public static List<UserRoleDto> convertUserRoleListToDtoList(List<UserRole> userRoleList) {
		List<UserRoleDto> result = new ArrayList<>();

		try {
			result = userRoleList.stream().map(userRole -> {
				UserRoleDto userRoleDto = new UserRoleDto();
				userRoleDto.setId(userRole.getId());
				userRoleDto.setRoleName(userRole.getRoleName().name());

				return userRoleDto;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Error converting user role list to dto: {}", e.getMessage());
		}

		return result;
	}

	public static List<UserDto> convertUserListToDtoList(List<User> userList) {
		List<UserDto> result = new ArrayList<>();

		try {
			result = userList.stream().map(user -> {
				UserDto userDto = new UserDto();
				userDto.setId(user.getId());
				userDto.setEmail(user.getEmail());
				userDto.setName(user.getName());
				userDto.setRole(user.getRole().getRoleName().toString());

				return userDto;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Error converting user list to dto: {}", e.getMessage());
		}

		return result;
	}

	public static UserDto convertUserToDto(Optional<User> user) {

		if (user.isPresent()) {
			UserDto result = new UserDto();

			result.setId(user.get().getId());
			result.setEmail(user.get().getEmail());
			result.setName(user.get().getName());
			result.setRole(user.get().getRole().getRoleName().toString());

			return result;

		} else {
			return null;
		}
	}

	public static User convertAuthUserDtoToUser(AuthUserDto authUserDto) {
		LOGGER.info("Trying to convert auth-user-dto to user.");
		try {
			User user = new User();

			user.setEmail(authUserDto.getUsername());
			user.setPassword(encodePassword(authUserDto.getPassword()));

			LOGGER.info("Converting auth-user-dto to user complete.");
			return user;
		} catch (Exception e) {
			LOGGER.error("Converting auth-user-dto to user failed. {}", e.getMessage());
			return null;
		}
	}

	public static String encodePassword(String password) {
		LOGGER.info("Trying to encode password.");
		try {
			String encodedPassword = null;
			final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
			if (password != null && !password.isBlank()) {
				final byte[] hashbytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
				encodedPassword = bytesToHex(hashbytes);
				LOGGER.info("Encoding password complete.");
			}
			return encodedPassword;
		} catch (Exception e) {
			LOGGER.error("Encoding password failed. {}", e.getMessage());
			return null;
		}
	}

	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static BannerImageDto convertImageToDto(BannerImage bannerImage) {

		if (bannerImage != null) {
			BannerImageDto result = new BannerImageDto();

			result.setId(bannerImage.getId());
			result.setImageOrder(bannerImage.getImageOrder());
			result.setUrl(bannerImage.getUrl());
			return result;

		} else {
			return null;
		}
	}

	public static List<BannerImageDto> convertImageListToDtoList(List<BannerImage> bannerImageList) {

		List<BannerImageDto> result = new ArrayList<>();
		if (bannerImageList != null && bannerImageList.size() > 0) {
			for (BannerImage bannerImage : bannerImageList) {
				result.add(convertImageToDto(bannerImage));
			}
		}

		return result;
	}

	public static BannerCampaignDto convertCampaignToDto(Optional<BannerCampaign> bannerCampaign) {

		if (bannerCampaign.isPresent()) {
			BannerCampaignDto result = new BannerCampaignDto();

			result.setId(bannerCampaign.get().getId());
			result.setDescription(bannerCampaign.get().getDescription());
			result.setRefreshRate(bannerCampaign.get().getRefreshRate());
			result.setCampaignFromDate(bannerCampaign.get().getCampaignFrom().toString().split("T")[0]);
			result.setCampaignFromTime(bannerCampaign.get().getCampaignFrom().toString().split("T")[1]);
			result.setCampaignToDate(bannerCampaign.get().getCampaignTo().toString().split("T")[0]);
			result.setCampaignToTime(bannerCampaign.get().getCampaignTo().toString().split("T")[1]);
			result.setImages(convertImageListToDtoList(bannerCampaign.get().getBannerImageList()));

			return result;

		} else {
			return null;
		}
	}

	public static List<BannerImageDto> convertBannerImageListToBannerImageResponseList(
			List<BannerImage> bannerImageList) {
		LOGGER.info("Trying to convert banner-image-list to banner-image-response list.");
		try {
			List<BannerImageDto> result = bannerImageList.stream().map(bannerImage -> {
				BannerImageDto bannerImageResponse = new BannerImageDto();

				bannerImageResponse.setId(bannerImage.getId());
				bannerImageResponse.setImageOrder(bannerImage.getImageOrder());
				bannerImageResponse.setUrl(bannerImage.getUrl());

				return bannerImageResponse;
			}).collect(Collectors.toList());
			LOGGER.info("Converting banner-image-list to banner-image-response-list complete.");
			return result;
		} catch (Exception e) {
			LOGGER.error("Converting banner-image-list to banner-image-response failed. {}", e.getMessage());
			return new ArrayList<>();
		}
	}

	public static BannerCampaign convertBannerCampaignDtoToBannerCampaign(BannerCampaignDto bannerCampaignDto) {
		LOGGER.info("Trying to convert banner-campaign-dto to banner-campaign.");
		try {
			BannerCampaign bannerCampaign = new BannerCampaign();

			bannerCampaign.setId(bannerCampaignDto.getId());
			bannerCampaign.setCampaignFrom(LocalDateTime.parse(
					bannerCampaignDto.getCampaignFromDate() + " " + bannerCampaignDto.getCampaignFromTime(),
					formatter));
			bannerCampaign.setCampaignTo(LocalDateTime.parse(
					bannerCampaignDto.getCampaignToDate() + " " + bannerCampaignDto.getCampaignToTime(), formatter));
			bannerCampaign.setDescription(bannerCampaignDto.getDescription());
			if (bannerCampaignDto.getRefreshRate() != null) {
				bannerCampaign.setRefreshRate(bannerCampaignDto.getRefreshRate());
			} else {
				bannerCampaign.setRefreshRate(10);
			}

			LOGGER.info("Converting banner-campaign-dto to banner-campaign complete.");
			return bannerCampaign;
		} catch (Exception e) {
			LOGGER.error("Converting banner-campaign-dto to banner-campaign failed. {}", e.getMessage());
			throw e;
		}
	}

	public static List<BannerCampaignDto> convertBannerCampaignListToBannerCampaignDtoList(
			List<BannerCampaign> bannerCampaignList) {
		LOGGER.info("Trying to convert banner-campaign-list to banner-campaign-dto-list.");
		try {
			List<BannerCampaignDto> result = bannerCampaignList.stream().map(bannerCampaign -> {
				BannerCampaignDto bannerCampaignResponse = new BannerCampaignDto();

				bannerCampaignResponse.setId(bannerCampaign.getId());
				bannerCampaignResponse.setCampaignFromDate(bannerCampaign.getCampaignFrom().toString().split("T")[0]);
				bannerCampaignResponse.setCampaignFromTime(bannerCampaign.getCampaignFrom().toString().split("T")[1]);
				bannerCampaignResponse.setCampaignToDate(bannerCampaign.getCampaignTo().toString().split("T")[0]);
				bannerCampaignResponse.setCampaignToTime(bannerCampaign.getCampaignTo().toString().split("T")[1]);
				bannerCampaignResponse.setDescription(bannerCampaign.getDescription());
				bannerCampaignResponse.setRefreshRate(bannerCampaign.getRefreshRate());

				return bannerCampaignResponse;
			}).collect(Collectors.toList());
			LOGGER.info("Converting banner-campaign-list to banner-campaign-response-list complete.");
			return result;
		} catch (Exception e) {
			LOGGER.error("Converting banner-campaign-list to banner-campaign-response-list failed. {}", e.getMessage());
			throw e;
		}
	}

}
