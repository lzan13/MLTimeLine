package net.melove.app.ml.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import net.melove.app.ml.MLApp;
import net.melove.app.ml.R;
import net.melove.app.ml.activity.MLNotePutActivity;
import net.melove.app.ml.activity.MLUserActivity;
import net.melove.app.ml.adapter.MLTimeLineAdapter;
import net.melove.app.ml.db.MLDBConstants;
import net.melove.app.ml.db.MLDBHelper;
import net.melove.app.ml.http.MLHttpConstants;
import net.melove.app.ml.http.MLHttpUtil;
import net.melove.app.ml.http.MLRequestParams;
import net.melove.app.ml.http.MLStringResponseListener;
import net.melove.app.ml.info.NoteInfo;
import net.melove.app.ml.info.UserInfo;
import net.melove.app.ml.utils.MLFile;
import net.melove.app.ml.utils.MLLog;
import net.melove.app.ml.utils.MLSPUtil;
import net.melove.app.ml.utils.MLScreen;
import net.melove.app.ml.views.MLFilterImageView;
import net.melove.app.ml.views.MLImageView;
import net.melove.app.ml.views.MLToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/26.
 */
public class MLTimeLineFragment extends MLBaseFragment {

    private Activity mActivity;
    private MLFragmentCallback mlCallback;

    private UserInfo mUserInfo;
    private UserInfo mSpouseInfo;

    private List<NoteInfo> mNoteInfoList;
    private MLTimeLineAdapter mlTimeLineAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private View mHeadView;
    private View mFooterView;

    private MLFilterImageView mUserCover;
    private MLImageView mUserAvatar;
    private MLImageView mSpouseAvatar;

    private ImageButton mAddNoteBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ml_note_timeline_fragment_layout, container, false);

        mActivity = getActivity();

        initInfo();

        initFragment(view);
        initSwipeRefreshLayout(view);
        initHeadView();
        initListView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private void initFragment(View view) {
        mListView = (ListView) view.findViewById(R.id.ml_listview_timeline);
        mHeadView = mActivity.getLayoutInflater().inflate(R.layout.ml_note_timeline_list_header, null);
        mFooterView = mActivity.getLayoutInflater().inflate(R.layout.ml_note_timeline_list_footer, null);

        mListView.addHeaderView(mHeadView);
        mListView.addFooterView(mFooterView);

        mAddNoteBtn = (ImageButton) view.findViewById(R.id.ml_imgbtn_add_note);
        mAddNoteBtn.setOnClickListener(viewListener);
        if (MLScreen.getNavigationBarHeight() > 0) {
            LinearLayout reservedBottomLayout = (LinearLayout) view.findViewById(R.id.ml_reserved_layout_bottom);
            View v = new View(mActivity);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MLScreen.getNavigationBarHeight()));
            reservedBottomLayout.addView(v);
        }
    }

    /**
     * 初始化下拉刷新组件
     *
     * @param view
     */
    private void initSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ml_swipe_refresh_layout);

        // 设置进度圈出现的位置及方式
//        mSwipeRefreshLayout.setProgressViewOffset(false, 0, MLScreen.dp2px(R.dimen.ml_dimen_48));
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.ml_blue, R.color.ml_orange,
                R.color.ml_green, R.color.ml_red);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        String url = MLHttpConstants.API_URL + MLHttpConstants.API_NOTE;
                        MLRequestParams params = new MLRequestParams();
                        params.putParams(MLDBConstants.COL_ACCESS_TOKEN, mUserInfo.getAccessToken());
                        MLHttpUtil.getInstance(mActivity).post(url, params,
                                new MLStringResponseListener() {
                                    @Override
                                    public void onFailure(int state, String content) {
                                        MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                                                mActivity.getResources().getString(R.string.ml_error_http)).show();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }

                                    @Override
                                    public void onSuccess(int state, String content) {
                                        parseNoteInfo(content);
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }

                                });
                    }
                });
    }

    /**
     * 初始化列表头部
     */
    private void initHeadView() {
        mUserCover = (MLFilterImageView) mHeadView.findViewById(R.id.ml_img_cover);
        mUserAvatar = (MLImageView) mHeadView.findViewById(R.id.ml_img_user_avatar);
        mSpouseAvatar = (MLImageView) mHeadView.findViewById(R.id.ml_img_spouse_avatar);

        mUserAvatar.setOnClickListener(viewListener);
        mSpouseAvatar.setOnClickListener(viewListener);

        if (mUserInfo != null) {
            if (!mUserInfo.getCover().equals("null")) {
                String userCoverPath = MLApp.getUserImage() + mUserInfo.getCover();
                Bitmap cover = MLFile.fileToBitmap(userCoverPath);
                if (cover != null) {
                    mUserCover.setImageBitmap(cover);
                } else {
                    String url = MLHttpConstants.UPLOAD_URL + mUserInfo.getSigninname()
                            + "/" + MLHttpConstants.IMAGE_URL + mUserInfo.getCover();
                    ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            if (loadedImage != null) {
                                mUserCover.setImageBitmap(loadedImage);
                                MLFile.saveBitmapToSDCard(loadedImage, MLApp.getUserImage() + mUserInfo.getCover());
                            }
                        }
                    });
                }
            }
            if (!mUserInfo.getAvatar().equals("null")) {
                String userAvatarPath = MLApp.getUserImage() + mUserInfo.getAvatar();
                Bitmap avatar = MLFile.fileToBitmap(userAvatarPath);
                if (avatar != null) {
                    mUserAvatar.setImageBitmap(avatar);
                } else {
                    String url = MLHttpConstants.UPLOAD_URL + mUserInfo.getSigninname()
                            + "/" + MLHttpConstants.IMAGE_URL + mUserInfo.getAvatar();
                    ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            if (loadedImage != null) {
                                mUserAvatar.setImageBitmap(loadedImage);
                                MLFile.saveBitmapToSDCard(loadedImage, MLApp.getUserImage() + mUserInfo.getAvatar());
                            }
                        }
                    });
                }
            }
        }
        if (mSpouseInfo != null) {
            if (!mSpouseInfo.getAvatar().equals("null")) {
                String spouseAvatarPath = MLApp.getUserImage() + mSpouseInfo.getAvatar();
                Bitmap avatar = MLFile.fileToBitmap(spouseAvatarPath);
                if (avatar != null) {
                    mSpouseAvatar.setImageBitmap(avatar);
                } else {
                    String url = MLHttpConstants.UPLOAD_URL + mSpouseInfo.getSigninname()
                            + "/" + MLHttpConstants.IMAGE_URL + mSpouseInfo.getAvatar();
                    ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            if (loadedImage != null) {
                                mSpouseAvatar.setImageBitmap(loadedImage);
                                MLFile.saveBitmapToSDCard(loadedImage, MLApp.getUserImage() + mSpouseInfo.getAvatar());
                            }
                        }
                    });
                }
            }
        }
        Animation avatarAnim = AnimationUtils.loadAnimation(mActivity, R.anim.ml_avatar_zoom_in);
        mUserAvatar.startAnimation(avatarAnim);
        mSpouseAvatar.startAnimation(avatarAnim);
    }

    private void parseNoteInfo(String content) {
        MLLog.d(content);
        Resources res = mActivity.getResources();
        String toastStr = "";
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (jsonObject.isNull("error")) {
                JSONArray jsonArray = jsonObject.getJSONArray("notes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject note = jsonArray.getJSONObject(i);
                    NoteInfo noteInfo = new NoteInfo(note);

                    ContentValues values = new ContentValues();
                    values.put(MLDBConstants.COL_LOVE_ID, noteInfo.getLoveId());
                    values.put(MLDBConstants.COL_USER_ID, noteInfo.getUserId());
                    values.put(MLDBConstants.COL_NOTE_ID, noteInfo.getNoteId());
                    values.put(MLDBConstants.COL_NOTE_TYPE, noteInfo.getNoteType());
                    values.put(MLDBConstants.COL_IMAGE, noteInfo.getImage());
                    values.put(MLDBConstants.COL_CONTENT, noteInfo.getContent());
                    values.put(MLDBConstants.COL_CREATE_AT, noteInfo.getCreateAt());

                    MLDBHelper mldbHelper = MLDBHelper.getInstance();
                    mldbHelper.insterData(MLDBConstants.TB_NOTE, values);
                    mldbHelper.closeDatabase();

                    toastStr = res.getString(R.string.ml_note_newest_success);
                    MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, toastStr).show();
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.sendToTarget();
                }

            } else {
                String error = jsonObject.getString("error");
                if (error.equals("null")) {
                    toastStr = res.getString(R.string.ml_note_newest_null);
                } else if (error.equals("user")) {
                    toastStr = res.getString(R.string.ml_user_access_token_overdue);
                }
                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, toastStr).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Note列表
     *
     * @param view
     */
    private void initListView(View view) {
        mNoteInfoList = new ArrayList<NoteInfo>();

        MLDBHelper mldbHelper = MLDBHelper.getInstance();
        if (mldbHelper != null) {
            if (mldbHelper != null) {
                String selection = MLDBConstants.COL_LOVE_ID + "=?";
                String args[] = {mUserInfo.getLoveId()};
                String orderBy = MLDBConstants.COL_CREATE_AT + " desc";
                Cursor cursor = mldbHelper.queryData(MLDBConstants.TB_NOTE, null, selection, args, null, null, orderBy, null);
                if (cursor.moveToFirst()) {
                    do {
                        NoteInfo temp = new NoteInfo(cursor);
                        if (temp.getUserId().equals(mUserInfo.getUserId())) {
                            temp.setUserInfo(mUserInfo);
                        } else {
                            temp.setUserInfo(mSpouseInfo);
                        }
                        mNoteInfoList.add(temp);
                    } while (cursor.moveToNext());
                }
            }
            mldbHelper.closeDatabase();
        }
        if (mNoteInfoList.size() == 0) {
            ViewStub viewStub = (ViewStub) mFooterView.findViewById(R.id.ml_empty_viewstub);
            viewStub.inflate();
        }
        mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        mlTimeLineAdapter = new MLTimeLineAdapter(mActivity, mNoteInfoList);
        mListView.setAdapter(mlTimeLineAdapter);
    }

    private void updateNoteList() {
        mNoteInfoList.clear();
        MLDBHelper mldbHelper = MLDBHelper.getInstance();
        if (mldbHelper != null) {
            String orderBy = MLDBConstants.COL_CREATE_AT + " desc";
            Cursor cursor = mldbHelper.queryData(MLDBConstants.TB_NOTE, null, null, null, null, null, orderBy, null);
            if (cursor.moveToFirst()) {
                do {
                    NoteInfo temp = new NoteInfo(cursor);
                    if (temp.getUserId().equals(mUserInfo.getUserId())) {
                        temp.setUserInfo(mUserInfo);
                    } else {
                        temp.setUserInfo(mSpouseInfo);
                    }
                    mNoteInfoList.add(temp);
                } while (cursor.moveToNext());
            }
        }
        mldbHelper.closeDatabase();

        mlTimeLineAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mlCallback = (MLFragmentCallback) activity;
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            ActivityOptionsCompat optionsCompat = null;
            switch (v.getId()) {
                case R.id.ml_img_user_avatar:
                    intent.setClass(mActivity, MLUserActivity.class);
                    optionsCompat = ActivityOptionsCompat.makeCustomAnimation(mActivity,
                            R.anim.ml_activity_right_in, R.anim.ml_activity_left_out);
                    ActivityCompat.startActivity(mActivity, intent, optionsCompat.toBundle());
                    mActivity.finish();
                    break;
                case R.id.ml_imgbtn_add_note:
                    intent.setClass(mActivity, MLNotePutActivity.class);
                    optionsCompat = ActivityOptionsCompat.makeCustomAnimation(mActivity,
                            R.anim.ml_fade_in, R.anim.ml_fade_out);
                    ActivityCompat.startActivity(mActivity, intent, optionsCompat.toBundle());
                    mActivity.finish();
                    break;
            }
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 0:
                    updateNoteList();
                    break;
            }
        }
    };
}
