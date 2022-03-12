package gg.igni.igniserver.watch.data;

import java.util.Date;

import gg.igni.igniserver.model.Embed;

public class EmbedPageCountDto {
  private Embed embed;
  private Long viewCount;
  private Date timeoutBumpDate;

  public EmbedPageCountDto(Embed embed, Long viewCount, Date timeoutBumpDate) {
    this.embed = embed;
    this.viewCount = viewCount;
    this.timeoutBumpDate = timeoutBumpDate;
  }
  public Embed getEmbed() {
    return embed;
  }
  public void setEmbed(Embed embed) {
    this.embed = embed;
  }
  public Long getViewCount() {
    return viewCount;
  }
  public void setViewCount(Long viewCount) {
    this.viewCount = viewCount;
  }
  public Date getTimeoutBumpDate() {
    return timeoutBumpDate;
  }
  public void setTimeoutBumpDate(Date timeoutBumpDate) {
    this.timeoutBumpDate = timeoutBumpDate;
  }



}
