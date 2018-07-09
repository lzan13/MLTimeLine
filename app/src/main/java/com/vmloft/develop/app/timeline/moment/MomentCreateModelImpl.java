package com.vmloft.develop.app.timeline.moment;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.vmloft.develop.app.timeline.common.Callback;
import com.vmloft.develop.app.timeline.common.bean.Moment;

public class MomentCreateModelImpl implements MomentCreateContract.IMomentCreateModel {

    @Override
    public void createMoment(Moment moment, final Callback callback) {
        moment.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    callback.onDone(null);
                } else {
                    callback.onError(e.getCode(), e.getMessage());
                }
            }
        });
    }
}
