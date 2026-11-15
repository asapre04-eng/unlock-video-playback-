package com.lightlink.unlockvideo;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.FrameLayout;

public class UnlockAccessibilityService extends AccessibilityService implements TextureView.SurfaceTextureListener {
    private WindowManager wm;
    private FrameLayout overlay;
    private TextureView texture;
    private MediaPlayer player;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private long lastShown = 0;
    private boolean pendingSurface = false;

    @Override public void onServiceConnected() { super.onServiceConnected(); wm = (WindowManager)getSystemService(WINDOW_SERVICE); }

    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null) return;
        int type = event.getEventType();
        if (type != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && type != AccessibilityEvent.TYPE_WINDOWS_CHANGED) return;
        KeyguardManager km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        if (km != null && !km.isKeyguardLocked()) {
            long now = SystemClock.elapsedRealtime();
            if (now - lastShown > 2500 && overlay == null) {
                lastShown = now;
                showVideo();
            }
        }
    }

    private void showVideo() {
        try {
            overlay = new FrameLayout(this);
            texture = new TextureView(this);
            texture.setSurfaceTextureListener(this);
            overlay.addView(texture, new FrameLayout.LayoutParams(-1, -1, Gravity.CENTER));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
            lp.gravity = Gravity.TOP | Gravity.START;
            wm.addView(overlay, lp);
            pendingSurface = true;
            handler.postDelayed(this::removeVideo, 10_000);
        } catch (Exception e) { removeVideo(); }
    }

    @Override public void onSurfaceTextureAvailable(android.graphics.SurfaceTexture st, int width, int height) {
        try {
            player = new MediaPlayer();
            player.setDataSource(getAssets().openFd("unlock.mp4"));
            player.setSurface(new Surface(st));
            player.setOnPreparedListener(mp -> { if (overlay != null) mp.start(); });
            player.setOnCompletionListener(mp -> removeVideo());
            player.setOnErrorListener((mp, what, extra) -> { removeVideo(); return true; });
            player.prepareAsync();
        } catch (Exception e) { removeVideo(); }
    }
    @Override public void onSurfaceTextureSizeChanged(android.graphics.SurfaceTexture st, int w, int h) {}
    @Override public boolean onSurfaceTextureDestroyed(android.graphics.SurfaceTexture st) { return true; }
    @Override public void onSurfaceTextureUpdated(android.graphics.SurfaceTexture st) {}

    private void removeVideo() {
        handler.removeCallbacksAndMessages(null);
        if (player != null) { try { if (player.isPlaying()) player.stop(); } catch (Exception ignored) {} try { player.release(); } catch (Exception ignored) {} player = null; }
        if (overlay != null && wm != null) { try { wm.removeView(overlay); } catch (Exception ignored) {} overlay = null; }
        texture = null;
    }
    @Override public void onInterrupt() { removeVideo(); }
    @Override public void onDestroy() { removeVideo(); super.onDestroy(); }
}
