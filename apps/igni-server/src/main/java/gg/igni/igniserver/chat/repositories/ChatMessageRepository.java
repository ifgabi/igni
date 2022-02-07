package gg.igni.igniserver.chat.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gg.igni.igniserver.chat.model.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{

  @Query(value = "SELECT * FROM table_chatmessages cm ORDER BY id DESC LIMIT 20;",
    nativeQuery= true
  )
  List<ChatMessage> getHistory();


}
