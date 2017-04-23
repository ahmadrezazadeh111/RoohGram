package com.vizitsaz;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.appgostaran.admob.click.Visiblity;

import org.telegram.messenger.R;

import ir.adad.Adad;

/**
 * Created by one of six on 1/26/2016.
 */
public class ClickBanner_CLickYab_Holder extends LinearLayout {
    Context context;
    static Boolean TestMode = false;
    public static void setTestMode()
    {
        TestMode = true;
    }
    public ClickBanner_CLickYab_Holder(Context context) {
        super(context);
        this.context = context;
        check();
    }

    public ClickBanner_CLickYab_Holder(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        check();
    }

    @TargetApi(11)
    public ClickBanner_CLickYab_Holder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        check();
    }

    @TargetApi(21)
    public ClickBanner_CLickYab_Holder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        check();
    }

    void check()
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.clickbanner_clickyab_holder, this, true);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        if(TestMode)
        {
            RelativeLayout layout_adad = (RelativeLayout) findViewById(R.id.layout_ad);
            layout_adad.setVisibility(VISIBLE);
            Adad.setTestMode(true);
        }
        else {
            _Visibelity _visibelity = new _Visibelity();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                _visibelity.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                _visibelity.execute();
            }
        }

    }

    private class _Visibelity extends AsyncTask<Void, Void, Boolean[]>
    {
        @Override
        protected Boolean[] doInBackground(Void... params) {
            Visiblity visiblity = new Visiblity();
            Boolean[] temp = {false, false};
            temp[0] = visiblity.isAdadVisible(context);
            temp[1] = visiblity.isClickyabVisible(context);
            return temp;
        }

        @Override
        protected void onPostExecute(Boolean[] b) {
            RelativeLayout layout_clickyab = (RelativeLayout) findViewById(R.id.holder_clickyab);
            RelativeLayout layout_adad = (RelativeLayout) findViewById(R.id.layout_ad);
            if(b[0])
            {
                layout_adad.setVisibility(VISIBLE);
            }
            else
            {
                layout_adad.setVisibility(GONE);
            }
            if(b[1])
            {
                layout_clickyab.setVisibility(VISIBLE);
            }
            else
            {
                layout_clickyab.setVisibility(GONE);
            }
            super.onPostExecute(b);
        }
    }

}
