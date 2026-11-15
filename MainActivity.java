package com.lightlink.unlockvideo;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 36, 28, 28);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundColor(Color.rgb(10, 24, 45));

        TextView title = new TextView(this);
        title.setText("Unlock Video Player"); title.setTextSize(26); title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER); title.setPadding(0, 12, 0, 22);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        TextView info = new TextView(this);
        info.setText("Unlock hone par aapka video 10 seconds ke liye screen par chalega. Video ke dauran neeche wali app par touch kaam karta rahega (device compatibility par nirbhar).\n\nSetup: neeche button dabayein, Accessibility mein Unlock Video Player ko ON karein, phir phone lock karke unlock karke test karein.");
        info.setTextSize(16); info.setTextColor(Color.WHITE); info.setPadding(0, 8, 0, 28);
        root.addView(info, new LinearLayout.LayoutParams(-1, -2));

        Button enable = new Button(this); enable.setText("Enable Unlock Detection");
        enable.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        root.addView(enable, new LinearLayout.LayoutParams(-1, -2));

        TextView note = new TextView(this);
        note.setText("Video: unlock.mp4 • Playback: 10 seconds\nAgar OriginOS service ko band kare, Accessibility settings mein service ko ON rakhein aur app ki battery restriction hata dein.");
        note.setTextColor(Color.LTGRAY); note.setTextSize(13); note.setPadding(0, 24, 0, 0);
        root.addView(note, new LinearLayout.LayoutParams(-1, -2));
        setContentView(root);
    }
}
