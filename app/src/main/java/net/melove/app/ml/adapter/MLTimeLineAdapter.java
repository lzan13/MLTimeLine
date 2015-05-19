package net.melove.app.ml.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.melove.app.ml.R;
import net.melove.app.ml.info.NoteInfo;
import net.melove.app.ml.views.MLImageView;

import java.util.List;

/**
 * Created by Administrator on 2014/12/18.
 */
public class MLTimeLineAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<NoteInfo> mNoteInfoList;


    public MLTimeLineAdapter(Context context, List<NoteInfo> noteInfoList) {
        mContext = context;
        mNoteInfoList = noteInfoList;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mNoteInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNoteInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteInfo noteInfo = (NoteInfo) getItem(position);
        MLItem mlItem = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.ml_timeline_list_item, null);
            mlItem = new MLItem(convertView);
            convertView.setTag(mlItem);
        }else{
            mlItem = (MLItem) convertView.getTag();
        }
//        mlItem.
        mlItem.getAvatarView().setTag(noteInfo.getUserId());
        mlItem.getContentTextView().setText(noteInfo.getContent());
//        mlItem.getReplyListView().setAdapter();

        return convertView;
    }

    private static class MLItem {
        private View baseView;
        private MLImageView avatarView;
        private TextView contentTextView;
        private MLImageView imageView;
        private ListView replyListView;


        public MLItem(View view) {
            baseView = view;
        }

        public MLImageView getAvatarView() {
            avatarView = (MLImageView) baseView.findViewById(R.id.ml_img_timeline_avatar);
            return avatarView;
        }

        public TextView getContentTextView() {
            contentTextView = (TextView) baseView.findViewById(R.id.ml_text_timeline_content);
            return contentTextView;
        }

        public MLImageView getImageView() {
            imageView = (MLImageView) baseView.findViewById(R.id.ml_img_timeline_image);
            return imageView;
        }
        public ListView getReplyListView() {
            replyListView = (ListView) baseView.findViewById(R.id.ml_listview_timeline_reply);
            return replyListView;
        }
    }


}
