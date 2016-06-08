/*
 * Copyright 2014 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jansen.com.testreaml.introExample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import jansen.com.testreaml.R;
import jansen.com.testreaml.base.BaseActivity;
import jansen.com.testreaml.introExample.model.Pen;
import jansen.com.testreaml.introExample.model.People;
import jansen.com.testreaml.introExample.model.Phone;

public class IntroExampleActivity extends BaseActivity {

    public static final String TAG = IntroExampleActivity.class.getName();
    private LinearLayout rootLayout = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_basic_example);
        rootLayout = ((LinearLayout) findViewById(R.id.container));
        rootLayout.removeAllViews();

        // These operations are small enough that
        // we can generally safely run them on the UI thread.
        /**
         * 一次额常规的小操作可以在ui线程中进行
         */
        basicCRUD(realm);
        basicQuery(realm);
        basicLinkQuery(realm);

        // More complex operations can be executed on another thread.
        /**
         * 非常复杂的操作最好是放到其他线程中去操作
         * 异步任务的looper是主线程的looper
         */
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String info;
                info = complexReadWrite();
                info += complexQuery();
                return info;
            }

            @Override
            protected void onPostExecute(String result) {
                showStatus(result);
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    private void showStatus(String txt) {
        Log.i(TAG, txt);
        TextView tv = new TextView(this);
        tv.setText(txt);
        rootLayout.addView(tv);
    }

    private void basicCRUD(Realm realm) {
        showStatus("Perform basic Create/Read/Update/Delete (CRUD) operations...");

        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a mPeople
                People mPeople = realm.createObject(People.class);
                mPeople.setId(1);
                mPeople.setName("Young People");
                mPeople.setAge(14);

            }
        });

        // Find the first mPeople (no query conditions) and read a field
        final People mPeople = realm.where(People.class).findFirst();
        showStatus(mPeople.getName() + ":" + mPeople.getAge());

        // Update mPeople in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mPeople.setName("Senior People");
                mPeople.setAge(99);
                showStatus(mPeople.getName() + " got older: " + mPeople.getAge());
            }
        });

        // Delete all persons
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(People.class);
            }
        });
    }

    private void basicQuery(Realm realm) {
        showStatus("\nPerforming basic Query operation...");
        showStatus("Number of persons: " + realm.where(People.class).count());

        RealmResults<People> results = realm.where(People.class)
                .equalTo("age", 99)
                .findAll();

        showStatus("Size of result set: " + results.size());
    }

    /**
     * 连表查询
     *
     * @param realm
     */
    private void basicLinkQuery(Realm realm) {
        showStatus("\nPerforming basic Link Query operation...");
        showStatus("Number of persons: " + realm.where(People.class).count());

        RealmResults<People> results = realm.where(People.class).equalTo("mPens.name", "Tiger").findAll();

        showStatus("Size of result set: " + results.size());
    }

    private String complexReadWrite() {
        String status = "\nPerforming complex Read/Write operation...";

        // Open the default realm. All threads must use it's own reference to the realm.
        // Those can not be transferred across threads.
        Realm realm = Realm.getDefaultInstance();

        // Add ten persons in one transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Phone fido = realm.createObject(Phone.class);
                fido.name = "fido";
                for (int i = 0; i < 10; i++) {
                    People mPeople = realm.createObject(People.class);
                    mPeople.setId(i);
                    mPeople.setName("People no. " + i);
                    mPeople.setAge(i);
                    mPeople.setPhone(fido);

                    // The field tempReference is annotated with @Ignore.
                    // This means setTempReference sets the People tempReference
                    // field directly. The tempReference is NOT saved as part of
                    // the RealmObject:
                    mPeople.setTempReference(42);

                    for (int j = 0; j < i; j++) {
                        Pen mPen = realm.createObject(Pen.class);
                        mPen.name = "Cat_" + j;
                        mPeople.getPens().add(mPen);
                    }
                }
            }
        });

        // Implicit read transactions allow you to access your objects
        status += "\nNumber of persons: " + realm.where(People.class).count();

        // Iterate over all objects
        for (People pers : realm.where(People.class).findAll()) {
            String dogName;
            if (pers.getPhone() == null) {
                dogName = "None";
            } else {
                dogName = pers.getPhone().name;
            }
            status += "\n" + pers.getName() + ":" + pers.getAge() + " : " + dogName + " : " + pers.getPens().size();
        }

        // Sorting
        RealmResults<People> mSortedPeoples = realm.where(People.class).findAllSorted("age", Sort.DESCENDING);
        status += "\nSorting " + mSortedPeoples.last().getName() + " == " + realm.where(People.class).findFirst()
                .getName();

        realm.close();
        return status;
    }

    private String complexQuery() {
        String status = "\n\nPerforming complex Query operation...";

        Realm realm = Realm.getDefaultInstance();
        status += "\nNumber of persons: " + realm.where(People.class).count();

        // Find all persons where age between 7 and 9 and name begins with "People".
        RealmResults<People> results = realm.where(People.class)
                .between("age", 7, 9)       // Notice implicit "and" operation
                .beginsWith("name", "People").findAll();
        status += "\nSize of result set: " + results.size();

        realm.close();
        return status;
    }
}
