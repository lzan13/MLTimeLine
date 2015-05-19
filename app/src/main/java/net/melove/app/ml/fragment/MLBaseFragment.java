package net.melove.app.ml.fragment;


import android.app.Fragment;

/**
 * Created by Administrator on 2015/4/17.
 */
public class MLBaseFragment extends Fragment {


    public static interface MLFragmentCallback{
        public void mlClickListener(int i);
    }

}
