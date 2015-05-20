package net.melove.app.ml.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import net.melove.app.ml.MLApp;
import net.melove.app.ml.R;
import net.melove.app.ml.http.MLHttpConstants;
import net.melove.app.ml.info.NoteInfo;
import net.melove.app.ml.utils.MLDate;
import net.melove.app.ml.utils.MLFile;
import net.melove.app.ml.utils.MLScreen;
import net.melove.app.ml.views.MLImageView;

import java.util.List;

/**
 * Created by Administrator on 2014/12/18.
 */
public class MLTimeLineAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<NoteInfo> mNoteInfoList;

    private DisplayImageOptions options;

    public MLTimeLineAdapter(Context context, List<NoteInfo> noteInfoList) {
        mContext = context;
        mNoteInfoList = noteInfoList;
        mInflater = LayoutInflater.from(mContext);
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .imageScaleType(ImageScaleType.EXACTLY)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.mipmap.bg_transparent_gray)
                .showImageOnLoading(R.mipmap.bg_transparent_gray)
                .showImageOnFail(R.mipmap.bg_transparent_gray)
                .build();
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
        } else {
            mlItem = (MLItem) convertView.getTag();
        }

        Bitmap avatar = MLFile.fileToBitmap(MLApp.getUserImage() + noteInfo.getUserInfo().getAvatar());
        if (avatar != null) {
            mlItem.avatarView.setImageBitmap(avatar);
        }
        mlItem.nameTextView.setText(noteInfo.getUserInfo().getNickname());
        mlItem.timeTextView.setText(noteInfo.getCreateAt());
        mlItem.contentTextView.setText(noteInfo.getContent());

        if (noteInfo.getNoteType().equals("image")) {
            int imageWidth = MLScreen.getImageSize(noteInfo.getImage()).x;
            int imageHeight = MLScreen.getImageSize(noteInfo.getImage()).y;

            int width = MLScreen.getScreenSize().x - MLScreen.dp2px(R.dimen.ml_dimen_16);
            int height = width * imageHeight / imageWidth;

            mlItem.imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));

            mlItem.imageView.setVisibility(View.VISIBLE);
            String url = MLHttpConstants.UPLOAD_URL + MLHttpConstants.IMAGE_URL + noteInfo.getImage();
            ImageLoader.getInstance().displayImage(url, mlItem.imageView, options);
        } else {
            mlItem.imageView.setVisibility(View.GONE);
        }
//        mlItem.getReplyListView().setAdapter();
        return convertView;
    }


    private static class MLItem {
        private View baseView;
        private MLImageView avatarView;
        private TextView nameTextView;
        private TextView timeTextView;
        private TextView contentTextView;
        private ImageView imageView;
        private ListView replyListView;


        public MLItem(View view) {
            baseView = view;
            avatarView = (MLImageView) baseView.findViewById(R.id.ml_img_note_user_avatar);
            nameTextView = (TextView) baseView.findViewById(R.id.ml_text_note_user_nickname);
            timeTextView = (TextView) baseView.findViewById(R.id.ml_text_note_time);
            contentTextView = (TextView) baseView.findViewById(R.id.ml_text_note_content);
            imageView = (ImageView) baseView.findViewById(R.id.ml_img_note_image);
            replyListView = (ListView) baseView.findViewById(R.id.ml_listview_note_reply);
        }
    }
}
