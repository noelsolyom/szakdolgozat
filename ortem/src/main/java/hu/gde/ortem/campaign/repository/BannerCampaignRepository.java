package hu.gde.ortem.campaign.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import hu.gde.ortem.campaign.model.BannerCampaign;
import jakarta.transaction.Transactional;

/*
* @author  Noel Solyom
*/

public interface BannerCampaignRepository extends CrudRepository<BannerCampaign, Long> {

	@Query("SELECT bc FROM BannerCampaign bc WHERE ((:campaignFrom BETWEEN bc.campaignFrom AND bc.campaignTo) AND bc.deleted = false) OR ((:campaignTo BETWEEN bc.campaignFrom AND bc.campaignTo) AND bc.deleted = false) OR ((bc.campaignFrom < :campaignFrom AND bc.campaignTo > :campaignTo) AND bc.deleted = false)")
	public List<BannerCampaign> bannerCampaignOverlap(@Param("campaignFrom") LocalDateTime campaignFrom,
			@Param("campaignTo") LocalDateTime campaignTo);

	@Query("SELECT bc FROM BannerCampaign bc WHERE bc.id = :id AND bc.deleted = false")
	public Optional<BannerCampaign> findActiveById(@Param("id") Long id);

	@Query("SELECT bc FROM BannerCampaign bc WHERE bc.deleted = false ORDER BY bc.campaignFrom")
	public Iterable<BannerCampaign> findAllActive();

	@Modifying
	@Transactional
	@Query("UPDATE BannerCampaign bc SET bc.deleted = true WHERE bc.id = :id")
	public Integer deleteCampaign(@Param("id") Long id);

	@Query("SELECT bc FROM BannerCampaign bc WHERE CURRENT_TIMESTAMP BETWEEN bc.campaignFrom AND bc.campaignTo AND bc.deleted = false")
	public Optional<BannerCampaign> getCurrentBannerCampaign();
}
