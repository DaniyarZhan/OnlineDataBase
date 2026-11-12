package com.example.onlinedatabase;

import java.util.ArrayList;
import java.util.List;

public class UserStaticInfo {


    //ключи параметра
    public final static String LOGIN = "login";
    public final static String POSITION = "position";
    public final static String USERS_SIGN_IN_INFO = "UsersSignInInfo";
    public final static String USERS_PROFILE_INFO = "UsersProfileInfo";
    public final static String PASSWORD = "password";
    public final static String PROFILE_ID = "profileId";
    public final static String NAME = "name";
    public final static String AGE = "age";
    public final static String STATE = "state";
    public static String profileId;

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
