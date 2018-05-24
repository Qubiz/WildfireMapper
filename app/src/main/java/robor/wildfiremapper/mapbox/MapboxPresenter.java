package robor.wildfiremapper.mapbox;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;
import robor.wildfiremapper.di.ActivityScoped;

/**
 * Listens to user actions from the UI ({@link MapboxFragment}), retrieves the data and updates the
 * UI as required.
 * <p/>
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the TasksPresenter (if it fails, it emits a compiler error).  It uses
 * {@link MapboxModule} to do so.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 **/
@ActivityScoped
public class MapboxPresenter implements MapboxContract.Presenter {

    @Nullable private MapboxContract.View boxmapView;

    @Inject
    MapboxPresenter() {

    }

    @Override
    public void takeView(MapboxContract.View view) {
        this.boxmapView = view;
    }

    @Override
    public void dropView() {
        boxmapView = null;
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }
}
