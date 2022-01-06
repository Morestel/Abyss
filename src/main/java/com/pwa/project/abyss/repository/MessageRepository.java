package com.pwa.project.abyss.repository;

import java.util.List;
import com.pwa.project.abyss.models.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {

    Message findById(int id);

    List<Message> findAll();

    @Query(value = "SELECT * FROM Message m WHERE m.schedule_id = ?1 ORDER BY m.id DESC", nativeQuery = true)
    List<Message> findAllOrderById(int idSchedule);
}
