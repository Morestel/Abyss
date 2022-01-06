package com.pwa.project.abyss.repository;

import java.util.List;
import com.pwa.project.abyss.models.PrivateMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateMessageRepository extends CrudRepository<PrivateMessage, Integer> {

    @Query(value = "SELECT * FROM Private_Message q WHERE q.sender_username = ?1 AND q.receiver_username = ?2 OR q.sender_username = ?2 AND q.receiver_username = ?1 ORDER BY q.date_pm DESC", nativeQuery = true)
    List<PrivateMessage> findAllOrderByDate(String sender_username, String receiver_username);

    List<PrivateMessage> findAll();

    @Query(
        value = "SELECT * FROM Private_Message q WHERE q.receiver_username = ?1",
        nativeQuery = true
    )
    List<PrivateMessage> findAllByReceiver(String receiver_username);

    @Query(
        value = "SELECT * FROM Private_Message q WHERE q.receiver_username = ?1 AND q.sender_username = ?2",
        nativeQuery = true
    )
    List<PrivateMessage> findAllByReceiverAndSender(String receiver_username, String sender_username);

    PrivateMessage findById(int id);
}
