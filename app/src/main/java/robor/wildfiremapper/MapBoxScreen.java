package robor.wildfiremapper;

import android.content.Context;

import com.wealthfront.magellan.Screen;

/**
 * Created by Mathijs de Groot on 23/05/2018.
 */
public class MapBoxScreen extends Screen<MapBoxView> {

    @Override
    protected MapBoxView createView(Context context) {
        return new MapBoxView(context);
    }

    @Override
    public String getTitle(Context context) {
        return "Map Screen";
    }
}
