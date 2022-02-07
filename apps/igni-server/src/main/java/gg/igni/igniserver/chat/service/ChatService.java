package gg.igni.igniserver.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gg.igni.igniserver.chat.data.ChatMessageDto;
import gg.igni.igniserver.chat.model.ChatMessage;
import gg.igni.igniserver.chat.repositories.ChatMessageRepository;

@Service
public class ChatService {
  @Autowired
  ChatMessageRepository chatMessageRepository;

  public ChatMessageDto logChatMessage(ChatMessageDto chatMessageData)
  {
    ModelMapper mm = new ModelMapper();

    ChatMessage cm = mm.map(chatMessageData, ChatMessage.class);

    ChatMessage savedCm = chatMessageRepository.save(cm);

    ChatMessageDto savedCmDto = mm.map(savedCm, ChatMessageDto.class);

    return savedCmDto;
  }

  public List<ChatMessageDto> getLastMessages() {

    List<ChatMessage> histo = chatMessageRepository.getHistory();

    ModelMapper mm = new ModelMapper();

    List<ChatMessageDto> ret = histo.stream().map(obj -> mm.map(obj, ChatMessageDto.class)).collect(Collectors.toList());

    return ret;
  }
}
