package zhonghuass.ssml.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import zhonghuass.ssml.di.component.DaggerDanymicComponent;
import zhonghuass.ssml.di.module.DanymicModule;
import zhonghuass.ssml.mvp.contract.DanymicContract;
import zhonghuass.ssml.mvp.presenter.DanymicPresenter;

import zhonghuass.ssml.R;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DanymicFragment extends BaseFragment<DanymicPresenter> implements DanymicContract.View {

    public static DanymicFragment newInstance() {
        DanymicFragment fragment = new DanymicFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerDanymicComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .danymicModule(new DanymicModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_danymic, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }


    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }
}
