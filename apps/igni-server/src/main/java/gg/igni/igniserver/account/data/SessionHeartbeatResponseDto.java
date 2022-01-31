package gg.igni.igniserver.account.data;

public class SessionHeartbeatResponseDto {
  private boolean alive;

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

}
