package gg.igni.igniserver.watch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.igni.igniserver.model.EmbedSite;

@Repository
public interface EmbedSiteRepository extends JpaRepository<EmbedSite, Long>{

}
