package com.pwa.project.abyss.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.pwa.project.abyss.models.Item;
import com.pwa.project.abyss.models.Message;
import com.pwa.project.abyss.models.Schedule;
import com.pwa.project.abyss.models.User;
import com.pwa.project.abyss.repository.ItemRepository;
import com.pwa.project.abyss.repository.ScheduleRepository;
import com.pwa.project.abyss.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScheduleController {

    @Inject
    ScheduleRepository scheduleRepo;

    @Inject
    UserRepository aUserRepo;

    @Inject
    ItemRepository itemRepo;

    /**
     * Give the day of the week
     * @param d Date
     * @return 0 SUNDAY / 1 MONDAY / 2 TUESDAY / 3 WEDNESDAY / 4 THURSDAY / 5 FRIDAY / 6 SATURDAY
     */
    public int getDayOfWeek(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Return the page of a specific schedule whose the number is include in the URL
     * @param model
     * @param auth
     * @param id_schedule The ID of the schedule that we want to have an access
     * @return The page of a specific schedule
     */
    @RequestMapping("/schedule/{id_schedule}")
    public String schedule(Model model, Authentication auth, @PathVariable int id_schedule) {
        Schedule sched = scheduleRepo.findScheduleById(id_schedule);
        model.addAttribute("schedule", sched);
        User u = new User();
        u = aUserRepo.findUserByUsername(auth.getName());
        model.addAttribute("user_profile", u);
        model.addAttribute("item", new Item());

        /* Add a list of item which concern the schedule */
        List<Item> listItem = new ArrayList<>();
        List<Item> listAllItem = new ArrayList<>();
        Date today = new Date();
        long diffInMilli;
        long diffInDay;

        for (Item i : sched.getItems()) {
            listAllItem.add(i);

            diffInMilli = i.getDay().getTime() - today.getTime();
            diffInDay = TimeUnit.DAYS.convert(diffInMilli, TimeUnit.MILLISECONDS);

            switch (getDayOfWeek(today)) {
                case 1: // SUNDAY
                    if (diffInDay > -7 && diffInDay < 0) {
                        listItem.add(i);
                    }
                    break;
                case 2: // MONDAY
                    if (diffInDay > -1 && diffInDay < 6) {
                        listItem.add(i);
                    }
                    break;
                case 3: // TUESDAY
                    if (diffInDay > -2 && diffInDay < 5) {
                        listItem.add(i);
                    }
                    break;
                case 4: // WEDNESDAY
                    if (diffInDay > -3 && diffInDay < 4) {
                        listItem.add(i);
                    }
                    break;
                case 5: // THURSDAY

                    if (diffInDay > -4 && diffInDay < 3) {
                        listItem.add(i);
                    }
                    break;
                case 6: // FRIDAY
                    if (diffInDay > -5 && diffInDay < 2) {
                        listItem.add(i);
                    }
                    break;
                case 7: // SATURDAY
                    if (diffInDay > -6 && diffInDay < 1) {
                        listItem.add(i);
                    }
                    break;
                default:
                    System.err.println("Unknown day");
                    break;
            }
        }
        model.addAttribute("listItem", listItem);
        model.addAttribute("listAllItem", listAllItem);
        List<Message> listeMessage = new ArrayList<>();
        for (Message m : sched.getMessages()) {
            listeMessage.add(m);
        }
        model.addAttribute("listeMessage", listeMessage);

        /* Add of a list of user */
        model.addAttribute("listUser", sched.getUsers());

        for (Item v : listItem) {
            System.err.println(v.getNameItem());
        }
        // Thymeleaf version
        // return "schedule";
        // VueJS version
        return "schedule-vue";
    }

    /**
     * Add a schedule in the database. The name is only needed because the ID is set automatically.
     * The list of all the users contains only the owner for the moment.
     * We save the schedule in the user database too.
     * @param nameSchedule The name we want to give to this schedule
     * @param auth
     * @return The page of the schedule which is created
     */
    @PostMapping(value = "/addSchedule")
    public String addSchedule(@RequestParam String nameSchedule, Authentication auth) {
        String owner = auth.getName();
        // Initialisation of the user and schedule
        User user = aUserRepo.findUserByUsername(owner);
        Schedule schedule = new Schedule(nameSchedule, owner);

        // We add schedule in the user and user in the schedule
        schedule.addUser(user);
        user.addSchedule(schedule);

        // We save both of them
        scheduleRepo.save(schedule);
        aUserRepo.save(user);

        return "redirect:/schedule/" + schedule.getId();
    }

    @GetMapping(path = "/schedule/addUser/{id_schedule}")
    public String addOwner(Authentication auth, @PathVariable int id_schedule) {
        Schedule schedule = scheduleRepo.findScheduleById(id_schedule);
        User user = aUserRepo.findUserByUsername(auth.getName());
        List<User> listUser = schedule.getUsers();
        listUser.add(user);
        schedule.setUsers(listUser);
        scheduleRepo.save(schedule);
        return "redirect:/schedule/" + id_schedule;
    }

    /**
     * This function add an user in a schedule. The schedule is specified in the path.
     * @param nameUser Username that we want to add in the schedule
     * @param id_schedule The ID of the schedule in which we will add the user
     * @return The page of the user if he all is ok, an error is the user is already in or if he doesn't exist.
     */
    @PostMapping(path = "/schedule/addUser/{id_schedule}")
    public String addUser(@RequestParam String nameUser, @PathVariable int id_schedule) {

        Schedule schedule = scheduleRepo.findScheduleById(id_schedule);
        /* Check if user is already in */
        for (User v : schedule.getUsers()) {
            if (v.getUsername().equals(nameUser)) {
                return "redirect:/schedule/" + id_schedule;
            }
        }

        for (User u : aUserRepo.findAll()) {
            if (u.getUsername().equals(nameUser)) {
                User user = aUserRepo.findUserByUsername(nameUser);
                List<User> listUser = schedule.getUsers();
                listUser.add(user);
                user.addSchedule(schedule);
                schedule.setUsers(listUser);
                aUserRepo.save(user);
                scheduleRepo.save(schedule);
                return "redirect:/schedule/" + id_schedule;
            }
        }
        return "redirect:/error/" + "UnknownUser";
    }

    /**
     * This function add an item in the schedule
     * @param nameItem
     * @param day
     * @param hourBeginItem
     * @param hourEndItem
     * @param placeItem
     * @param id
     * @param descriptionItem
     * @return The page of the schedule in which we add the item
     */
    @PostMapping(path = "/schedule/addItem/{id}")
    public String addItem(@RequestParam String nameItem, @RequestParam String day, @RequestParam String hourBeginItem,
            @RequestParam String hourEndItem, @RequestParam String placeItem, @PathVariable int id,
            @RequestParam String descriptionItem) {

        /* All the input are required so no need to check if the champ are empty */
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date dayItem = new Date();

        try {
            dayItem = simpleDateFormat.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Schedule schedule = scheduleRepo.findScheduleById(id);
        List<Item> listItem = schedule.getItems();

        Item item = new Item(nameItem, dayItem, hourBeginItem, hourEndItem, placeItem, schedule, descriptionItem);
        itemRepo.save(item);

        listItem.add(item);
        schedule.setItems(listItem);
        scheduleRepo.save(schedule);

        return "redirect:/schedule/" + id;
    }

    /**
     * This function allow to delete an user from a specified schedule. 
     * It can be both the user who wants to leave the schedule, or the owner who kicks him from this
     * @param model
     * @param auth
     * @param idSchedule ID of the schedule
     * @param username Username of the one who will be kicked
     * @return The home page if the user is leaving, the schedule page if the owner kicked the user
     */
    @RequestMapping("schedule/{idSchedule}/delete/{username}")
    public String deleteUserFromSchedule(Model model, 
                                        Authentication auth, 
                                        @PathVariable(required = true) int idSchedule, 
                                        @PathVariable(required = true) String username){

        User vUser = aUserRepo.findUserByUsername(username);
        Schedule vSchedule = scheduleRepo.findScheduleById(idSchedule);
        
        List<Schedule> listSchedule = vUser.getSchedule();
        List<User> listUser = vSchedule.getUsers();
                                        
        if (listSchedule.contains(vSchedule) && listUser.contains(vUser)){
            listSchedule.remove(vSchedule);
            listUser.remove(vUser);

            vUser.setSchedule(listSchedule);
            vSchedule.setUsers(listUser);

            aUserRepo.save(vUser);
            scheduleRepo.save(vSchedule);
        }

        // Want to leave so he returns home
        if (auth.getName().equals(username)){
            return "redirect:/home";
        }
        else{
            return "redirect:/schedule/" + idSchedule;
        }
    }

    /**
     * This function allows to delete a schedule, if the user is the owner of the schedule.
     * This condition is verified in Thymeleaf, only the owner have the option to delete a schedule,
     * so we don't need to test it here
     * @param model
     * @param auth
     * @param idSchedule ID of the schedule which will be deleted
     * @return The home page
     */
    @RequestMapping("/schedule/{idSchedule}/delete")
    public String deleteSchedule(Model model, Authentication auth, @PathVariable int idSchedule){
        Schedule sc = scheduleRepo.findScheduleById(idSchedule);
        sc.setUsers(new ArrayList<>());
        scheduleRepo.save(sc);
        // Find and delete the schedule for all the users
        for (User vUser: aUserRepo.findAllByOrderByUsername()){
            if (vUser.getSchedule().contains(sc)){
                List<Schedule> listSchedule = vUser.getSchedule();
                listSchedule.remove(sc);
                vUser.setSchedule(listSchedule);
                aUserRepo.save(vUser);
            }
        }

        scheduleRepo.delete(sc);
        return "redirect:/home";

    }
}