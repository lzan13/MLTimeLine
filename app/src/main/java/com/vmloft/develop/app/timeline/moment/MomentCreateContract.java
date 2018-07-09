package com.vmloft.develop.app.timeline.moment;

import com.vmloft.develop.app.timeline.common.CResult;
import com.vmloft.develop.app.timeline.common.Callback;
import com.vmloft.develop.app.timeline.common.base.BPresenter;
import com.vmloft.develop.app.timeline.common.bean.Moment;

public class MomentCreateContract {

    public interface IMomentCreateModel {

        void createMoment(Moment moment, Callback callback);
    }

    public interface IMomentCreateView {

        void onCreateMomentResult(CResult result);
    }

    public static abstract class IMomentCreatePresenter<V> extends BPresenter<V> {

        public abstract void createMoment(Moment moment);
    }
}

