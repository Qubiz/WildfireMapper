package robor.wildfiremapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.wealthfront.magellan.Navigator;
import com.wealthfront.magellan.support.SingleActivity;

public class MainActivity extends SingleActivity {

    @Override
    protected Navigator createNavigator() {
        return Navigator.withRoot(new HomeScreen())
                .loggingEnabled(true)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mapbox.getInstance(this, getString(R.string.mapbox_key));
    }
}
