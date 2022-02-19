package gg.igni.igniserver.chat.controller;

import org.apache.logging.log4j.message.Message;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import gg.igni.igniserver.chat.data.UserDto;
import gg.igni.igniserver.chat.service.ChatService;
import gg.igni.igniserver.account.model.User;
import gg.igni.igniserver.chat.data.ChatMessageDto;
import gg.igni.igniserver.chat.data.ChatMessageRecvDto;
import gg.igni.igniserver.chat.data.ChatMessageSendDto;

@Controller
public class ChatController {

  @Autowired
  ChatService chatService;

  // @PreAuthorize("hasAuthority('PRIVILEGE_CHAT_USAGE')")
	@MessageMapping("/sendToChannel")
	@SendTo("/channel")
	public ChatMessageSendDto sendMessage(@RequestBody ChatMessageRecvDto msgr,
			SimpMessageHeaderAccessor headerAccessor, Authentication auth) throws Exception {

    User user = (User) auth.getPrincipal();
    if(user != null)
    {
      ChatMessageSendDto sendDto = new ChatMessageSendDto();

      ChatMessageDto cmdto = new ChatMessageDto();
      cmdto.setId(-1l);
      cmdto.setMessage(msgr.getMessage());
      ModelMapper mm = new ModelMapper();
      cmdto.setUser(mm.map(user, UserDto.class));

      //Will have a proper id, lets check
      sendDto.setChatMessage(chatService.logChatMessage(cmdto));

      if(sendDto.getChatMessage().getId() < 0l)
      {
        //TODO throw some exception if the chatmessage entity wasnt persisted
        return null;
      }

      return sendDto;
    }

    return null;
	}
}
