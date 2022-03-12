package gg.igni.igniserver.watch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gg.igni.igniserver.model.Embed;

@Repository
public interface EmbedRepository extends JpaRepository<Embed, Long>{

  // @Query(value = "SELECT emb FROM Embed emb WHERE emb.embedsite_id = ?1 and emb.token = ?2")
  // Optional<Embed> getByEmbedSiteIdAndToken(Long embedSiteId, String token);
  Optional<Embed> findByTokenAndEmbedSite_Id(String token, Long id);

  @Query(value = "UPDATE igni.table_embeds emb SET timeout_bump_date = CURRENT_TIMESTAMP() WHERE emb.id = :embedId ;"
  ,nativeQuery = true)
  @Modifying
  void updateTimeoutUpdate(@Param(value = "embedId") Long embedId);

}
