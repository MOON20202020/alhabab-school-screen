package om.alhabab.screen;
import android.app.Activity;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.*;
import android.view.*;
import android.webkit.*;

public class MainActivity extends Activity {
  static final String URL = "https://moon20202020.github.io/alhabab-school-screen/";
  WebView web; final Handler h = new Handler(Looper.getMainLooper());
  final Runnable retry = new Runnable() { public void run() { if (web != null) web.loadUrl(URL + "?t=" + System.currentTimeMillis()); } };

  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN
        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    web = new WebView(this);
    web.setBackgroundColor(Color.WHITE);
    web.setKeepScreenOn(true);
    WebSettings s = web.getSettings();
    s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setDatabaseEnabled(true);
    s.setMediaPlaybackRequiresUserGesture(false);
    s.setLoadWithOverviewMode(true); s.setUseWideViewPort(true);
    s.setCacheMode(WebSettings.LOAD_DEFAULT);
    if (Build.VERSION.SDK_INT >= 21) s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
    web.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    web.setWebChromeClient(new WebChromeClient());
    web.setWebViewClient(new WebViewClient() {
      @Override public void onReceivedError(WebView v, WebResourceRequest r, WebResourceError e) {
        if (r.isForMainFrame()) { h.removeCallbacks(retry); h.postDelayed(retry, 15000); }
      }
      @Override public boolean onRenderProcessGone(WebView v, RenderProcessGoneDetail d) { recreate(); return true; }
    });
    setContentView(web);
    hideBars();
    web.loadUrl(URL);
  }
  void hideBars() {
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
  }
  @Override public void onWindowFocusChanged(boolean f) { super.onWindowFocusChanged(f); if (f) hideBars(); }
  @Override protected void onResume() { super.onResume(); if (web != null) web.onResume(); hideBars(); }
  @Override public void onBackPressed() { /* شاشة عرض: زر الرجوع لا يغلقها */ }
}
