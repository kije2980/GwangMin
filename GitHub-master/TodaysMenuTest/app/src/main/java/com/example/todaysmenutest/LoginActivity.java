package com.example.todaysmenutest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.api.UserApi;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.AgeRange;
import com.kakao.usermgmt.response.model.Gender;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.User;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity {

    private SessionCallback callback;      //콜백 선언
    private Button btn_guest;
    private TextView btn_managerMode;
    int count = 0;
    final static String TAG = "LoginActivityT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //로그인 없이 시작하기
        btn_guest = (Button)findViewById(R.id.btn_guest);
        btn_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

        //관리자모드
        btn_managerMode = (TextView) findViewById(R.id.btn_manage_mode);
        btn_managerMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count +=1;
                if(count == 5){
                    Intent intent = new Intent(LoginActivity.this,ManagermodeActivity.class);
                    startActivity(intent);
                }
            }
        });

        //카카오 로그인 콜백받기
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        //토큰 만료시 갱신 시켜줌
        //if(Session.getCurrentSession().isOpened())
        //    Session.getCurrentSession().checkAndImplicitOpen();

        Log.e(TAG, "토큰 : " + Session.getCurrentSession().getTokenInfo().getAccessToken());
        Log.e(TAG, "리프레쉬 토큰 : "+Session.getCurrentSession().getTokenInfo().getRefreshToken());
        Log.e(TAG, "토큰 만료기간 : " + Session.getCurrentSession().getTokenInfo().getRemainingExpireTime());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        //로그인 성공한 상태
        @Override
        public void onSessionOpened() {

            requestMe();

        }

        //로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }


    protected void requestMe() { //유저의 정보를 받아오는 함수
        List<String> keys = new ArrayList<>();
        keys.add("properties.nickname"); //서비스별 닉네임
        keys.add("properties.profile_image"); //서비스볼 프로필 이미지 URL
        keys.add("kakao_account.profile"); //사용자 카카오계정의 프로필 소유여부, 닉네임과 프로필이미지 값
        keys.add("kakao_account.age_range"); //사용자 카카오계정의 연령대 소유여부, 연령대 값
        keys.add("kakao_account.gender"); //사용자 카카오계정의 성별 소유여부, 성별 값

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {

            //사용자정보 요청에 실패한 경우
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "faild to get user info.msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onFailureForUiThread(ErrorResult errorResult) {
                super.onFailureForUiThread(errorResult);
                String message = "faild to get user info.msg=" + errorResult;
                Logger.d(message);
            }

            //세션 오픈 실패, 세션이 삭제된 경우
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            //사용자정보 요청에 성공한 경우
            @Override
            public void onSuccess(MeV2Response response) { //로그인 성공시

                //handleScopeError(response.getKakaoAccount()); //동적동의
                long token = response.getId();
                String profileImageUrl = response.getKakaoAccount().getProfile().getProfileImageUrl();
                String nickName = response.getKakaoAccount().getProfile().getNickname();
 //               String age_range = response.getKakaoAccount().getAgeRange().getValue();
 //               String gender = response.getKakaoAccount().getGender().getValue();

                Logger.d("userToken : " + response.getId());
                Logger.d("profileImageUrl : " + response.getKakaoAccount().getProfile().getProfileImageUrl());
                Logger.d("nickname : " + response.getKakaoAccount().getProfile().getNickname());
  //              Logger.d("age_range : " + response.getKakaoAccount().getAgeRange().getValue());
  //              Logger.d("gender : " + response.getKakaoAccount().getGender().getValue());



                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("profileImageUrl",profileImageUrl);
                intent.putExtra("nickName",nickName);
 //               intent.putExtra("ageRange",age_range);
 //               intent.putExtra("gender",gender);
 //               intent.putExtra("token",token);
                startActivity(intent);

                //redirectHomeActivity();

            }
        });
    }

    //사용자 정보 동적동의
    private void handleScopeError(UserAccount account){
        List<String> neededScops = new ArrayList<>();

        //연령대
        if(account.ageRangeNeedsAgreement().getBoolean()){
            neededScops.add("age_range");
        }//성별
        if(account.genderNeedsAgreement().getBoolean()){
            neededScops.add("gender");
        }
        Session.getCurrentSession().updateScopes(this, neededScops, new AccessTokenCallback() {
            @Override
            public void onAccessTokenReceived(AccessToken accessToken) {
                Toast.makeText(LoginActivity.this,"동적동의 얻음",Toast.LENGTH_SHORT).show();
                //유저에게 성공적으로 동의를 받음. 토큰을 재발급 받게 됨.
            }

            @Override
            public void onAccessTokenFailure(ErrorResult errorResult) {
                Toast.makeText(LoginActivity.this,"동적동의 실패",Toast.LENGTH_SHORT).show();
                //동의 얻기 실패
            }
        });
    }

    private void requestAccessTokenInfo(){
        AuthService.getInstance().requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e("failed to get access token info. msg=" + errorResult);
            }

            @Override
            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                long userId = accessTokenInfoResponse.getUserId();
                Logger.d("this access token is for userId=" + userId);

                long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
                Logger.d("this access token expire after" + expiresInMilis + "millisecons.");

            }
        });
    }




    private void redirectHomeActivity() { //메인으로
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    protected void redirectLoginActivity() { //로그인창으로 intent 후 앱종료
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public void onClickLogout(){ //로그아웃시
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //redirectLoginActivity();
            }
        });
    }


}

