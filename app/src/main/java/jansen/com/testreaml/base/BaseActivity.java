package jansen.com.testreaml.base;

import android.app.Activity;
import android.os.Bundle;

import io.realm.Realm;

/**
 * Created Jansen on 2016/6/6.
 */
public class BaseActivity extends Activity{
    protected Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
