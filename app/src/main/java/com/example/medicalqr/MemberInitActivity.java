package com.example.medicalqr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberInitActivity extends AppCompatActivity {
    private static final String TAG = "MemberInitActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.checkButton:
                    profileUpdate();
                    break;
            }
        }
    };

    // 프로필 정보 업데이트
    private void profileUpdate(){
        String name=((EditText)findViewById(R.id.nameEditText)).getText().toString();
        String phoneNumber=((EditText)findViewById(R.id.phoneNumberEditText)).getText().toString();
        String birthDay=((EditText)findViewById(R.id.birthDayEditText)).getText().toString();
        String address=((EditText)findViewById(R.id.addressEditText)).getText().toString();
        String height=((EditText)findViewById(R.id.heightEditText)).getText().toString();
        String weight=((EditText)findViewById(R.id.weightEditText)).getText().toString();
        String fever=((EditText)findViewById(R.id.feverEditText)).getText().toString();
        String medicine=((EditText)findViewById(R.id.medicineEditText)).getText().toString();

        if(name.length()>0 && phoneNumber.length()>0 && birthDay.length()>0 && address.length()>0 && height.length()>0 && weight.length()>0 && fever.length()>0 && medicine.length()>0){
            // 회원가입 시 생성된 uid 추적
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // db 초기화
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // MemberInfo 클래스로부터 인스턴스 생성
            MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthDay, address, height, weight, fever, medicine);

            if(user != null){
                db.collection("users").document(user.getUid()).set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원 정보 등록을 성공하였습니다.");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원 정보 등록을 실패하였습니다.");
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }

        }else{
            startToast("회원 정보를 입력해 주세요.");
        }
    }
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

}
