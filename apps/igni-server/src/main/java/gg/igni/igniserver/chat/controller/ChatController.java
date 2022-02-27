package gg.igni.igniserver.chat.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import gg.igni.igniserver.chat.data.ChatMessageDto;
import gg.igni.igniserver.chat.data.ChatMessageRecvDto;
import gg.igni.igniserver.chat.data.ChatMessageSendDto;
import gg.igni.igniserver.chat.data.UserDto;
import gg.igni.igniserver.chat.service.ChatService;
import gg.igni.igniserver.model.User;

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
