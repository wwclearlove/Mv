package cdictv;

import android.content.Context;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

public class App extends LitePalApplication {
    public static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        LitePal.initialize(this);
    }
}
