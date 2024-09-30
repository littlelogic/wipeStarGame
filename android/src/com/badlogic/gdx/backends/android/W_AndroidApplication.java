package com.badlogic.gdx.backends.android;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gdx.game.android.LogAndroid;

import java.lang.reflect.Method;

public class W_AndroidApplication extends AndroidApplication {

    public void initialize (ApplicationListener listener) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(listener, config);
    }

    public void initialize (ApplicationListener listener, AndroidApplicationConfiguration config) {
        init(listener, config, false);
    }

    public View initializeForView (ApplicationListener listener) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        return initializeForView(listener, config);
    }

    public View initializeForView (ApplicationListener listener, AndroidApplicationConfiguration config) {
        init(listener, config, true);
        return graphics.getView();
    }

    private void init (ApplicationListener listener, AndroidApplicationConfiguration config, boolean isForView) {
        if (this.getVersion() < MINIMUM_SDK) {
            throw new GdxRuntimeException("libGDX requires Android API Level " + MINIMUM_SDK + " or later.");
        }
        GdxNativesLoader.load();
        setApplicationLogger(new AndroidApplicationLogger());
        graphics = new AndroidGraphics(this, config,
                config.resolutionStrategy == null ? new FillResolutionStrategy() : config.resolutionStrategy);
        input = createInput(this, this, graphics.view, config);
        audio = createAudio(this, config);
        files = createFiles();
        net = new AndroidNet(this, config);
        this.listener = listener;
        this.handler = new Handler();
        this.useImmersiveMode = config.useImmersiveMode;
        this.clipboard = new AndroidClipboard(this);

        // Add a specialized audio lifecycle listener
        addLifecycleListener(new LifecycleListener() {

            @Override
            public void resume () {
                // No need to resume audio here
            }

            @Override
            public void pause () {
                audio.pause();
            }

            @Override
            public void dispose () {
                audio.dispose();
            }
        });

        Gdx.app = this;
        Gdx.input = this.getInput();
        Gdx.audio = this.getAudio();
        Gdx.files = this.getFiles();
        Gdx.graphics = this.getGraphics();
        Gdx.net = this.getNet();

        if (!isForView) {
            try {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
            } catch (Exception ex) {
                log("AndroidApplication", "Content already displayed, cannot request FEATURE_NO_TITLE", ex);
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            setContentView(graphics.getView(), createLayoutParams());

            ///<<--------------------
            FrameLayout mFrameLayout=new FrameLayout(this);
            /**
             * 禁止多点触控
             */
            mFrameLayout.setMotionEventSplittingEnabled(false);
            setContentView(mFrameLayout, createLayoutParams());
            mFrameLayout.addView(graphics.getView(),createLayoutParams());
            TextView mTextView=new TextView(this);
            LogAndroid.mTextView=mTextView;//---temp
            mTextView.setText("wjw");
            mTextView.setTextColor(Color.WHITE);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.TOP|Gravity.RIGHT;
            mTextView.setLayoutParams(layoutParams);
            mFrameLayout.addView(mTextView);
            ///>>--------------------
        }

        createWakeLock(config.useWakelock);
        useImmersiveMode(this.useImmersiveMode);
        if (this.useImmersiveMode && getVersion() >= Build.VERSION_CODES.KITKAT) {
            AndroidVisibilityListener vlistener = new AndroidVisibilityListener();
            vlistener.createListener(this);
        }

        // detect an already connected bluetooth keyboardAvailable
        if (getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS) input.setKeyboardAvailable(true);
    }

}
