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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gg.igni.igniserver.watch.data.EmbedDto;
import gg.igni.igniserver.watch.data.EmbedRecvDto;
import gg.igni.igniserver.watch.data.EmbedSendDto;
import gg.igni.igniserver.watch.data.EmbedsSendDto;
import gg.igni.igniserver.watch.service.EmbedService;


@Controller
public class StreamController {

  @Autowired
  private EmbedService embedService;

  @GetMapping(value="/streams/{page}")
  @ResponseBody
  public ResponseEntity<EmbedsSendDto> getStreams(@PathVariable int page) {

    EmbedsSendDto send = new EmbedsSendDto();
    List<EmbedDto> embeds = embedService.getEmbedsForPage(page);
    send.setEmbeds(embeds);

    ResponseEntity<EmbedsSendDto> re = new ResponseEntity<EmbedsSendDto>(send, HttpStatus.OK);
    return re;
  }


  @GetMapping(value="/stream/{id}")
  @ResponseBody
  public ResponseEntity<EmbedSendDto> getStream(@PathVariable Long id) {

    EmbedSendDto send = new EmbedSendDto();
    EmbedDto embed = embedService.getEmbedForId(id);
    send.setEmbed(embed);

    ResponseEntity<EmbedSendDto> re = new ResponseEntity<EmbedSendDto>(send, HttpStatus.OK);
    return re;
  }

  @PostMapping(value="/addstream")
  @ResponseBody
  public ResponseEntity<EmbedSendDto> postStream(@RequestBody EmbedRecvDto embedRecvData)
  {
    System.out.println("WE GOT HERE!");
    EmbedSendDto send = new EmbedSendDto();
    send.setEmbed(embedService.createOrGetEmbed(embedRecvData.getEmbedSiteId(), embedRecvData.getToken()));

    ResponseEntity<EmbedSendDto> resp = new ResponseEntity<EmbedSendDto>(send, HttpStatus.OK);
    return resp;
  }

}
