/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package robor.wildfiremapper.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import robor.wildfiremapper.di.ActivityContext;
import robor.wildfiremapper.utils.rx.AppSchedulerProvider;
import robor.wildfiremapper.utils.rx.SchedulerProvider;

@Module
public class ActivityModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

//    @Provides
//    @PerActivity
//    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(
//            SplashPresenter<SplashMvpView> presenter) {
//        return presenter;
//    }
//
//    @Provides
//    AboutMvpPresenter<AboutMvpView> provideAboutPresenter(
//            AboutPresenter<AboutMvpView> presenter) {
//        return presenter;
//    }
//
//    @Provides
//    @PerActivity
//    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(
//            LoginPresenter<LoginMvpView> presenter) {
//        return presenter;
//    }
//
//    @Provides
//    @PerActivity
//    MainMvpPresenter<MainMvpView> provideMainPresenter(
//            MainPresenter<MainMvpView> presenter) {
//        return presenter;
//    }
//
//    @Provides
//    RatingDialogMvpPresenter<RatingDialogMvpView> provideRateUsPresenter(
//            RatingDialogPresenter<RatingDialogMvpView> presenter) {
//        return presenter;
//    }
//
//    @Provides
//    FeedMvpPresenter<FeedMvpView> provideFeedPresenter(
//            FeedPresenter<FeedMvpView> presenter) {
//        return presenter;
//    }
//
//    @Provides
//    OpenSourceMvpPresenter<OpenSourceMvpView> provideOpenSourcePresenter(
//            OpenSourcePresenter<OpenSourceMvpView> presenter) {
//        return presenter;
//    }
//
//    @Provides
//    BlogMvpPresenter<BlogMvpView> provideBlogMvpPresenter(
//            BlogPresenter<BlogMvpView> presenter) {
//        return presenter;
//    }
//
//    @Provides
//    FeedPagerAdapter provideFeedPagerAdapter(AppCompatActivity activity) {
//        return new FeedPagerAdapter(activity.getSupportFragmentManager());
//    }
//
//    @Provides
//    OpenSourceAdapter provideOpenSourceAdapter() {
//        return new OpenSourceAdapter(new ArrayList<OpenSourceResponse.Repo>());
//    }
//
//    @Provides
//    BlogAdapter provideBlogAdapter() {
//        return new BlogAdapter(new ArrayList<BlogResponse.Blog>());
//    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }
}
