package gg.igni.igniserver.watch.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gg.igni.igniserver.model.Embed;
import gg.igni.igniserver.model.EmbedSite;
import gg.igni.igniserver.watch.data.EmbedCountElementsDto;
import gg.igni.igniserver.watch.data.EmbedDto;
import gg.igni.igniserver.watch.data.EmbedPageCountDto;
import gg.igni.igniserver.watch.repository.EmbedRepository;
// import gg.igni.igniserver.watch.repository.EmbedRepositoryPageable;
import gg.igni.igniserver.watch.repository.EmbedRepositoryPageable;

@Service
public class EmbedService {

  @Autowired
  private EmbedRepositoryPageable embedRepositoryPageable;

  @Autowired
  private EmbedRepository embedRepository;

  private final int PER_PAGE = 15;

  public Page<EmbedDto> getEmbedsForPage(int pageNum)
  {
    Date currentDate = new Date(System.currentTimeMillis());
    Calendar c = Calendar.getInstance();
    c.setTime(currentDate);
    c.add(Calendar.HOUR, -6);
    //after 6 hours an embed that has not been bumped will not be displayed

    Pageable pageable = PageRequest.of(pageNum, PER_PAGE, Sort.by("viewCount").descending().and(Sort.by("timeout")).descending());

    Page<EmbedPageCountDto> pageDtos = embedRepositoryPageable.findAllFrontPage(c.getTime(), pageable);

    ModelMapper mm = new ModelMapper();

    Page<Embed> page = pageDtos.map((embedPageCountDto) -> {
      Embed result = embedPageCountDto.getEmbed() != null? mm.map(embedPageCountDto.getEmbed(), Embed.class) : null;
      result.setViewCount(embedPageCountDto.getViewCount());
      return result;
    });

    Page<EmbedDto> returnData = page.map((embed) -> embed != null? mm.map(embed, EmbedDto.class) : null);

    return returnData;
  }

  public EmbedDto getEmbedForId(Long id) {
      Optional<Embed> embed = embedRepository.findById(id);

      ModelMapper mm = new ModelMapper();

      if(embed.isEmpty() == false)
      {
        EmbedDto returnData = mm.map(embed.orElse(null), EmbedDto.class);
        return returnData;
      }

      return null;
  }

  public EmbedDto createOrGetEmbed(Long embedSiteId, String token) {

    Embed embedResp = embedRepository.findByTokenAndEmbedSite_Id(token, embedSiteId).orElse(null);

    if(embedResp == null)
    {
      Embed emb = new Embed();
      EmbedSite embSite = new EmbedSite();
      embSite.setId(embedSiteId);
      emb.setEmbedSite(embSite);
      emb.setMemo("Empty memo.");
      emb.setToken(token);
      embedRepository.save(emb);
      embedResp = emb;
    }

    ModelMapper mm = new ModelMapper();
    EmbedDto embedDtoResp = mm.map(embedResp, EmbedDto.class);

    return embedDtoResp;
  }

}
