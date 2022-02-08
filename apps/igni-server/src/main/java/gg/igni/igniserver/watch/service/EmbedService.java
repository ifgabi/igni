package gg.igni.igniserver.watch.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gg.igni.igniserver.watch.data.EmbedDto;
import gg.igni.igniserver.watch.data.EmbedSendDto;
import gg.igni.igniserver.watch.model.Embed;
import gg.igni.igniserver.watch.model.EmbedSite;
import gg.igni.igniserver.watch.repository.EmbedRepository;
import gg.igni.igniserver.watch.repository.EmbedRepositoryPageable;
import gg.igni.igniserver.watch.repository.EmbedSiteRepository;

@Service
public class EmbedService {
  @Autowired
  private EmbedRepositoryPageable embedRepositoryPageable;

  @Autowired
  private EmbedSiteRepository embedSiteRepository;

  @Autowired
  private EmbedRepository embedRepository;

  private final int PER_PAGE = 12;

  public List<EmbedDto> getEmbedsForPage(int pageNum)
  {
    Pageable pageable = PageRequest.of(pageNum, PER_PAGE);

    Page<Embed> page = embedRepositoryPageable.findAll(pageable);
    Collection<Embed> coll = page.toList();

    ModelMapper mm = new ModelMapper();
    List<EmbedDto> returnData = coll.stream()
      .map((embed) -> embed != null? mm.map(embed, EmbedDto.class) : null)
      .collect(Collectors.toList());

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
