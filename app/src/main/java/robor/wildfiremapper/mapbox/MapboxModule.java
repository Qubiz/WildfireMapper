package robor.wildfiremapper.mapbox;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import robor.wildfiremapper.di.ActivityScoped;
import robor.wildfiremapper.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link MapboxPresenter}.
 */
@Module
public abstract class MapboxModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MapboxFragment boxmapFragment();

    @ActivityScoped
    @Binds
    abstract MapboxContract.Presenter boxmapPresenter(MapboxPresenter presenter);

}
