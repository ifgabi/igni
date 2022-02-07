package gg.igni.igniserver.watch.data;

import java.util.List;

public class EmbedSendDto {
  private List<EmbedDto> embeds;

  public List<EmbedDto> getEmbeds() {
    return embeds;
  }

  public void setEmbeds(List<EmbedDto> embeds) {
    this.embeds = embeds;
  }
}
