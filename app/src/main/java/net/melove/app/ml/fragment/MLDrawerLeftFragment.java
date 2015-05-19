package net.melove.app.ml.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.melove.app.ml.MLApp;
import net.melove.app.ml.R;
import net.melove.app.ml.activity.MLSignActivity;
import net.melove.app.ml.adapter.MLDrawerLeftAdapter;
import net.melove.app.ml.constants.MLAppConstant;
import net.melove.app.ml.db.MLDBConstants;
import net.melove.app.ml.db.MLDBHelper;
import net.melove.app.ml.info.UserInfo;
import net.melove.app.ml.utils.MLFile;
import net.melove.app.ml.utils.MLSPUtil;
import net.melove.app.ml.utils.MLScreen;
import net.melove.app.ml.views.MLFilterImageView;
import net.melove.app.ml.views.MLImageView;
import net.melove.app.ml.views.MLToast;


/**
 * Created by Administrator on 2015/3/27.
 */
public class MLDrawerLeftFragment extends MLBaseFragment {

    private Activity mActivity;
    private MLFragmentCallback mlCallback;


    private MLDrawerLeftAdapter mAdapter;
    private ListView mListView;

    private UserInfo mUserInfo;
    private UserInfo mSpouseInfo;

    private MLFilterImageView mUserCover;
    private MLImageView mUserAvatar;
    private MLImageView mSpouseAvatar;
    private TextView mNickname;
    private TextView mSignature;

    // ListView 选项以外的 设置和帮助按钮
    private View settingMenu;
    private View helpMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();

        View view = inflater.inflate(R.layout.ml_drawerlayout_left, container, false);

        initInfo();

        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化账户数据
     */
    private void initInfo() {
        MLDBHelper mldbHelper = MLDBHelper.getInstance();
        if (mldbHelper == null) {
            mlCallback.mlClickListener(10);
            return;
        }
        String s1 = MLDBConstants.COL_ACCESS_TOKEN + "=?";
        String args1[] = new String[]{(String) MLSPUtil.get(mActivity, MLDBConstants.COL_ACCESS_TOKEN, "")};
        Cursor c1 = mldbHelper.queryData(MLDBConstants.TB_USER, null, s1, args1, null, null, null, null);
        if (c1.moveToFirst()) {
            do {
                mUserInfo = new UserInfo(c1);
            } while (c1.moveToNext());
        }

        String s2 = MLDBConstants.COL_USER_ID + "=?";
        String args2[] = new String[]{mUserInfo.getSpouseId()};
        Cursor c2 = mldbHelper.queryData(MLDBConstants.TB_USER, null, s2, args2, null, null, null, null);
        if (c2.moveToFirst()) {
            do {
                mSpouseInfo = new UserInfo(c2);
            } while (c2.moveToNext());
        }
        mldbHelper.closeDatabase();
    }

    private void initView(View view) {
        mUserCover = (MLFilterImageView) view.findViewById(R.id.ml_img_drawer_left_cover);
        mUserAvatar = (MLImageView) view.findViewById(R.id.ml_img_user_avatar);
        mSpouseAvatar = (MLImageView) view.findViewById(R.id.ml_img_spouse_avatar);
        mNickname = (TextView) view.findViewById(R.id.ml_text_drawer_user_nickname);
        mSignature = (TextView) view.findViewById(R.id.ml_text_drawer_user_signature);

        if (mUserInfo != null) {
            if (!mUserInfo.getCover().equals(null)) {
                String userCoverPath = MLApp.getUserImage() + mUserInfo.getCover();
                Bitmap cover = MLFile.fileToBitmap(userCoverPath);
                if (cover != null) {
                    mUserCover.setImageBitmap(cover);
                }
            }
            if (!mUserInfo.getAvatar().equals(null)) {
                String userAvatarPath = MLApp.getUserImage() + mUserInfo.getAvatar();
                Bitmap avatar = MLFile.fileToBitmap(userAvatarPath);
                if (avatar != null) {
                    mUserAvatar.setImageBitmap(avatar);
                }
            }
            mNickname.setText(mUserInfo.getNickname());
            mSignature.setText(mUserInfo.getSignature());
        }
        if (mSpouseInfo != null) {
            if (mSpouseInfo.getAvatar() != null) {
                String spouseAvatarPath = MLApp.getUserImage() + mSpouseInfo.getAvatar();
                Bitmap avatar = MLFile.fileToBitmap(spouseAvatarPath);
                if (avatar != null) {
                    mSpouseAvatar.setImageBitmap(avatar);
                }
            }
        }
        mUserAvatar.setOnClickListener(viewListener);

        mListView = (ListView) view.findViewById(R.id.ml_drawer_menu_listview);
        mAdapter = new MLDrawerLeftAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setItemChecked(0, true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mlCallback.mlClickListener(position);
            }
        });

        //获取并设置按钮监听事件
        settingMenu = view.findViewById(R.id.ml_drawer_menu_setting);
        helpMenu = view.findViewById(R.id.ml_drawer_menu_help);

        settingMenu.setOnClickListener(viewListener);
        helpMenu.setOnClickListener(viewListener);
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mlCallback.mlClickListener(v.getId());
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mlCallback = (MLFragmentCallback) activity;
    }

}
