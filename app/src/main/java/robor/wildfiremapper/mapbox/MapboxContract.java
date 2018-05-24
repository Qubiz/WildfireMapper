package robor.wildfiremapper.mapbox;

import robor.wildfiremapper.BasePresenter;
import robor.wildfiremapper.BaseView;

/**
 * Created by Mathijs de Groot on 24/05/2018.
 */
public interface MapboxContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {

        void result(int requestCode, int resultCode);

        void takeView(MapboxContract.View view);

        void dropView();
    }

}
