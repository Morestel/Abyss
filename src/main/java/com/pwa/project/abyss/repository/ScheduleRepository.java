package com.pwa.project.abyss.repository;

import java.util.List;
import com.pwa.project.abyss.models.Schedule;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Integer> {

    Schedule findScheduleById(int id);

    List<Schedule> findAllByOrderByNameSchedule();

    @Query(
        value = "SELECT * FROM Schedule s WHERE s.owner = ?1",
        nativeQuery = true
    )
    List<Schedule> findAllByOwner(String owner);

    Schedule findScheduleByNameSchedule(String nameSchedule);

    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM Schedule s WHERE s.id = ?1"
    )
    void deleteSchedule(int id);
}
