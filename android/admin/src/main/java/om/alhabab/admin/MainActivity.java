package om.alhabab.admin;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.webkit.*;

public class MainActivity extends Activity {
  static final String URL = "https://moon20202020.github.io/alhabab-school-screen/#admin";
  static final int PICK = 7;
  WebView web; ValueCallback<Uri[]> pending;

  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    if (Build.VERSION.SDK_INT >= 21) getWindow().setStatusBarColor(0xFF0EA5E9);
    web = new WebView(this);
    WebSettings s = web.getSettings();
    s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setDatabaseEnabled(true);
    s.setAllowFileAccess(true); s.setMediaPlaybackRequiresUserGesture(false);
    s.setLoadWithOverviewMode(true); s.setUseWideViewPort(true);
    CookieManager.getInstance().setAcceptCookie(true);
    if (Build.VERSION.SDK_INT >= 21) CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
    web.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
        String u = r.getUrl().toString();
        if (u.startsWith("https://moon20202020.github.io/") || u.contains("firebase") || u.contains("googleapis")) return false;
        try { startActivity(new Intent(Intent.ACTION_VIEW, r.getUrl())); } catch (Exception e) {}
        return true;
      }
    });
    web.setWebChromeClient(new WebChromeClient() {
      @Override public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> cb, FileChooserParams p) {
        if (pending != null) pending.onReceiveValue(null);
        pending = cb;
        try { startActivityForResult(p.createIntent(), PICK); }
        catch (Exception e) {
          Intent i = new Intent(Intent.ACTION_GET_CONTENT); i.addCategory(Intent.CATEGORY_OPENABLE); i.setType("*/*");
          try { startActivityForResult(i, PICK); } catch (Exception e2) { pending = null; return false; }
        }
        return true;
      }
    });
    setContentView(web);
    if (b != null) web.restoreState(b); else web.loadUrl(URL);
  }
  @Override protected void onActivityResult(int req, int res, Intent data) {
    if (req == PICK && pending != null) { pending.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(res, data)); pending = null; }
    else super.onActivityResult(req, res, data);
  }
  @Override protected void onSaveInstanceState(Bundle o) { super.onSaveInstanceState(o); web.saveState(o); }
  @Override public void onBackPressed() { if (web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}
