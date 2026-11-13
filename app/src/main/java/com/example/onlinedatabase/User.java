package com.example.onlinedatabase;

public class User {
    private String Id, Name, State;
    private int Age, StateSignal;

    public User(String name, String state, int age, int stateSignal) {
        Name = name;
        State = state;
        Age = age;
        StateSignal = stateSignal;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getStateSignal() {
        return StateSignal;
    }

    public void setStateSignal(int stateSignal) {
        StateSignal = stateSignal;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
