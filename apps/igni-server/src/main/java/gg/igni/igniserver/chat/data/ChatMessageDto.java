package gg.igni.igniserver.chat.data;

public class ChatMessageDto {

  private Long id;
  private UserDto user;
  private String message;

  public Long getId() {
    return this.id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public UserDto getUser() {
    return user;
  }
  public void setUser(UserDto user) {
    this.user = user;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }



}
