package com.idroidms.railway.parseutils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idroidms.railway.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by ubuntu1 on 15/3/16.
 */
public class OneToOneChatListAdapter extends RecyclerView.Adapter<OneToOneChatListAdapter.DataObjectHolder> {

    private static final String TAG = OneToOneChatListAdapter.class.getSimpleName();
    private List<Message> mDataset;
    private Context mContext;
    private ImageLoader imageLoader;
    private String mUserId;


    public OneToOneChatListAdapter(Context context, String userId, List<Message> myDataset) {
        mDataset = myDataset;
        mContext = context;
        mUserId = userId;
        imageLoader =  ImageLoader.getInstance(mContext);
        //imageLoader = CustomVolleyRequestQueue.getInstance(mContext).getImageLoader();


    }

    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat1, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        final boolean isMe = mDataset.get(position).getUserId().equals(mUserId);
        if (isMe) {
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);

            //holder.txtBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            holder.txtBody.setTextColor(mContext.getResources().getColor(R.color.login_btn));
            holder.llBody.setGravity(Gravity.CENTER_VERTICAL |Gravity.RIGHT);
            holder.txtName.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

        } else {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);

            holder.txtBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.txtBody.setTextColor(mContext.getResources().getColor(R.color.cardview_dark_background));
            holder.llBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.txtName.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);


        }
        final CircularImage profileView = isMe ? holder.imageRight : holder.imageLeft;
        try {
            if (mDataset.get(position).getProfileImage().length() != 0) {
                loadImage(mDataset.get(position).getProfileImage(), profileView, mContext);
            }else {
                loadImage(getProfileUrl(mDataset.get(position).getUserId()), profileView,mContext);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        if (isValidUrl(mDataset.get(position).getBody())){
            holder.imagePic.setVisibility(View.VISIBLE);
            holder.txtBody.setVisibility(View.GONE);
            loadImage(mDataset.get(position).getBody(),holder.imagePic,mContext);
        }else {
            holder.imagePic.setVisibility(View.GONE);
            holder.txtBody.setVisibility(View.VISIBLE);
            holder.txtBody.setText(mDataset.get(position).getBody());
        }
        holder.txtName.setText(mDataset.get(position).getUserName());
        //holder.txtUserName.setText(mDataset.get(position).getUserName() + " at " + mDataset.get(position).getDateTime());
        profileView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               /* String oId = mDataset.get(position).getUserId();
                Intent i = new Intent(mContext, UserProfileActivity.class);
                i.putExtra(ApplicationConstant.FLAG,oId);
                mContext.startActivity(i);*/
            }
        });
    }

    public void addItem(Message dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void loadImage( String url,CircularImage imageView, Context context) {
        //Ion.with(context).load(url).intoImageView(imageView);
        imageLoader.DisplayImage(url,imageView);
    }
    public void loadImage( String url,ImageView imageView, Context context) {
        //Ion.with(context).load(url).intoImageView(imageView);
        imageLoader.DisplayImage(url,imageView);
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView txtBody,txtName;
        CircularImage imageLeft, imageRight;
        LinearLayout llBody;
        ImageView imagePic;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageLeft = (CircularImage) itemView.findViewById(R.id.ivProfileLeft);
            imageRight = (CircularImage) itemView.findViewById(R.id.ivProfileRight);
            txtBody = (TextView) itemView.findViewById(R.id.txtBody);
            llBody = (LinearLayout) itemView.findViewById(R.id.llText);
            txtName = (TextView)itemView.findViewById(R.id.txtName);
            imagePic = (ImageView)itemView.findViewById(R.id.imgPic);
            //txtUserName = (TextView) itemView.findViewById(R.id.userName);
            //llBody = (LinearLayout)itemView.findViewById(R.id.llBody);
        }

    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url);
        if(m.matches())
            return true;
        else
            return false;
    }
}
