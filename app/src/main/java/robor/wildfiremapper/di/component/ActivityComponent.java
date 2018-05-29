package robor.wildfiremapper.di.component;

import dagger.Component;
import robor.wildfiremapper.di.PerActivity;
import robor.wildfiremapper.di.module.ActivityModule;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

//    void inject(MainActivity activity);
//
//    void inject(LoginActivity activity);
//
//    void inject(SplashActivity activity);
//
//    void inject(FeedActivity activity);
//
//    void inject(AboutFragment fragment);
//
//    void inject(OpenSourceFragment fragment);
//
//    void inject(BlogFragment fragment);
//
//    void inject(RateUsDialog dialog);

}
