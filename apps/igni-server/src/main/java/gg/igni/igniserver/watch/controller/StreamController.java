package gg.igni.igniserver.watch.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gg.igni.igniserver.watch.data.EmbedDto;
import gg.igni.igniserver.watch.data.EmbedSendDto;
import gg.igni.igniserver.watch.service.EmbedService;


@Controller
public class StreamController {

  @Autowired
  private EmbedService embedService;

  @GetMapping(value="/streams/{page}")
  @ResponseBody
  public ResponseEntity<EmbedSendDto> getStreams(@PathVariable int page) {

    EmbedSendDto send = new EmbedSendDto();
    List<EmbedDto> embeds = embedService.getEmbedsForPage(page);
    send.setEmbeds(embeds);

    ResponseEntity<EmbedSendDto> re = new ResponseEntity<EmbedSendDto>(send, HttpStatus.OK);
    return re;
  }

}
