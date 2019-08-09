package hello.leavesC.presenter.viewModel;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import hello.leavesC.presenter.model.ProfileModel;
import hello.leavesC.presenter.viewModel.base.BaseAndroidViewModel;
import hello.leavesC.sdk.Constants;
import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：叶应是叶
 * 时间：2018/9/29 22:36
 * 描述：
 */
public class RegisterViewModel extends BaseAndroidViewModel {

    private TLSAccountHelper accountHelper;

    private MutableLiveData<ProfileModel> regSuccessLiveData;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        accountHelper = TLSAccountHelper.getInstance().init(application,
                Constants.SDK_APP_ID, Constants.ACCOUNT_TYPE, Constants.APP_VERSION);
        regSuccessLiveData = new MutableLiveData<>();
    }

    public void register(String identifier, String password) {
        if (identifier.length() < 5) {
            showToast("用户名至少五位");
            return;
        }
        if (password.length() < 8) {
            showToast("密码至少八位");
            return;
        }
        int action = accountHelper.TLSStrAccReg(identifier, password, new TLSStrAccRegListener() {
            @Override
            public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {
                dismissLoadingDialog();
                showToast("注册成功");
                ProfileModel profileModel = new ProfileModel();
                profileModel.setIdentifier(tlsUserInfo.identifier);
                regSuccessLiveData.setValue(profileModel);
            }

            @Override
            public void OnStrAccRegFail(TLSErrInfo tlsErrInfo) {
                dismissLoadingDialog();
                showToast(tlsErrInfo.Msg);
            }

            @Override
            public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo) {
                dismissLoadingDialog();
                showToast(tlsErrInfo.Msg);
            }
        });
        if (action == TLSErrInfo.INPUT_INVALID) {
            showToast("格式有误，请重新输入");
        } else {
            showLoadingDialog("正在注册...");
        }
    }

    public MutableLiveData<ProfileModel> getRegSuccessLiveData() {
        return regSuccessLiveData;
    }

}
