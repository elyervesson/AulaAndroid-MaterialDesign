package br.com.thiengo.tcmaterialdesign.interfaces;

import android.view.View;

/**
 * Created by elyervesson on 15/05/17.
 */

public interface RecyclerViewOnClickListenerHack {
    public void onClickListener(View view, int position);

    public void onLongPressClickListener(View view, int position);
}
