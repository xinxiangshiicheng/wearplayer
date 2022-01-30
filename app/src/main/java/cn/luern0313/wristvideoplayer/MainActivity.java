package cn.luern0313.wristvideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.luern0313.wristvideoplayer.about.InformationsActivity;
import cn.luern0313.wristvideoplayer.game.GamelistActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void playlocatvideo(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,PlayActivity.class);
        startActivity(intent);
    }
    public void game(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, GamelistActivity.class);
        startActivity(intent);
    }
    public void setting(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,SettingActivity.class);
        startActivity(intent);
    }
    public void about(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,InformationsActivity.class);
        startActivity(intent);
    }
}