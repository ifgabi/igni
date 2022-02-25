package gg.igni.igniserver.watch.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import gg.igni.igniserver.watch.data.StreamHeartbeatRecvDto;
import gg.igni.igniserver.watch.data.StreamHeartbeatSendDto;
import gg.igni.igniserver.watch.service.EmbedService;


@Controller
public class StreamController {

  @Autowired
  private EmbedService embedService;

  @GetMapping(value="/streams/{page}")
  public ResponseEntity<EmbedsSendDto> getStreams(@PathVariable int page) {

    EmbedsSendDto send = new EmbedsSendDto();
    Page<EmbedDto> embeds = embedService.getEmbedsForPage(page);
    send.setEmbeds(embeds.toList());
    send.setNumberOfPages(embeds.getTotalPages());

    ResponseEntity<EmbedsSendDto> re = new ResponseEntity<EmbedsSendDto>(send, HttpStatus.OK);
    return re;
  }


  @GetMapping(value="/stream/{id}")
  public ResponseEntity<EmbedSendDto> getStream(@PathVariable Long id) {

    EmbedSendDto send = new EmbedSendDto();
    EmbedDto embed = embedService.getEmbedForId(id);
    send.setEmbed(embed);

    ResponseEntity<EmbedSendDto> re = new ResponseEntity<EmbedSendDto>(send, HttpStatus.OK);
    return re;
  }

  @PostMapping(value="/addstream")
  public ResponseEntity<EmbedSendDto> postStream(@RequestBody EmbedRecvDto embedRecvData)
  {
    EmbedSendDto send = new EmbedSendDto();
    send.setEmbed(embedService.createOrGetEmbed(embedRecvData.getEmbedSiteId(), embedRecvData.getToken()));

    ResponseEntity<EmbedSendDto> resp = new ResponseEntity<EmbedSendDto>(send, HttpStatus.OK);
    return resp;
  }

  @PostMapping(value = "/heartbeatStream")
  public ResponseEntity<StreamHeartbeatSendDto> postHeartbeatStream(@RequestBody StreamHeartbeatRecvDto streamHeartBeatRecv)
  {

    //viewservice viewrepository

    return new ResponseEntity<StreamHeartbeatSendDto>(new StreamHeartbeatSendDto(), HttpStatus.OK);
  }

}
