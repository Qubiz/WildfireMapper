package robor.wildfiremapper.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.ViewGroup;

public abstract class BaseSubView extends ViewGroup implements SubMvpView {

    private MvpView parentMvpView;

    public BaseSubView(Context context) {
        super(context);
    }

    public BaseSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public BaseSubView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void attachParentMvpView(MvpView mvpView) {
        parentMvpView = mvpView;
    }

    @Override
    public void showLoading() {
        if (parentMvpView != null) {
            parentMvpView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (parentMvpView != null) {
            parentMvpView.hideLoading();
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (parentMvpView != null) {
            parentMvpView.onError(resId);
        }
    }

    @Override
    public void onError(String message) {
        if (parentMvpView != null) {
            parentMvpView.onError(message);
        }
    }

    @Override
    public void showMessage(String message) {
        if (parentMvpView != null) {
            parentMvpView.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (parentMvpView != null) {
            parentMvpView.showMessage(resId);
        }
    }

    @Override
    public void hideKeyboard() {
        if (parentMvpView != null) {
            parentMvpView.hideKeyboard();
        }
    }

    @Override
    public boolean isNetworkConnected() {
        if (parentMvpView != null) {
            return parentMvpView.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (parentMvpView != null) {
            parentMvpView.openActivityOnTokenExpire();
        }
    }

    protected abstract void bindViewsAndSetOnClickListeners();

    protected abstract void setUp();
}
