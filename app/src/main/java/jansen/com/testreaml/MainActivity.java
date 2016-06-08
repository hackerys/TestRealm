package jansen.com.testreaml;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wn518.net.utils.WnLogsUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import jansen.com.testreaml.base.BaseActivity;
import jansen.com.testreaml.bean.Dog;

public class MainActivity extends BaseActivity {


    @Bind(R.id.add)
    Button mAdd;
    @Bind(R.id.update)
    Button mUpdate;
    @Bind(R.id.del)
    Button mDel;
    @Bind(R.id.query)
    Button mQuery;
    private int count = 1;
    RealmResults<Dog> results;
    Dog mDog1;
    RealmAsyncTask transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // obtain the results of a query
        results = realm.where(Dog.class).findAll();
        //相当于整个狗的表都处于监听之中
        results.addChangeListener(new RealmChangeListener<RealmResults<Dog>>() {
            @Override
            public void onChange(RealmResults<Dog> element) {
                WnLogsUtils.e("还有" + results.size() + "条狗");
                WnLogsUtils.e("狗被修改了");
            }
        });
    }

    @OnClick({R.id.add, R.id.update, R.id.del, R.id.query})
    public void onClick(View mView) {
        /**
         *增加
         */
        if (mView.getId() == R.id.add) {
            // Persist your data in a transaction
            /**
             * 默认事务是同步会阻塞，所以使用异步事务，返回的异步任务要在activity结束的时候关闭
             */
             transaction =realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    Dog mDog = bgRealm.createObject(Dog.class);
                    mDog.setAge(10000);
                    mDog.setName("jack" + System.currentTimeMillis());
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    // Transaction was a success.
                    WnLogsUtils.e("添加成功");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    // Transaction failed and was automatically canceled.
                    WnLogsUtils.e("添加失败");
                }
            });
        }
        /**
         * 更新
         */
        if (mView.getId() == R.id.update) {
            // Asynchronously update objects on a background thread
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    //这个对象只能在execute方法内有效,且得判空
                    Dog dog = bgRealm.where(Dog.class).equalTo("age", 100).findFirst();
                    if (dog != null) {
                        dog.setAge(4);
                    } else {
                        WnLogsUtils.e("修改失败,这条狗不存在");
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    // Original queries and Realm objects are automatically updated.
                    WnLogsUtils.e("修改年龄成功");
                }
            });
        }
        /**
         * 删除
         */
        if (mView.getId() == R.id.del) {

            WnLogsUtils.e("还有" + results.size() + "条狗");
            // All changes to data must happen in a transaction
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // remove single match
                    results.deleteFirstFromRealm();
                    // Delete all matches
                    //                    results.deleteAllFromRealm();
                }
            });
        }
        /**
         * 查找
         */
        if (mView.getId() == R.id.query) {
            for (Dog mDog : results) {
                WnLogsUtils.e(mDog.getName() + "");
            }
        }
    }

    /**
     * 关闭异步的事务，防止回调使app崩溃
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (transaction != null && !transaction.isCancelled()) {
            transaction.cancel();
        }
    }
}
