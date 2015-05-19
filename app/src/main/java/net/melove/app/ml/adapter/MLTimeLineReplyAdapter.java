package net.melove.app.ml.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.melove.app.ml.R;
import net.melove.app.ml.info.ReplyInfo;
import net.melove.app.ml.views.MLImageView;

import java.util.List;

/**
 * Created by Administrator on 2015/4/27.
 */
public class MLTimeLineReplyAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<ReplyInfo> mReplyInfoList;

    public MLTimeLineReplyAdapter(Context context, List<ReplyInfo> replyInfoList) {
        mContext = context;
        mReplyInfoList = replyInfoList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mReplyInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mReplyInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReplyInfo replyInfo = (ReplyInfo) getItem(position);

        MLItem mlItem = null;
        if (convertView == null) {
            View v = inflater.inflate(R.layout.ml_timeline_list_item_reply_list_item, null);
            mlItem = new MLItem(convertView);
            convertView.setTag(mlItem);
        }else{
            mlItem = (MLItem) convertView.getTag();
        }

//        mlItem.getAvatarView().setImageDrawable();
        mlItem.getAuthorTextView().setText(replyInfo.getUserId());
        mlItem.getContentTextView().setText(replyInfo.getContent());
        mlItem.getTimeTextView().setText(replyInfo.getCreateAt());
        return convertView;
    }

    private class MLItem {
        private View baseView;
        private MLImageView avatarView;
        private TextView authorTextView;
        private TextView contentTextView;
        private TextView timeTextView;


        public MLItem(View view) {
            baseView = view;
        }

        public MLImageView getAvatarView() {
            avatarView = (MLImageView) baseView.findViewById(R.id.ml_img_timeline_reply_avatar);
            return avatarView;
        }

        public TextView getAuthorTextView() {
            authorTextView = (TextView) baseView.findViewById(R.id.ml_text_timeline_reply_author);
            return authorTextView;
        }

        public TextView getContentTextView() {
            contentTextView = (TextView) baseView.findViewById(R.id.ml_text_timeline_reply_content);
            return contentTextView;
        }

        public TextView getTimeTextView() {
            timeTextView = (TextView) baseView.findViewById(R.id.ml_text_timeline_reply_time);
            return timeTextView;
        }
    }
}
