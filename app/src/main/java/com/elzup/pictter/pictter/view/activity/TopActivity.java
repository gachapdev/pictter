package com.elzup.pictter.pictter.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.elzup.pictter.pictter.R;


public class TopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
		// タイトルを非表示にします。
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// splash.xmlをViewに指定します。
		setContentView(R.layout.top_view);
		Handler hdl = new Handler();
		// 500ms遅延させてsplashHandlerを実行します。
		hdl.postDelayed(new splashHandler(), 500);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class splashHandler implements Runnable {
        public void run() {
            // スプラッシュ完了後に実行するActivityを指定します。
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            // SplashActivityを終了させます。
            TopActivity.this.finish();
        }
    }
}
