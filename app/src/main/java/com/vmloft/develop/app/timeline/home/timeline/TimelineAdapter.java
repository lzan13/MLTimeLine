package com.vmloft.develop.app.timeline.home.timeline;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.app.timeline.common.bean.Moment;
import com.vmloft.develop.library.tools.adapter.VMAdapter;
import com.vmloft.develop.library.tools.adapter.VMHolder;
import java.util.List;

public class TimelineAdapter extends VMAdapter<Moment, TimelineAdapter.TimelineHolder> {

    public TimelineAdapter(Context context, List<Moment> list) {
        super(context, list);
    }

    @Override
    public TimelineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_timeline, parent, false);
        return new TimelineHolder(view);
    }

    @Override
    public void onBindViewHolder(TimelineHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    class TimelineHolder extends VMHolder {

        @BindView(R.id.img_avatar) ImageView avatarView;
        @BindView(R.id.text_nickname) TextView nicknameView;
        @BindView(R.id.text_time) TextView timeView;

        public TimelineHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
