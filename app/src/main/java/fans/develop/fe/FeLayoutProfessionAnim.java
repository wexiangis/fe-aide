package fans.develop.fe;

import android.content.Context;
import android.widget.RelativeLayout;

public class FeLayoutProfessionAnim extends FeLayout {

    public FeLayoutProfessionAnim(Context context) {
        super(context);
        //
    }

    /* ---------- abstract interface ---------- */

    public boolean onKeyBack() {
        return false;
    }

    public boolean onDestory() {
        //释放子view
        _removeViewAll(this);
        return true;
    }

    public void onReload() {
        ;
    }
}
