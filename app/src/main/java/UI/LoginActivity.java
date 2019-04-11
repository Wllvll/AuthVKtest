package UI;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.testapp.oauthvk.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;

import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import static com.vk.sdk.VKSdk.authorize;

public class LoginActivity extends AppCompatActivity {

    Button auth;
    private static final String[] scope = {VKScope.FRIENDS};
    private ShimmerFrameLayout mShimmerViewContainer;
    public static final String APP_ID = "6936956";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //auth = findViewById(R.id.auth);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        //ShimmerFrameLayout container = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        //container.startShimmer();

        VKUIHelper.onCreate(this);
        // Инициализация SDK - передаем слушатель и id standalone-приложения
        VKSdk.initialize(listener, APP_ID);
        // При наличии токена авторизации запускаем UserActivity
        if (VKSdk.wakeUpSession()) {
            startUserActivity();
        }
        //Делаем обработчик нажатия для кнопки "Авторизоваться"
        View.OnClickListener btn_auth = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorize();
            }
        };
        //Присваиваем обработчик кнопке
        mShimmerViewContainer.setOnClickListener(btn_auth);
    }

    //Создаем слушателя
    VKSdkListener listener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            super.onReceiveNewToken(newToken);
            // Получение нового токена после авторицации и запуск активности
            startUserActivity();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            super.onAcceptUserToken(token);
            // Точно не понял, но вроде что о хорошее - запускаем активность
            startUserActivity();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            // Токен просрочен, запуск авторизации
            authorize();
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }
    };

    private void authorize(){
        // Запускаем авторизацию со списком разрешений. Результат - запуск VKOpenAuthActivity
        VKSdk.authorize(scope,true,false);
        //authorize делает startActivityForResult
    }

    //При наличии токена авторизации запускаем активити профиля
    private void startUserActivity(){
        startActivity(new Intent(this, UserActivity.class));
        finish();
    }

    //Получаем ответ с экрана авторизации
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Если получен ответ от VKOpenAuthActivity - вызывается один из методов слушателя listener
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this,requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        VKUIHelper.onResume(this);
    }
}
