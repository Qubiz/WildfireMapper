package robor.wildfiremapper;

import android.content.Context;

import com.wealthfront.magellan.Screen;

/**
 * Created by Mathijs de Groot on 23/05/2018.
 */
class HomeScreen extends Screen<HomeView> {

    @Override
    protected HomeView createView(Context context) {
        return new HomeView(context);
    }

    void openMap() {
        getNavigator().goTo(new MapBoxScreen());
    }

}
