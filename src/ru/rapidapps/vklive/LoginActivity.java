package ru.rapidapps.vklive;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.perm.kate.api.Api;
import com.perm.kate.api.Auth;


public class LoginActivity extends Activity implements OnClickListener{
	
	Button btnSignIn, btnSignUp;
	static LinearLayout layout1;
	static RelativeLayout layout2;
	WebView webview;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
	    setupUI();    
	}
	
		// Объвление компонентов
	private void setupUI() {
		webview = (WebView) findViewById(R.id.webview);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        layout1.setVisibility(layout1.INVISIBLE);
        layout2.setVisibility(layout2.VISIBLE);
		btnSignIn = (Button) findViewById(R.id.btnSignIn);
		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignIn.setOnClickListener(this);
		btnSignUp.setOnClickListener(this);
	}

		// Нажатие кнопок
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSignIn: // Авторизация	
			Authorize();
			break;
		case R.id.btnSignUp: // Регистрация
			Register();
			break;
		}
	}
	
	/****************************** Begin Авторизация ******************************/
	
	private void Authorize() {
		layout1.setVisibility(layout1.VISIBLE);
		layout2.setVisibility(layout2.INVISIBLE);
		webview.getSettings().setJavaScriptEnabled(true);
        webview.clearCache(true); 
        	//Чтобы получать уведомления об окончании загрузки страницы
        webview.setWebViewClient(new VkontakteWebViewClient()); 
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        
        String url = Auth.getUrl(getString(R.string.app_id), Auth.getSettings());
        webview.loadUrl(url);
	}
	
	class VkontakteWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            parseUrl(url);
        }
    }
	
	private void parseUrl(String url) {
        try {
            if(url == null)
                return;
            if(url.startsWith(Auth.redirect_url))
            {
                if(!url.contains("error=")){
                	String[] auth = Auth.parseRedirectUrl(url);
                    MainActivity.account.access_token = auth[0];
                    MainActivity.account.user_id = Long.parseLong(auth[1]);
                    MainActivity.account.save(this);
                    
                    MainActivity.api = new Api(MainActivity.account.access_token, getString(R.string.app_id));  
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/****************************** End Авторизация ******************************/
	
	/****************************** Begin Регистрация ******************************/
	
	private void Register() {
		
	}
	
	/****************************** End Регистрация ******************************/

}
