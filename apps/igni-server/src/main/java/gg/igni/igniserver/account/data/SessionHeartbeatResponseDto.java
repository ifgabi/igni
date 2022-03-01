package gg.igni.igniserver.account.data;

public class SessionHeartbeatResponseDto {
  private boolean alive;
  private UserDto user;

  public boolean isAlive() {
    return alive;
  }

  public void setAlive(boolean alive) {
    this.alive = alive;
  }

  public SessionHeartbeatResponseDto(boolean alive)
  {
    this.alive = alive;
  }

  public UserDto getUser() {
    return user;
  }

  public void setUser(UserDto user) {
    this.user = user;
  }



}
