package gg.igni.igniserver.watch.data;

import java.util.Date;

public class EmbedDto {
	private Long id;
  private String token;
  private EmbedSiteDto embedSite;
  private String memo;
  private Long viewCount;
  private Date timeoutBumpDate;

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }
  public EmbedSiteDto getEmbedSite() {
    return embedSite;
  }
  public void setEmbedSite(EmbedSiteDto embedSite) {
    this.embedSite = embedSite;
  }
  public String getMemo() {
    return memo;
  }
  public void setMemo(String memo) {
    this.memo = memo;
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
