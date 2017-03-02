package com.examen.aloglife;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Created by Girondins on 2017-02-28.
 */
public class MainActivity extends AndroidApplication {
    private WebView wb;
    private static final String CALLBACK_URL = "https://localhost";
    private final String CLIENT_ID ="daacd362-05d2-4d15-ab2c-ed07848469d4";
    private Controller cont;
    private RelativeLayout loading;
    private Button login;
    private RelativeLayout loginPanel;
    private boolean onLogin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RequestQueue queue = Volley.newRequestQueue(this);
        loading = (RelativeLayout) findViewById(R.id.loadingPanel);
        loginPanel = (RelativeLayout) findViewById(R.id.loginPanel);
        login = (Button) findViewById(R.id.loginBtn);
        wb = (WebView) findViewById(R.id.authWebID);
        wb.clearCache(true);
        cont = new Controller(this);
        loading.setBackgroundColor(Color.TRANSPARENT);



        final AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.r = cfg.g = cfg.b = cfg.a = 8;
        View spine = initializeForView(new SimpleTestLog(),cfg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        spine.setLayoutParams(params);
        loading.addView(spine);


        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.setZOrderOnTop(true);
        }


        loading.setVisibility(View.GONE);
        loginPanel.setVisibility(View.GONE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wb.setVisibility(View.VISIBLE);
                loginPanel.setVisibility(View.GONE);
                onLogin = true;
            }
        });





        wb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("api")){
                    loginPanel.setVisibility(View.VISIBLE);
                    wb.setVisibility(View.GONE);
                }

                if(url.contains("localhost")) {
                    wb.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    Log.d("code",url);
                    Uri uri =  Uri.parse( url );;
                    final String codice=(uri.getQueryParameter("code").toString());
                    Log.d("codice", codice);
                    cont.setAuthCode(codice);
                    //          thread.execute(new GetUserToken(codice));
                    return true;
                }
                Log.d("Returing False", url);
                // return true; //Indicates WebView to NOT load the url;
                return false; //Allow WebView to load url
            }
        });
        wb.loadUrl("https://platform.lifelog.sonymobile.com/oauth/2/authorize?client_id="+CLIENT_ID+ "&scope=lifelog.profile.read+lifelog.activities.read+lifelog.locations.read");
    }


    @Override
    public void onBackPressed() {
        if(onLogin == true){
            loginPanel.setVisibility(View.VISIBLE);
            wb.setVisibility(View.GONE);
            onLogin = false;
            wb.loadUrl("https://platform.lifelog.sonymobile.com/oauth/2/authorize?client_id="+CLIENT_ID+ "&scope=lifelog.profile.read+lifelog.activities.read+lifelog.locations.read");
        }else
            super.onBackPressed();
    }

}