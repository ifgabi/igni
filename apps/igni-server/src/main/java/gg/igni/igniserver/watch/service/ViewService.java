package gg.igni.igniserver.watch.service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import gg.igni.igniserver.account.repositories.UserRepository;
import gg.igni.igniserver.model.Embed;
import gg.igni.igniserver.model.User;
import gg.igni.igniserver.model.View;
import gg.igni.igniserver.model.ViewKey;
import gg.igni.igniserver.watch.repository.EmbedRepository;
import gg.igni.igniserver.watch.repository.ViewRepository;

@Service
public class ViewService {

  @Autowired
  private ViewRepository viewRepository;

  @Autowired
  private EmbedRepository embedRepository;

  @Autowired
  private UserRepository userRepository;

  public void bumpUpdate(Long embedId, Long userId) {
    viewRepository.updateLastUpdate(embedId, userId);
    embedRepository.updateTimeoutUpdate(embedId);
  }

  public Optional<Integer> getViewCount(Long embedId) {
    Date currentDate = new Date(System.currentTimeMillis());
    Calendar c = Calendar.getInstance();
    c.setTime(currentDate);
    c.add(Calendar.SECOND, -30);
    Optional<Integer> count = viewRepository.getViewCount(embedId, new java.sql.Date(c.getTime().getTime()));

    return count;
  }

  @Scheduled(fixedRate = 30000)
  @Transactional
	public void cleanUpExpiredViews() {
    this.viewRepository.cleanupViews();
	}

  public boolean testData(Long id)
  {
    Embed emb = embedRepository.getById(id);

    User admin = userRepository.getById(1L);
    User user1 = userRepository.getById(2L);

    Date currentDate = new Date(System.currentTimeMillis());
    Calendar cal = Calendar.getInstance();
    cal.setTime(currentDate);

    View newview = new View();
    newview.setPrimaryKey(new ViewKey());
    newview.setLastUpdate(cal.getTime());
    newview.getPrimaryKey().setEmbed(emb);
    newview.getPrimaryKey().setUser(admin);
    viewRepository.save(newview);

    View newview2 = new View();
    newview2.setPrimaryKey(new ViewKey());
    newview2.setLastUpdate(cal.getTime());
    newview2.getPrimaryKey().setEmbed(emb);
    newview2.getPrimaryKey().setUser(user1);
    viewRepository.save(newview2);

    return true;
  }
}
