package com.example.onlinedatabase;

import java.util.ArrayList;
import java.util.List;

public class UserStaticInfo {

    //ключ параметра позиция
    public final static String POSITION = "position";

    //Список пользователей объявим
    public static List<User> users = new ArrayList<>();
    public UserStaticInfo() {
        if (users.isEmpty())
            AddUsersInList();
    }

    private void AddUsersInList() {
        users.add(new User("Алексей", "Я ушел", 18, 0));
        users.add(new User("Андрей", "Я не буду работать", 17, 1));
        users.add(new User("Максим", "Я анимешник", 19, 2));
        users.add(new User("Дмитрий", "Отдохнуть хочу", 27, 1));
        users.add(new User("Алексей", "Я ушел", 15, 2));
        users.add(new User("Алексий", "Я умер", 91, 0));
    }
}
