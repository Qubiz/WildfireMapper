package robor.wildfiremapper;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.wealthfront.magellan.BaseScreenView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mathijs de Groot on 23/05/2018.
 */
public class HomeView extends BaseScreenView<HomeScreen> {

    @OnClick(R.id.open_map_button)
    void open() {
        Log.d("Test", "Hello!");
        getScreen().openMap();
    }

    public HomeView(Context context) {
        super(context);
        inflate(context, R.layout.home, this);
        ButterKnife.bind(this);
    }

}
