package gg.igni.igniserver.watch.repository;

import java.util.Collection;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import gg.igni.igniserver.watch.model.Embed;

@Repository
public interface EmbedRepository extends PagingAndSortingRepository<Embed, Long> {
  // public Collection<Embed> findAll(Pageable pageable);
}
