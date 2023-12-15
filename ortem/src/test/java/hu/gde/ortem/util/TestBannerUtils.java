package hu.gde.ortem.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import hu.gde.ortem.campaign.model.BannerCampaign;
import hu.gde.ortem.campaign.model.BannerCampaignDto;
import hu.gde.ortem.common.util.OrtemConverterUtil;

@SpringBootTest
public class TestBannerUtils {

	@Test
	public void testConvertCampaignToDtoTime() {

		BannerCampaign bannerCampaign = new BannerCampaign();
		bannerCampaign.setCampaignFrom(LocalDateTime.of(2023, 12, 5, 10, 0, 0));
		bannerCampaign.setCampaignTo(LocalDateTime.of(2023, 12, 6, 20, 30, 0));

		BannerCampaignDto bannerCampaignDto = OrtemConverterUtil.convertCampaignToDto(Optional.of(bannerCampaign));

		assertEquals(bannerCampaignDto.getCampaignFromDate(), "2023-12-05");
		assertEquals(bannerCampaignDto.getCampaignFromTime(), "10:00");
		assertEquals(bannerCampaignDto.getCampaignToDate(), "2023-12-06");
		assertEquals(bannerCampaignDto.getCampaignToTime(), "20:30");
	}

	@Test
	public void testConvertCampaignDtoToCampaignTime() {

		BannerCampaignDto bannerCampaignDto = new BannerCampaignDto();
		bannerCampaignDto.setCampaignFromDate("2023-12-02");
		bannerCampaignDto.setCampaignFromTime("11:00");
		bannerCampaignDto.setCampaignToDate("2023-12-14");
		bannerCampaignDto.setCampaignToTime("23:59");

		BannerCampaign bannerCampaign = OrtemConverterUtil.convertBannerCampaignDtoToBannerCampaign(bannerCampaignDto);

		assertEquals(bannerCampaign.getCampaignFrom(), LocalDateTime.of(2023, 12, 2, 11, 0, 0));
		assertEquals(bannerCampaign.getCampaignTo(), LocalDateTime.of(2023, 12, 14, 23, 59, 0));

	}

}
