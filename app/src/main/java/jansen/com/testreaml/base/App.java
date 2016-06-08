package jansen.com.testreaml.base;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
import com.wn518.net.WNHttpEngine;
import com.wn518.net.utils.PreferencesUtils;

import java.security.SecureRandom;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created Jansen on 2016/6/6.
 */
public class App extends Application {
    public static final String KEY = "ys";

    @Override
    public void onCreate() {
        super.onCreate();
        byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);
        /**
         * 必须配置此项，否则chrome检测不到realm数据库
         */
        RealmInspectorModulesProvider mProvider = RealmInspectorModulesProvider.builder(this)
                .withFolder(getCacheDir())
                .withMetaTables()
                .withEncryptionKey("mykey", key)
                .withDescendingOrder()
                .withLimit(1000)
                .databaseNamePattern(Pattern.compile(".+\\.realm"))
                .build();
        /**
         * htpp初始化
         */
        WNHttpEngine.getInstance().init(this);
        WNHttpEngine.getInstance().setDebug(true);
        /**
         * stetho初始化
         */
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(mProvider)
                        .build());
        //        Stetho.initializeWithDefaults(this);
        /**
         * realm初始化
         */
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        /**
         * 轻量存储
         */
        PreferencesUtils.init(this);
        PreferencesUtils.setShareStringData("yansheng", "testdata");
    }
}
