package UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import Adapters.GeneralFriendsFields;
import com.testapp.oauthvk.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;

import Adapters.FriendsAdapter;
import Requests.DownloadAvatar;
import Requests.FriendsRequest;

public class UserActivity extends AppCompatActivity {

    FriendsAdapter mFriendsAdapter;
    private int mQuantityOfAllUser = 0;
    private int mOffset = 5;
    private int mStep = 5;
    private int Capacity = 5;
    private SparseArray<VKApiUserFull> mFriendsArray = new SparseArray<>(Capacity);
    ListView friend_list;
    TextView profil_name;
    ImageView avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        friend_list = findViewById(R.id.friend_list);
        profil_name = findViewById(R.id.profil_name);
        avatar = findViewById(R.id.avatar);
        final FriendsRequest friendsRequest = new FriendsRequest(GeneralFriendsFields.FIRSTNAME_LASTNAME_ICON_ONLINE, mOffset, mStep);
        friendsRequest.executeWithListener(new VKFriendsRequestListener());
        mFriendsAdapter = new FriendsAdapter(this, mFriendsArray, R.layout.friend_list_item);
        friend_list.setAdapter(mFriendsAdapter);

        //Получаем данные авторизовавшегося профиля и заполняем соответствующие поля имени и аватарки
        final VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200, contacts"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKApiUserFull user = ((VKList<VKApiUserFull>)response.parsedModel).get(0);
                profil_name.setText(user.first_name + " " + user.last_name);
                //Загружаем и округляем аватарку пользователя в фоне
                new DownloadAvatar(user.photo_200, avatar).execute();
            }
        });
    }

    public class VKFriendsRequestListener extends VKRequest.VKRequestListener {

        public VKFriendsRequestListener() {
            super();
        }

        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            mQuantityOfAllUser = ((VKUsersArray) response.parsedModel).getCount();
            for (VKApiUserFull currentUser : (VKUsersArray) response.parsedModel) {
                mFriendsAdapter.addItem(currentUser);
            }
            mFriendsAdapter.notifyDataSetChanged();
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            super.onProgress(progressType, bytesLoaded, bytesTotal);
        }
    }

    public void logout() {
        VKSdk.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_logout) logout();
        return super.onOptionsItemSelected(item);
    }

}
