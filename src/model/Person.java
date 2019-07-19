package model;

/**
 * ===========================
 * author:       wangjialong
 * date:         2019/7/12
 * time:         16:12
 * description:  请输入描述
 * ============================
 */

public class Person {
    private int gender;
    private String name;

    public Person(int gender, String name) {
        this.gender = gender;
        this.name = name;
    }

    public Person() {
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
