package zhonghuass.ssml.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import zhonghuass.ssml.R;
import zhonghuass.ssml.di.component.DaggerRegisterComponent;
import zhonghuass.ssml.di.module.RegisterModule;
import zhonghuass.ssml.mvp.contract.RegisterContract;
import zhonghuass.ssml.mvp.presenter.RegisterPresenter;
import zhonghuass.ssml.mvp.ui.MBaseActivity;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RegisterActivity extends MBaseActivity<RegisterPresenter> implements RegisterContract.View {

    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_passworld)
    EditText edtPassworld;
    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.tv_getcode)
    TextView tvGetcode;
    @BindView(R.id.tv_upload)
    TextView tvUpload;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.iv_see)
    ImageView ivSee;
    @BindView(R.id.iv_tip_choose)
    ImageView ivTipChoose;
    private Disposable mDispos;
    private String mPhone;
    private String mPass;
    private boolean isChecked;
    private String mCode;
    private boolean isflag;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRegisterComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .registerModule(new RegisterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_register; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        llTop.setVisibility(View.GONE);
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
        ArmsUtils.makeText(this, message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @OnClick({R.id.tv_getcode, R.id.tv_upload, R.id.tv_agreement, R.id.iv_see,R.id.iv_tip_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode:
                getRegisteCode();
                break;
            case R.id.tv_upload:
                toLogin();
                break;
            case R.id.tv_agreement:

                break;
            case R.id.iv_see:
                toChoose();
                break;
            case R.id.iv_tip_choose:
                toAgreement();
                break;
        }
    }

    private void toAgreement() {
        if (isflag) {
            ivTipChoose.setBackgroundResource(R.mipmap.login_icon_5);
            isflag = false;
        } else {
            ivTipChoose.setBackgroundResource(R.mipmap.login_icon_5_1);
            isflag = true;
        }
    }

    private void toChoose() {
        if (isChecked) {
            //如果选中，显示密码
            edtPassworld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivSee.setBackgroundResource(R.mipmap.login_icon_8);
            isChecked = false;
        } else {
            //否则隐藏密码
            edtPassworld.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivSee.setBackgroundResource(R.mipmap.login_icon_4);
            isChecked = true;
        }
    }

    private void getRegisteCode() {
        String mPhone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mPhone)) {
            ArmsUtils.makeText(this, "请核对手机号码!");
            return;
        }
        tvGetcode.setEnabled(false);
        mPresenter.getCode(mPhone);
        mDispos = Flowable.interval(1, 1, TimeUnit.SECONDS)
                .take(60)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext((aLong) -> {
                    tvGetcode.setText("重新获取(" + (60 - aLong) + ")");
                })
                .doOnComplete(() -> {
                    tvGetcode.setEnabled(true);
                    tvGetcode.setText("获取验证码");
                })
                .doOnError((throwable) ->
                        throwable.printStackTrace()
                )
                .subscribe();

    }

    private void toLogin() {
        mPhone = edtPhone.getText().toString().trim();
        mPass = edtPassworld.getText().toString().trim();
        mCode = edtCode.getText().toString().trim();
        if (TextUtils.isEmpty(mPhone)) {
            ArmsUtils.makeText(this, "请输入手机号码!");
            return;
        }
        if (TextUtils.isEmpty(mPass)) {
            ArmsUtils.makeText(this, "请核输入密码");
            return;
        }
        if (TextUtils.isEmpty(mCode)) {
            ArmsUtils.makeText(this, "请核手机号验证码");
            return;
        }
        mPresenter.toRegist(mPhone, mPass, mCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDispos != null) {
            mDispos.dispose();
        }
    }

    @Override
    public void toNewActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.putExtra("mPhone", mPhone);
        intent.putExtra("mPass", mPass);
        launchActivity(intent);
        RegisterActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
