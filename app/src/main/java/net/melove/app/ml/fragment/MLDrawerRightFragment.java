package net.melove.app.ml.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.melove.app.ml.R;

/**
 * Created by Administrator on 2015/3/31.
 */
public class MLDrawerRightFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ml_drawerlayout_right, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){

    }
}
