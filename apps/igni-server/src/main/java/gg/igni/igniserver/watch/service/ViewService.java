package gg.igni.igniserver.watch.service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import gg.igni.igniserver.watch.repository.ViewRepository;

@Service
public class ViewService {

  @Autowired
  private ViewRepository viewRepository;

  public void bumpUpdate(Long embedId, Long userId) {
    viewRepository.updateLastUpdate(embedId, userId);
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
}
