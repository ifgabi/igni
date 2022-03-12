package gg.igni.igniserver.model;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "table_views")
public class View {

  @EmbeddedId
  private ViewKey primaryKey;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdate;

  public ViewKey getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(ViewKey primaryKey) {
    this.primaryKey = primaryKey;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }


}
