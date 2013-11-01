package com.cradle.iitc_mobile;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;

public class IITC_DrawerLayout extends DrawerLayout {

    public IITC_DrawerLayout(Context context) {
        super(context);
    }

    public IITC_DrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IITC_DrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        // super.fitSystemWindows doesn't work for us as it aborts after the first child returns true

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);

            if (view instanceof IITC_WebView) {
                ((IITC_WebView) view).fitSystemWindows(insets);
                continue;
            }

            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
        }
        return true;
    }
}
