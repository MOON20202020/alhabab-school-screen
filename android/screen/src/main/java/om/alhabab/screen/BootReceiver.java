package om.alhabab.screen;
import android.content.*;
public class BootReceiver extends BroadcastReceiver {
  @Override public void onReceive(Context c, Intent i) {
    try { Intent a = new Intent(c, MainActivity.class); a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); c.startActivity(a); } catch (Exception e) {}
  }
}
