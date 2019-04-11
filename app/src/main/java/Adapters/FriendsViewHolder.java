package Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.testapp.oauthvk.R;

public class FriendsViewHolder {

    private ImageView mAvatar = null;
    private TextView mFirstLastNames = null;
    private TextView mOnline = null;

    public FriendsViewHolder(View view){
        mAvatar = view.findViewById(R.id.avatar);
        mFirstLastNames = view.findViewById(R.id.first_last_name);
        mOnline = view.findViewById(R.id.online);
    }

    public ImageView getAvatar() {
        return mAvatar;
    }

    public TextView getFirstLastNames() {
        return mFirstLastNames;
    }

    public TextView getOnline() {
        return mOnline;
    }
}
