package gg.igni.igniserver.watch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import gg.igni.igniserver.watch.model.Embed;

@Repository
public interface EmbedRepositoryPageable extends PagingAndSortingRepository<Embed, Long> {
  // public Collection<Embed> findAll(Pageable pageable);
}
