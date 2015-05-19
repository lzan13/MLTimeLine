package net.melove.app.ml.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import net.melove.app.ml.R;
import net.melove.app.ml.activity.MLSignActivity;
import net.melove.app.ml.adapter.MLMessageAdapter;
import net.melove.app.ml.db.MLDBConstants;
import net.melove.app.ml.db.MLDBHelper;
import net.melove.app.ml.http.MLHttpConstants;
import net.melove.app.ml.http.MLHttpUtil;
import net.melove.app.ml.http.MLRequestParams;
import net.melove.app.ml.http.MLStringResponseListener;
import net.melove.app.ml.info.MessageInfo;
import net.melove.app.ml.info.UserInfo;
import net.melove.app.ml.utils.MLSPUtil;
import net.melove.app.ml.utils.MLScreen;
import net.melove.app.ml.views.MLToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/26.
 */
public class MLMessageFragment extends MLBaseFragment {

    private Activity mActivity;
    private MLFragmentCallback mlCallback;

    private UserInfo mUserInfo;
    private UserInfo mSpouseInfo;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private View mHeaderView;
    private View mFooterView;
    private List<MessageInfo> mMessageInfoList;
    private MLMessageAdapter mlMessageAdapter;

    private EditText mMessageEdit;
    private Button mSendBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ml_message_fragment_layout, container, false);

        mActivity = getActivity();
        initInfo();
        initFragment(view);
        initSwipeRefreshLayout(view);
        initListView(view);
        return view;
    }

    private void initFragment(View view) {
        mListView = (ListView) view.findViewById(R.id.ml_listview_message);
        mHeaderView = mActivity.getLayoutInflater().inflate(R.layout.ml_message_list_header, null);
        mFooterView = mActivity.getLayoutInflater().inflate(R.layout.ml_message_list_footer, null);

        mListView.addHeaderView(mHeaderView);
        mListView.addFooterView(mFooterView);
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


    /**
     * 初始化下拉刷新组件
     *
     * @param view
     */
    private void initSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ml_swipe_refresh_layout);

        mSwipeRefreshLayout.setProgressViewOffset(false, 0, MLScreen.dp2px(R.dimen.ml_dimen_96));
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.ml_blue,
                R.color.ml_orange,
                R.color.ml_green,
                R.color.ml_red);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        MLRequestParams params = new MLRequestParams();
                        params.putParams(MLDBConstants.COL_ACCESS_TOKEN, mUserInfo.getAccessToken());
                        MLHttpUtil.getInstance(mActivity).post(MLHttpConstants.API_URL + MLHttpConstants.API_MESSAGE, params,
                                new MLStringResponseListener() {
                                    @Override
                                    public void onFailure(int state, String content) {
                                        MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                                                mActivity.getResources().getString(R.string.ml_error_http)).show();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }

                                    @Override
                                    public void onSuccess(int state, String content) {
                                        parseMessageInfo(content);
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                    }
                });
    }

    /**
     * 解析请求Message返回的数据
     *
     * @param content
     */
    private void parseMessageInfo(String content) {
        Resources res = mActivity.getResources();
        String toastStr = "";
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (jsonObject.isNull("error")) {
                JSONArray jsonArray = jsonObject.getJSONArray("messages");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject message = jsonArray.getJSONObject(i);
                    MessageInfo messageInfo = new MessageInfo(message);

                    ContentValues values = new ContentValues();
                    values.put(MLDBConstants.COL_LOVE_ID, messageInfo.getSendUserId());
                    values.put(MLDBConstants.COL_USER_ID, messageInfo.getReceiveUserId());
                    values.put(MLDBConstants.COL_NOTE_ID, messageInfo.getMessageId());
                    values.put(MLDBConstants.COL_NOTE_TYPE, messageInfo.getMessageType());
                    values.put(MLDBConstants.COL_CONTENT, messageInfo.getContent());
                    values.put(MLDBConstants.COL_STATE, messageInfo.getState());
                    values.put(MLDBConstants.COL_CREATE_AT, messageInfo.getCreateAt());

                    MLDBHelper mldbHelper = MLDBHelper.getInstance();
                    mldbHelper.insterData(MLDBConstants.TB_MESSAGE, values);
                    mldbHelper.closeDatabase();

                    toastStr = res.getString(R.string.ml_message_newest_success);
                    MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, toastStr).show();
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.sendToTarget();
                }

            } else {
                String error = jsonObject.getString("error");
                if (error.equals("null")) {
                    toastStr = res.getString(R.string.ml_message_newest_null);
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
     * 初始化消息列表
     *
     * @param view
     */
    private void initListView(View view) {
        mMessageInfoList = new ArrayList<MessageInfo>();

        MLDBHelper mldbHelper = MLDBHelper.getInstance();
        if (mldbHelper != null) {
            Cursor cursor = mldbHelper.queryData(MLDBConstants.TB_MESSAGE, null, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    MessageInfo temp = new MessageInfo();
                    temp.setSendUserId(cursor.getString(0));
                    temp.setReceiveUserId(cursor.getString(1));
                    temp.setMessageId(cursor.getString(2));
                    temp.setMessageType(cursor.getString(3));
                    temp.setContent(cursor.getString(4));
                    temp.setState(cursor.getInt(5));
                    temp.setCreateAt(cursor.getString(6));
                    mMessageInfoList.add(temp);
                } while (cursor.moveToNext());
            }
        }

        mldbHelper.closeDatabase();

        if (mMessageInfoList.size() == 0) {
            ViewStub viewStub = (ViewStub) mFooterView.findViewById(R.id.ml_empty_viewstub);
            viewStub.inflate();
        }
        mlMessageAdapter = new MLMessageAdapter(mActivity, mMessageInfoList);
        mListView.setAdapter(mlMessageAdapter);
    }

    private void updateListView() {
        mMessageInfoList.clear();
        MLDBHelper mldbHelper = MLDBHelper.getInstance();
        if (mldbHelper != null) {
            Cursor cursor = mldbHelper.queryData(MLDBConstants.TB_MESSAGE, null, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    MessageInfo temp = new MessageInfo();
                    temp.setSendUserId(cursor.getString(0));
                    temp.setReceiveUserId(cursor.getString(1));
                    temp.setMessageId(cursor.getString(2));
                    temp.setMessageType(cursor.getString(3));
                    temp.setContent(cursor.getString(4));
                    temp.setState(cursor.getInt(5));
                    temp.setCreateAt(cursor.getString(6));
                    mMessageInfoList.add(temp);
                } while (cursor.moveToNext());
            }
        }

        mldbHelper.closeDatabase();
        mlMessageAdapter.notifyDataSetChanged();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 0:
                    updateListView();
                    break;

            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mlCallback = (MLFragmentCallback) activity;
    }
}
