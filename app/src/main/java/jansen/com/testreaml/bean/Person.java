package jansen.com.testreaml.bean;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created Jansen on 2016/6/6.
 */
public class Person extends RealmObject{
    @PrimaryKey
    private long id;
    private String name;
    private RealmList<Dog> dogs; // Declare one-to-many relationships

    public Person() {
    }

    public Person(long mId, String mName) {
        id = mId;
        name = mName;
    }

    public long getId() {
        return id;
    }

    public void setId(long mId) {
        id = mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        name = mName;
    }

    public RealmList<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(RealmList<Dog> mDogs) {
        dogs = mDogs;
    }
}
