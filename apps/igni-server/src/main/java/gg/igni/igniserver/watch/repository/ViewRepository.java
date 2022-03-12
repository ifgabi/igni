package gg.igni.igniserver.watch.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gg.igni.igniserver.model.View;

@Repository
public interface ViewRepository extends JpaRepository<View, Long>{

  @Query(value = "SELECT COUNT(v.primaryKey.user.id) FROM View AS v WHERE v.primaryKey.embed.id = :embedId AND v.lastUpdate > :minDate GROUP BY v.primaryKey.embed.id")
  public Optional<Integer> getViewCount(@Param("embedId") Long embedId, @Param("minDate") Date minDate);

  @Modifying
  @Query(value = "INSERT INTO igni.table_views (embed_id, user_id, last_update) VALUES (:embedId, :userId, NOW()) ON DUPLICATE KEY UPDATE last_update = NOW();",
    nativeQuery =  true)
  public void updateLastUpdate(@Param("embedId") Long embedId, @Param("userId") Long userId);

  @Modifying
  @Query(value = "DELETE FROM igni.table_views WHERE TIMESTAMPDIFF(SECOND, last_update, NOW()) > 30;",
    nativeQuery = true)
  public void cleanupViews();
}
