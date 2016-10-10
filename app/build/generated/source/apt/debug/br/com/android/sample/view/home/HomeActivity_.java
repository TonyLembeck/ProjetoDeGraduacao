//
// DO NOT EDIT THIS FILE.
// Generated using AndroidAnnotations 4.1.0.
// 
// You can create a larger work that contains this file and distribute that work under terms of your choice.
//

package br.com.android.sample.view.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import br.com.android.sample.R;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.builder.PostActivityStarter;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class HomeActivity_
    extends HomeActivity
    implements HasViews, OnViewChangedListener
{
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(R.layout.home_activity);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static HomeActivity_.IntentBuilder_ intent(Context context) {
        return new HomeActivity_.IntentBuilder_(context);
    }

    public static HomeActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new HomeActivity_.IntentBuilder_(fragment);
    }

    public static HomeActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new HomeActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        this.textView = ((TextView) hasViews.findViewById(R.id.acelerometro));
        View view_buttonAR = hasViews.findViewById(R.id.buttonAR);
        View view_mapss = hasViews.findViewById(R.id.mapss);

        if (view_buttonAR!= null) {
            view_buttonAR.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    HomeActivity_.this.onARClick();
                }
            }
            );
        }
        if (view_mapss!= null) {
            view_mapss.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    HomeActivity_.this.onMapsClick();
                }
            }
            );
        }
        onAfterViews();
    }

    @Override
    public void onARClick() {
        UiThreadExecutor.runTask("", new Runnable() {

            @Override
            public void run() {
                HomeActivity_.super.onARClick();
            }
        }
        , 0L);
    }

    @Override
    public void onMapsClick() {
        UiThreadExecutor.runTask("", new Runnable() {

            @Override
            public void run() {
                HomeActivity_.super.onMapsClick();
            }
        }
        , 0L);
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<HomeActivity_.IntentBuilder_>
    {
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, HomeActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), HomeActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), HomeActivity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public PostActivityStarter startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent, requestCode, lastOptions);
                } else {
                    if (context instanceof Activity) {
                        Activity activity = ((Activity) context);
                        ActivityCompat.startActivityForResult(activity, intent, requestCode, lastOptions);
                    } else {
                        context.startActivity(intent, lastOptions);
                    }
                }
            }
            return new PostActivityStarter(context);
        }
    }
}
