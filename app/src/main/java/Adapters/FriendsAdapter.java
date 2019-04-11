package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.vk.sdk.api.model.VKApiUserFull;

import Requests.DownloadAvatar;

public class FriendsAdapter extends BaseAdapter {
    private SparseArray<VKApiUserFull> mFriendsArray;

    private boolean mUpdatedData = false;

    public SparseArray<VKApiUserFull> getFriendsArray(){
        return mFriendsArray.clone();
    }

    public void addItem(VKApiUserFull vkApiUserFull){
        mFriendsArray.put(vkApiUserFull.id, vkApiUserFull);
    }

    public void setOnline(int id,boolean isOnline){

        mFriendsArray.get(id).online = isOnline;
        notifyDataSetChanged();
    }

    private Context mContext;
    private int mLayout;
    private LayoutInflater mInflaterFriends;
    public FriendsAdapter(Context context, SparseArray<VKApiUserFull> friendsArray, int layout){
        mContext = context;
        mFriendsArray = friendsArray;
        mLayout = layout;
        mInflaterFriends = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFriendsArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mFriendsArray.get(mFriendsArray.keyAt(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflaterFriends.inflate(mLayout,parent,false);
        }
        VKApiUserFull currentFriend = (VKApiUserFull) getItem(position);
        FriendsViewHolder friendsViewHolder = (FriendsViewHolder) convertView.getTag();

        if(friendsViewHolder == null){
            friendsViewHolder = new FriendsViewHolder(convertView);
            convertView.setTag(friendsViewHolder);
        }
        new DownloadAvatar(currentFriend.photo_100, friendsViewHolder.getAvatar()).execute();
        friendsViewHolder.getFirstLastNames().setText(currentFriend.first_name + " " + currentFriend.last_name);
        friendsViewHolder.getOnline().setText(currentFriend.online ? "ON" : "OFF");
        friendsViewHolder.getOnline().setTextColor(currentFriend.online ? Color.GREEN : Color.RED);
        return convertView;

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mUpdatedData = true;
    }

}
