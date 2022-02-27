package gg.igni.igniserver.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ViewKey implements Serializable{

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "embed_id")
  private Embed embed;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Embed getEmbed() {
    return embed;
  }

  public void setEmbed(Embed embed) {
    this.embed = embed;
  }



}
