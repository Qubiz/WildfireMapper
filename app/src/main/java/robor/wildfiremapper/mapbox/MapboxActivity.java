package robor.wildfiremapper.mapbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import robor.wildfiremapper.R;
import robor.wildfiremapper.util.ActivityUtils;

/**
 * Created by Mathijs de Groot on 24/05/2018.
 */
public class MapboxActivity extends DaggerAppCompatActivity {

    @Inject
    MapboxPresenter mapboxPresenter;
    @Inject Lazy<MapboxFragment> boxmapFragmentProvider;

    @BindView(R.id.drawer_layout)   DrawerLayout    drawerLayout;
    @BindView(R.id.toolbar)         Toolbar         toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapbox_activity);

        ButterKnife.bind(this);

        // Set up the toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        MapboxFragment mapboxFragment = (MapboxFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        // Set up the fragment
        if (mapboxFragment == null) {
            mapboxFragment = boxmapFragmentProvider.get();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mapboxFragment, R.id.contentFrame);
        }

        // Load previously saved state, if available
        if (savedInstanceState != null) {

        }

    }
}
