package gg.igni.igniserver.watch.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gg.igni.igniserver.watch.data.EmbedDto;
import gg.igni.igniserver.watch.model.Embed;
import gg.igni.igniserver.watch.repository.EmbedRepository;

@Service
public class EmbedService {
  @Autowired
  private EmbedRepository embedRepository;

  private final int PER_PAGE = 10;

  public List<EmbedDto> getEmbedsForPage(int pageNum)
  {
    Pageable pageable = PageRequest.of(pageNum, PER_PAGE);

    Page<Embed> page = embedRepository.findAll(pageable);
    Collection<Embed> coll = page.toList();

    ModelMapper mm = new ModelMapper();
    List<EmbedDto> returnData = coll.stream()
      .map((embed) -> mm.map(embed, EmbedDto.class))
      .collect(Collectors.toList());

    return returnData;
  }

}
