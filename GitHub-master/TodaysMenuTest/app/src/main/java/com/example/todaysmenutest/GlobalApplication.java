package com.example.todaysmenutest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

public class GlobalApplication extends Application {
    private static volatile GlobalApplication instance = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter()); //카카오 SDK 초기화
    }

    private static class KakaoSDKAdapter extends KakaoAdapter {

        //로그인시 사용 될, Session의 옵션설정을 위한 인터페이스
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {

                //로그인시 인증타입 지정
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                }

                //로그인 웹뷰에서 pause와 resume시에 타이머를 설정하여 CPU의 소모를 절약할지의 여부
                //true로 지정할 경우, 로그인 웹뷰의 onPuase()와 onResume()에 타이머를 설정해야함
                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                //로그인시 토큰을 저장할 때의 암호화 여부 지정
                @Override
                public boolean isSecureMode() {
                    return false;
                }

                //일반 사용자가 아닌 Kakao와 제휴된 앱에서 사용되는 값
                //값을 지정하지 않을 경우, ApprovalType.INDIVIDUAL 값으로 사용됨
                @Nullable
                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                //로그인 웹뷰에서 email 입력 폼의 데이터를 저장할지 여부 지정
                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        //Application이 가지고 있는 정보를 얻기위한 인터페이스
        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getGlobalApplicationContext();
                }
            };
        }
    }

    public static GlobalApplication getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    public static Activity getCurrentActivity(){
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity){
        GlobalApplication.currentActivity = currentActivity;
    }

}




