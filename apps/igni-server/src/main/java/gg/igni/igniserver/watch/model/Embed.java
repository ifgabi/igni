package gg.igni.igniserver.watch.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="table_embeds")
public class Embed {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

  private String token;

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="embedsite_id")
  private EmbedSite embedSite;

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

  public EmbedSite getEmbedSite() {
    return embedSite;
  }

  public void setEmbedSite(EmbedSite embedSite) {
    this.embedSite = embedSite;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }


}
