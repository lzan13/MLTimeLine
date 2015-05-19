package net.melove.app.ml.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.melove.app.ml.R;
import net.melove.app.ml.views.MLToast;

/**
 * Created by Administrator on 2015/4/18.
 */
public class MLForgetPassFragment extends MLBaseFragment {

    private Activity mActivity;
    private MLFragmentCallback mlCallback;

    private Button mFindPassBtn;
    private Button mFindPassGotoSigninBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();

        View view = inflater.inflate(R.layout.ml_signforgetpass_fragment_layout, container, false);
        initView(view);

        return view;
    }


    private void initView(View view) {
        mFindPassBtn = (Button) view.findViewById(R.id.ml_btn_find_pass);
        mFindPassGotoSigninBtn = (Button) view.findViewById(R.id.ml_btn_find_pass_goto_signin);
        mFindPassBtn.setOnClickListener(listener);
        mFindPassGotoSigninBtn.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ml_btn_find_pass) {
                MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp,
                        mActivity.getResources().getString(R.string.ml_toast_forget_password)).show();
            }
            mlCallback.mlClickListener(v.getId());
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mlCallback = (MLFragmentCallback) activity;
    }
}
