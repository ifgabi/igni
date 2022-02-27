package gg.igni.igniserver.watch.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import gg.igni.igniserver.model.Embed;

@Repository
public interface EmbedRepositoryPageable extends PagingAndSortingRepository<Embed, Long> {
  // public Collection<Embed> findAll(Pageable pageable);
}
