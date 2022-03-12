package gg.igni.igniserver.watch.data;

import java.util.Date;

public class EmbedCountElementsDto {
  private Long cts;

  private Date timeout;

  public EmbedCountElementsDto(Long cts, Date timeout) {
    this.cts = cts;
    this.timeout = timeout;
  }

  public Long getCts() {
    return cts;
  }

  public void setCts(Long cts) {
    this.cts = cts;
  }

  public Date getTimeout() {
    return timeout;
  }

  public void setTimeout(Date timeout) {
    this.timeout = timeout;
  }

}
