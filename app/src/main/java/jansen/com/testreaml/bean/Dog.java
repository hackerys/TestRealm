package jansen.com.testreaml.bean;

import io.realm.RealmObject;

/**
 * Created Jansen on 2016/6/6.
 */
public class Dog extends RealmObject{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        name = mName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int mAge) {
        age = mAge;
    }
}
