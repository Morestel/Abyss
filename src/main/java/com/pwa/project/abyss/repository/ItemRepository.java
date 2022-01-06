package com.pwa.project.abyss.repository;

import java.util.List;
import com.pwa.project.abyss.models.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {

    Item findById(int id);
    Item findByNameItem(String nameItem);
    List<Item> findAll();
    List<Item> findAllByOrderByNameItem();
    // List<Item> findAllOrderByDay();
}