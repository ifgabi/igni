package gg.igni.igniserver.watch.controller;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gg.igni.igniserver.account.repositories.UserRepository;
import gg.igni.igniserver.model.Embed;
import gg.igni.igniserver.model.User;
import gg.igni.igniserver.model.View;
import gg.igni.igniserver.model.ViewKey;
import gg.igni.igniserver.watch.data.EmbedDto;
import gg.igni.igniserver.watch.data.EmbedRecvDto;
import gg.igni.igniserver.watch.data.EmbedSendDto;
import gg.igni.igniserver.watch.data.EmbedsSendDto;
import gg.igni.igniserver.watch.data.StreamHeartbeatRecvDto;
import gg.igni.igniserver.watch.data.StreamHeartbeatSendDto;
import gg.igni.igniserver.watch.repository.EmbedRepository;
import gg.igni.igniserver.watch.repository.ViewRepository;
import gg.igni.igniserver.watch.service.EmbedService;
import gg.igni.igniserver.watch.service.ViewService;


@Controller
public class StreamController {

  @Autowired
  private EmbedService embedService;

  @Autowired
  private ViewService viewService;

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
  @Transactional
  public ResponseEntity<StreamHeartbeatSendDto> postHeartbeatStream(@RequestBody StreamHeartbeatRecvDto streamHeartBeatRecv, HttpServletRequest request)
  {
    StreamHeartbeatSendDto data = new StreamHeartbeatSendDto();

    SecurityContext sc = SecurityContextHolder.getContext();
		Authentication auth = sc.getAuthentication();
		if(auth != null)
			if(!(auth instanceof AnonymousAuthenticationToken))
				if(auth.isAuthenticated())
        {
          User user = (User)auth.getPrincipal();
          //use IP instead of userId
          viewService.bumpUpdate(streamHeartBeatRecv.getEmbedId(), user.getId());
        }

    data.setCount(viewService.getViewCount(streamHeartBeatRecv.getEmbedId()).orElse(0));

    return new ResponseEntity<StreamHeartbeatSendDto>(data, HttpStatus.OK);
  }

  @GetMapping(value="/testData/{id}")
  @Transactional
  @ResponseBody
  public boolean testData(@PathVariable(name = "id") Long id)
  {
    return viewService.testData(id);
  }

}
