package com.example.mediasample;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

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

        // スイッチ取得
        SwitchMaterial loopSwitch = findViewById(R.id.swLoop);
        // リスナ設定
        loopSwitch.setOnCheckedChangeListener(new LoopSwitchChangedListener());
    }

    /**
     * 再生ボタンタップ字の処理
     * @param view
     */
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

    /**
     * 「<<」ボタンタップ時の処理
     * @param view
     */
    public void onBackButtonClick(View view) {
        // 再生位置を先頭に変更
        this.player.seekTo(0);
    }

    /**
     * 「>>」ボタンタップ時の処理
     * @param view
     */
    public void onForwardButtonClick(View view) {

        // 現在再生中のメディアの長さを取得
        int duration = this.player.getDuration();

        // 再生位置を終焉に変更
        this.player.seekTo(duration);

        // 再生中でなければ再生実行
        if (!this.player.isPlaying()) {
            this.player.start();
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
            if (!player.isLooping()) {
                // 再生ボタンのラベルを「再生」に変更
                Button button = findViewById(R.id.btPlay);
                button.setText(R.string.bt_play_play);
            }
        }
    }

    /**
     * スイッチ変更時のリスナ
     */
    private class LoopSwitchChangedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // ループ設定
            player.setLooping(isChecked);
        }
    }
}