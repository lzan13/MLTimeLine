package net.melove.app.ml.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import net.melove.app.ml.R;
import net.melove.app.ml.activity.MLCommemoratePutActivity;
import net.melove.app.ml.utils.MLScreen;

/**
 * Created by lzan13 on 2015/5/20.
 */
public class MLCommemorateFragment extends MLBaseFragment {

    private Activity mActivity;

    private ImageButton mAddNoteBtn;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = getActivity();

        View view = inflater.inflate(R.layout.ml_commemorate_fragment_layout, null);

        initFragment(view);
        return view;
    }

    private void initFragment(View view) {
        mAddNoteBtn = (ImageButton) view.findViewById(R.id.ml_imgbtn_commemorate_add);
        mAddNoteBtn.setOnClickListener(viewListener);

        if (MLScreen.getNavigationBarHeight() > 0) {
            LinearLayout reservedBottomLayout = (LinearLayout) view.findViewById(R.id.ml_reserved_layout_bottom);
            View v = new View(mActivity);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MLScreen.getNavigationBarHeight()));
            reservedBottomLayout.addView(v);
        }
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            ActivityOptionsCompat optionsCompat = null;
            switch (v.getId()) {
                case R.id.ml_imgbtn_commemorate_add:
                    intent.setClass(mActivity, MLCommemoratePutActivity.class);
                    optionsCompat = ActivityOptionsCompat.makeCustomAnimation(mActivity,
                            R.anim.ml_fade_in, R.anim.ml_fade_out);
                    ActivityCompat.startActivity(mActivity, intent, optionsCompat.toBundle());
                    mActivity.finish();
            }
        }
    };
}
