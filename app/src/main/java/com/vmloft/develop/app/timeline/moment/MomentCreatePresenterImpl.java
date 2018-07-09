package com.vmloft.develop.app.timeline.moment;

import com.vmloft.develop.app.timeline.common.CResult;
import com.vmloft.develop.app.timeline.common.Callback;
import com.vmloft.develop.app.timeline.common.bean.Moment;
import com.vmloft.develop.app.timeline.moment.MomentCreateContract.IMomentCreateModel;
import com.vmloft.develop.app.timeline.moment.MomentCreateContract.IMomentCreateView;
import com.vmloft.develop.app.timeline.moment.MomentCreateContract.IMomentCreatePresenter;

public class MomentCreatePresenterImpl extends IMomentCreatePresenter<IMomentCreateView> {

    private IMomentCreateModel createModel;

    public MomentCreatePresenterImpl() {
        createModel = new MomentCreateModelImpl();
    }

    @Override
    public void createMoment(Moment moment) {
        createModel.createMoment(moment, new Callback() {
            @Override
            public void onDone(Object object) {
                obtainView().onCreateMomentResult(new CResult());
            }

            @Override
            public void onError(int code, String desc) {
                obtainView().onCreateMomentResult(new CResult(code, desc));
            }
        });
    }
}
