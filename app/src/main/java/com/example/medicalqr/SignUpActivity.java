package com.example.medicalqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    // Firebase Auth 인스턴스 선언
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Firebase Auth 초기화
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginButton).setOnClickListener(onClickListener);
    }

    // 뒤로 가기 눌렀을 시 앱 종료
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    // activity_sign_up.xml에서 버튼 클릭했을시 반응
    View.OnClickListener onClickListener = (v) ->  {
            switch (v.getId()){
                case R.id.signUpButton:
                    // signUp()은 신규 사용자 가입 함수
                    signUp();
                    break;
                case R.id.gotoLoginButton:
                    myStartActivity(LoginActivity.class);
                    break;
            }
    };

    // 신규 사용자 가입
    private void signUp(){
        // getText()를 사용하기 위해 EditText로 형변환 (일반 뷰는 getText() 사용 불가)
        String email=((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password=((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck=((EditText)findViewById(R.id.passwordCheckEditText)).getText().toString();

        if(email.length()>0 && password.length()>0 && passwordCheck.length()>0){
            // passwordCheck 일치 확인
            if(password.equals(passwordCheck)){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, (task) ->  {
                                // 회원가입이 성공했을 때
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원가입이 성공했습니다.");
                                    myStartActivity(MainActivity.class);
                                // 회원가입이 실패했을 때
                                } else {
                                    if(task.getException() != null){
                                        startToast(task.getException().toString());
                                    }
                                }
                        });
            }else{
                startToast("비밀번호가 일치하지 않습니다.");
            }
        }else{
            startToast("이메일 또는 비밀번호를 입력해 주세요.");
        }
    }
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    // 로그인 화면으로 가기 위한 함수
    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
