package gg.igni.igniserver.watch.data;

public class EmbedReqDto {
  private String token;
  private Long embedSiteId;

  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }
  public Long getEmbedSiteId() {
    return embedSiteId;
  }
  public void setEmbedSiteId(Long embedSiteId) {
    this.embedSiteId = embedSiteId;
  }


}
