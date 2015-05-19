package net.melove.app.ml.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.melove.app.ml.R;
import net.melove.app.ml.info.MessageInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/3/30.
 */
public class MLMessageAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<MessageInfo> mMessageList;


    public MLMessageAdapter(Context context, List<MessageInfo> messageInfoList) {
        mContext = context;
        mMessageList = messageInfoList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageInfo noteInfo = (MessageInfo) getItem(position);
        MLItemView mlItemView = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.ml_message_list_item, null);
            mlItemView = new MLItemView(convertView);
            convertView.setTag(mlItemView);
        } else {
            mlItemView = (MLItemView) convertView.getTag();
        }
        mlItemView.getTextView().setText(noteInfo.getContent());
        return convertView;
    }

    private static class MLItemView {
        private View baseView;
        private TextView textView;

        public MLItemView(View view) {
            baseView = view;
        }

        public TextView getTextView() {
            textView = (TextView) baseView.findViewById(R.id.ml_text_message_item_content);
            return textView;
        }

    }

}
