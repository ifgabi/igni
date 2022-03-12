package gg.igni.igniserver.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

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

  @OneToMany(mappedBy = "primaryKey.embed")
  private List<View> views;

  @Temporal(TemporalType.TIMESTAMP)
  private Date timeoutBumpDate;

  @Transient
  private Long viewCount;

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

  public List<View> getViews() {
    return views;
  }

  public void setViews(List<View> views) {
    this.views = views;
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
