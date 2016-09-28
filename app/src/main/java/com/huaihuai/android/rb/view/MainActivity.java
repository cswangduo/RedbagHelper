package com.huaihuai.android.rb.view;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.huaihuai.android.rb.R;
import com.huaihuai.android.rb.util.AccessibilityServiceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.backdrop)
    ImageView backdrop;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.cv_github)
    CardView cvGithub;
    @BindView(R.id.cv_jianshu)
    CardView cvJianshu;
    @BindView(R.id.cv_qrcode)
    CardView cvQrcode;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initCardView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isRunning = AccessibilityServiceHelper.isAccessibilitySettingsOn(getBaseContext());
        fab.setImageResource(isRunning ? R.drawable.ic_stop : R.drawable.ic_start);
    }

    private void initCardView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cvGithub.setForeground(getDrawable(R.drawable.default_selector));
            cvJianshu.setForeground(getDrawable(R.drawable.default_selector));
            cvQrcode.setForeground(getDrawable(R.drawable.default_selector));
        } else {
            cvGithub.setBackgroundResource(R.drawable.default_selector);
            cvJianshu.setBackgroundResource(R.drawable.default_selector);
            cvQrcode.setBackgroundResource(R.drawable.default_selector);
        }
    }

    @OnClick(R.id.fab)
    public void onClick() {
        AccessibilityServiceHelper.startService(MainActivity.this);
    }

}
