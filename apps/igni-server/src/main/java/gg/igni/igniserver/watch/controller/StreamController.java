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
import gg.igni.igniserver.watch.data.EmbedReqDto;
import gg.igni.igniserver.watch.data.EmbedRespDto;
import gg.igni.igniserver.watch.data.EmbedsRespDto;
import gg.igni.igniserver.watch.data.StreamHeartbeatReqDto;
import gg.igni.igniserver.watch.data.StreamHeartbeatRespDto;
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
  public ResponseEntity<EmbedsRespDto> getStreams(@PathVariable int page) {

    EmbedsRespDto send = new EmbedsRespDto();
    Page<EmbedDto> embeds = embedService.getEmbedsForPage(page);
    send.setEmbeds(embeds.toList());
    send.setNumberOfPages(embeds.getTotalPages());

    ResponseEntity<EmbedsRespDto> re = new ResponseEntity<EmbedsRespDto>(send, HttpStatus.OK);
    return re;
  }


  @GetMapping(value="/stream/{id}")
  public ResponseEntity<EmbedRespDto> getStream(@PathVariable Long id) {

    EmbedRespDto send = new EmbedRespDto();
    EmbedDto embed = embedService.getEmbedForId(id);
    send.setEmbed(embed);

    ResponseEntity<EmbedRespDto> re = new ResponseEntity<EmbedRespDto>(send, HttpStatus.OK);
    return re;
  }

  @PostMapping(value="/addstream")
  public ResponseEntity<EmbedRespDto> postStream(@RequestBody EmbedReqDto embedRecvData)
  {
    EmbedRespDto send = new EmbedRespDto();
    send.setEmbed(embedService.createOrGetEmbed(embedRecvData.getEmbedSiteId(), embedRecvData.getToken()));

    ResponseEntity<EmbedRespDto> resp = new ResponseEntity<EmbedRespDto>(send, HttpStatus.OK);
    return resp;
  }

  @PostMapping(value = "/heartbeatStream")
  @Transactional
  public ResponseEntity<StreamHeartbeatRespDto> postHeartbeatStream(@RequestBody StreamHeartbeatReqDto streamHeartBeatRecv, HttpServletRequest request)
  {
    StreamHeartbeatRespDto data = new StreamHeartbeatRespDto();

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

    return new ResponseEntity<StreamHeartbeatRespDto>(data, HttpStatus.OK);
  }

}
