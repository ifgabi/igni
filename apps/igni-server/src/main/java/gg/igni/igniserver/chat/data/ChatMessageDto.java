package gg.igni.igniserver.chat.data;

public class ChatMessageDto {

  private Long Id;
  private UserDto user;
  private String message;
  public Long getId() {
    return Id;
  }
  public void setId(Long id) {
    Id = id;
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
