package gg.igni.igniserver.watch.repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import gg.igni.igniserver.model.Embed;
import gg.igni.igniserver.watch.data.EmbedCountElementsDto;
import gg.igni.igniserver.watch.data.EmbedPageCountDto;

@Repository
public class EmbedRepositoryPageable {

  @Autowired
  private EntityManager entityManager;

  public Page<EmbedPageCountDto> findAllFrontPage(Date maxDate, Pageable pageable)
  {

    String order = StringUtils.collectionToCommaDelimitedString(
      StreamSupport.stream(pageable.getSort().spliterator(), false)
          .map(o -> o.getProperty() + " " + o.getDirection())
          .collect(Collectors.toList()));

    List<EmbedPageCountDto> prePageDtos = entityManager.createQuery(String.format("SELECT NEW gg.igni.igniserver.watch.data.EmbedPageCountDto(emb AS embedded, COUNT(v) AS viewCount, emb.timeoutBumpDate AS timeout) FROM Embed AS emb LEFT JOIN emb.views AS v GROUP BY emb.id HAVING COUNT(v) > 0 OR :maxDate <= emb.timeoutBumpDate ORDER BY %s", order), EmbedPageCountDto.class)
    .setParameter("maxDate", maxDate)
    .setFirstResult(pageable.getPageSize() * pageable.getPageNumber())
    .setMaxResults(pageable.getPageSize())
    .getResultList();

    List<EmbedCountElementsDto> count = entityManager.createQuery("SELECT NEW gg.igni.igniserver.watch.data.EmbedCountElementsDto(COUNT(emb) AS cts, emb.timeoutBumpDate AS timeout) FROM Embed AS emb LEFT JOIN emb.views AS v GROUP BY emb.id HAVING COUNT(v) > 0 OR :maxDate <= emb.timeoutBumpDate", EmbedCountElementsDto.class)
      .setParameter("maxDate", maxDate)
      .getResultList();

    Page<EmbedPageCountDto> pageDtos = new PageImpl<EmbedPageCountDto>(prePageDtos, pageable, count.size());

    return pageDtos;
  }
}
