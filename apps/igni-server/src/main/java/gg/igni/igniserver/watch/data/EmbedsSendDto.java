package gg.igni.igniserver.watch.data;

import java.util.List;

import org.springframework.data.domain.Page;

public class EmbedsSendDto {
  private List<EmbedDto> embeds;
  private long numberOfPages;

  public List<EmbedDto> getEmbeds() {
    return embeds;
  }

  public void setEmbeds(List<EmbedDto> embeds) {
    this.embeds = embeds;
  }

  public long getNumberOfPages() {
    return numberOfPages;
  }

  public void setNumberOfPages(long numberOfPages) {
    this.numberOfPages = numberOfPages;
  }


}
