package gg.igni.igniserver.watch.data;

public class EmbedDto {
	private Long id;
  private String token;
  private EmbedSiteDto embedSite;
  private String memo;

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


}
