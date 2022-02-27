package gg.igni.igniserver.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="table_embedsites")
public class EmbedSite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

  private String name;
  private String code;

  @OneToMany(mappedBy = "embedSite")
  private Set<Embed> embeds;

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public Set<Embed> getEmbeds() {
    return embeds;
  }
  public void setEmbeds(Set<Embed> embeds) {
    this.embeds = embeds;
  }




}
