package com.jiyehoo.opao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //退出
        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //询问是否开始
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("确定要开始O泡时间？")
                .setMessage("开始之后音量会最大，并锁住back。\n但您可以任务管理器关闭。或者点击按钮")
                .setPositiveButton("开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //开始播放
                        startOPao();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //不开始，退出
                        finish();
                    }
                }).show();

    }

    //开始播放
    private void startOPao() {

        //载入
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        //循环
        mediaPlayer.setLooping(true);
        try {
            //准备
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        //设置为最大音量
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);

        //开始播放
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //释放
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    //屏蔽按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //音量+，-,back三个按钮
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this, "按钮被屏蔽", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}