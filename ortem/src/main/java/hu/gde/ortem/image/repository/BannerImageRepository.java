package hu.gde.ortem.image.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import hu.gde.ortem.image.model.BannerImage;
import jakarta.transaction.Transactional;

/*
* @author  Noel Solyom
*/

public interface BannerImageRepository extends CrudRepository<BannerImage, Long> {

	@Query("SELECT bi FROM BannerImage bi WHERE bi.bannerCampaign.id = :bannerCampaignId AND bi.deleted = false")
	public Iterable<BannerImage> getBannerImagesByBannerId(@Param("bannerCampaignId") Long bannerCampaignId);

	@Modifying
	@Transactional
	@Query("UPDATE BannerImage bi SET bi.deleted = true WHERE bi.id = :id")
	public Integer deleteImage(@Param("id") Long id);

	@Query("SELECT bi FROM BannerImage bi WHERE bi.url = :url AND bi.deleted = false")
	public Optional<BannerImage> findBannerImageByUrl(@Param("url") String url);

}