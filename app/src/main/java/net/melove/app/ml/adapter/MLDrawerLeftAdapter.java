package net.melove.app.ml.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.melove.app.ml.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/30.
 */
public class MLDrawerLeftAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater inflater;
    private List<DrawableItemData> itemDataList;

    public MLDrawerLeftAdapter(Context context) {
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);

        String[] itemTitles = mContext.getResources().getStringArray(R.array.ml_menu_title);
        int[] itemIconIds = {
                R.mipmap.icon_settings_gray_24dp,
                R.mipmap.icon_settings_gray_24dp,
                R.mipmap.icon_settings_gray_24dp
        };
        itemDataList = new ArrayList<DrawableItemData>();
        for (int i = 0; i < itemTitles.length; i++) {
            itemDataList.add(new DrawableItemData(mContext.getResources().getDrawable(itemIconIds[i]), itemTitles[i]));
        }
    }

    @Override
    public int getCount() {
        return itemDataList.size();
    }

    @Override
    public DrawableItemData getItem(int position) {
        return itemDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawableItemData itemData = getItem(position);
        MLItem mlItem = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ml_drawer_list_item, null);
            mlItem = new MLItem(convertView);
            convertView.setTag(mlItem);
        }else{
            mlItem = (MLItem) convertView.getTag();
        }
        mlItem.getmItemIcon().setImageDrawable(itemData.getIcon());
        mlItem.getmItemTitle().setText(itemData.getTitle());
        return convertView;
    }

    private class MLItem{
        private View mBaseView;

        private ImageView mItemIcon;
        private TextView mItemTitle;
        public MLItem(View view){
            mBaseView = view;
        }
        public ImageView getmItemIcon() {
            mItemIcon = (ImageView) mBaseView.findViewById(R.id.ml_img_menu_icon);
            return mItemIcon;
        }
        public TextView getmItemTitle() {
            mItemTitle = (TextView) mBaseView.findViewById(R.id.ml_text_menu_title);
            return mItemTitle;
        }
    }

    public class DrawableItemData {
        private Drawable icon;
        private String title;

        public DrawableItemData(Drawable icon, String title) {
            setIcon(icon);
            setTitle(title);
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Drawable getIcon() {
            return icon;
        }

        public String getTitle() {
            return title;
        }
    }

}
