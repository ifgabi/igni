package gg.igni.igniserver.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import gg.igni.igniserver.chat.data.ChatHistorySendDto;
import gg.igni.igniserver.chat.service.ChatService;

@Controller
public class ChatHistoryController {

  @Autowired
  private ChatService chatService;

  @GetMapping("/history")
  public ResponseEntity<ChatHistorySendDto> getHistory() {

    ChatHistorySendDto respData = new ChatHistorySendDto();
    respData.setMessages(chatService.getLastMessages());

    if(respData.getMessages().size() < 0)
    {
      return new ResponseEntity<ChatHistorySendDto>(respData, HttpStatus.NOT_FOUND);
    }

    ResponseEntity<ChatHistorySendDto> re = new ResponseEntity<ChatHistorySendDto>(respData, HttpStatus.OK);

    return re;
  }
}
