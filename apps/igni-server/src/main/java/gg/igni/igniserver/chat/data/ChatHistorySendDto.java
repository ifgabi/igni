package gg.igni.igniserver.chat.data;

import java.util.List;

public class ChatHistorySendDto {
  private List<ChatMessageDto> messages;

  public List<ChatMessageDto> getMessages() {
    return messages;
  }

  public void setMessages(List<ChatMessageDto> messages) {
    this.messages = messages;
  }
}
