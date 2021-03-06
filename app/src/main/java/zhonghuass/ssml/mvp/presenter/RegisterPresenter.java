package zhonghuass.ssml.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import zhonghuass.ssml.http.BaseResponse;
import zhonghuass.ssml.mvp.contract.RegisterContract;
import zhonghuass.ssml.utils.RxUtils;


@ActivityScope
public class RegisterPresenter extends BasePresenter<RegisterContract.Model, RegisterContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private RxErrorHandler rxErrorHandler;

    @Inject
    public RegisterPresenter(RegisterContract.Model model, RegisterContract.View rootView, RxErrorHandler rxErrorHandler) {
        super(model, rootView);
        this.rxErrorHandler = rxErrorHandler;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void toRegist(String mPhone, String mPass, String mCode) {
        mModel.toRegist(mPhone, mPass, mCode)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse<Void>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse<Void> voidBaseResponse) {
                        mRootView.showMessage(voidBaseResponse.getMessage());
                        if(voidBaseResponse.isSuccess()){
                            mRootView.toNewActivity();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });


    }

    public void getCode(String mPhone) {
        mModel.toGetCode(mPhone)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse<Void>>(rxErrorHandler) {
                    @Override
                    public void onNext(BaseResponse<Void> voidBaseResponse) {
                        mRootView.showMessage(voidBaseResponse.getMessage());
                    }
                });
    }
}
