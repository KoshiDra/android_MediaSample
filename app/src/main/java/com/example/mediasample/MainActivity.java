package com.example.mediasample;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // メディアプレーヤー
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.player = new MediaPlayer();

        // 音声ファイルのURI文字列の作成
        // ----------------------------------------------------------------------
        // アプリ内のリソース音声ファイルのURI文字列
        // 「android.resource://アプリのルートパッケージ/リソースファイルのR値」
        // ----------------------------------------------------------------------
        String mediaFileUriStr = "android.resource://" + getPackageName() + "/" + R.raw.sample;

        // 音声ファイルのURI文字列からURIオブジェクトの作成
        Uri mediaFileUri = Uri.parse(mediaFileUriStr);

        try {
            // メディアプレーヤーに音声ファイルを設定
            this.player.setDataSource(MainActivity.this, mediaFileUri);

            // 非同期でのメディア再生準備完了時のリスナ設定
            this.player.setOnPreparedListener(new PlayerPreparedListener());

            // メディア再生終了時のリスナ設定
            this.player.setOnCompletionListener(new PlayerCompletionListener());

            // 非同期でのメディア再生準備
            this.player.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPlayButtonClick(View view) {

        Button button = findViewById(R.id.btPlay);

        // プレーヤー再生中
        if (this.player.isPlaying()) {
            // プレーヤー一時停止
            this.player.pause();
            // ラベルを「再生」に変更
            button.setText(R.string.bt_play_play);
        } else {
            // プレーヤーを再生し、ラベルを「一時停止」に変更
            this.player.start();
            button.setText(R.string.bt_play_pause);
        }
    }

    @Override
    protected void onDestroy() {

        // 再生中であれば停止
        if (this.player.isPlaying()) {
            this.player.stop();
        }

        // プレーヤーを解放
        this.player.release();

        // nullを設定
        this.player = null;

        super.onDestroy();
    }

    /**
     * プレーヤーの再生準備が整った時のリスナ
     */
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            // 各ボタンをタップ可能に変更
            Button btPlay = findViewById(R.id.btPlay);
            btPlay.setEnabled(true);

            Button btBack = findViewById(R.id.btBack);
            btBack.setEnabled(true);

            Button btForward = findViewById(R.id.btForward);
            btForward.setEnabled(true);
        }
    }

    /**
     * 再生が終了した時のリスナ
     */
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // 再生ボタンのラベルを「再生」に変更
            Button button = findViewById(R.id.btPlay);
            button.setText(R.string.bt_play_play);
        }
    }
}