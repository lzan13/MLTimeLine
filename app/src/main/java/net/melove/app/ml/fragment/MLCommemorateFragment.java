package net.melove.app.ml.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.melove.app.ml.R;

/**
 * Created by lzan13 on 2015/5/20.
 */
public class MLCommemorateFragment extends MLBaseFragment {

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = getActivity();

        View view = inflater.inflate(R.layout.ml_commemorate_fragment_layout, null);


        return view;
    }

    private void initFragment(View view){

    }
}
