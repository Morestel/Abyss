package com.pwa.project.abyss;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import com.pwa.project.abyss.components.UserManagement;
import com.pwa.project.abyss.models.PrivateMessage;
import com.pwa.project.abyss.models.Schedule;
import com.pwa.project.abyss.models.User;
import com.pwa.project.abyss.repository.ItemRepository;
import com.pwa.project.abyss.repository.MessageRepository;
import com.pwa.project.abyss.repository.PrivateMessageRepository;
import com.pwa.project.abyss.repository.ScheduleRepository;
import com.pwa.project.abyss.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Data {
    @Inject
    UserRepository aUserRepository;

    @Inject
    ScheduleRepository aScheduleRepository;

    @Inject
    ItemRepository aItemRepository;

    @Inject
    MessageRepository aMessageRepository;

    @Inject
    PrivateMessageRepository aPrivateMessageRepository;

    @Inject
    UserManagement aUserManagement;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void generateData() {

        System.err.println("Generating data ...");
        System.err.println("Generating users ...");
        List<User> listUser = new ArrayList<>();
        List<Schedule> listSchedule = new ArrayList<>();
        // Do it if and only if the user's database is empty
        if (aUserRepository.count() == 0) {
            // Generate the admin of the website
            User vUser = new User("admin", "admin@gmail.com");
            vUser.setEncodedPassword(passwordEncoder.encode("password"));
            aUserRepository.save(vUser);
            aUserManagement.makeUserAdmin(vUser.getUsername());

            // Generate lots of user
            for (int i = 0; i < 100; i++) {
                vUser = new User("user" + i, "user" + i + "@gmail.com");
                vUser.setEncodedPassword(passwordEncoder.encode("password" + i));
                aUserRepository.save(vUser);
            }

            System.err.println("... Done");
        } else {
            System.err.println("Users already exists ");
        }
        int rand;
        System.err.println("Generating schedules ... ");
        if (aScheduleRepository.count() == 0) {
            for (int i = 0; i < 300; i++) {
                rand = new Random().nextInt(100);
                Schedule schedule = new Schedule("Schedule" + i, "user" + rand);
                User vUser = aUserRepository.findUserByUsername("user" + rand);
                aScheduleRepository.save(schedule);
                listUser.add(vUser);
                schedule.setUsers(listUser);
                aUserRepository.save(vUser);
                aScheduleRepository.save(schedule);
                listSchedule = vUser.getSchedule();
                listSchedule.add(schedule);
                vUser.setSchedule(listSchedule);
                aUserRepository.save(vUser);
                listSchedule = new ArrayList<>();
                listUser = new ArrayList<>();
                
            }
            System.err.println("... Done");
        } else {
            System.err.println("Schedule already exists ");
        }

        System.err.println("Generating private messages ... ");
        if (aPrivateMessageRepository.count() == 0) {
            PrivateMessage pm = new PrivateMessage(aUserRepository.findUserByUsername("admin"),
                    aUserRepository.findUserByUsername("user1"), new Date(), "Oui");
            // PrivateMessage pm2 = new
            // PrivateMessage(aUserRepository.findUserByUsername("user1"),
            // aUserRepository.findUserByUsername("admin"), new Date(), "Non");
            PrivateMessage pm3 = new PrivateMessage(aUserRepository.findUserByUsername("user2"),
                    aUserRepository.findUserByUsername("admin"), new Date(), "Hello");
            PrivateMessage pm4 = new PrivateMessage(aUserRepository.findUserByUsername("user3"),
                    aUserRepository.findUserByUsername("admin"), new Date(), "World");

            aPrivateMessageRepository.save(pm);
            // aPrivateMessageRepository.save(pm2);
            aPrivateMessageRepository.save(pm3);
            aPrivateMessageRepository.save(pm4);

            System.err.println("... Done");
        } else {
            System.err.println("Private message already exists ");
        }
    }
}
