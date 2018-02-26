package com.codepath.apps.restclienttemplate.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2/24/2018.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private List<Tweet> mTweets;
    private Context mContext;

    public TweetAdapter(@NonNull Context context, List<Tweet> tweets) {
        mTweets=tweets;
        mContext=context;
        Log.d("adapter","testOK");
    }

    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        TweetAdapter.ViewHolder viewHolder = new TweetAdapter.ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetAdapter.ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);

        // Populate data into the template view using the data object
        holder.tvbody.setText(tweet.getBody());
        holder.tvuUsername.setText(tweet.getUser().getName());
        holder.tvcreateAt.setText(tweet.getCreated_at());
        Glide.with(mContext)
                .load(Uri.parse(tweet.getUser().getProfile_imageURL())).into(holder.image);

    }
    private String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public void setClickListener(ItemClickListenerInterface   clickListener) {
        this.clickListener = clickListener;
    }

    private ItemClickListenerInterface  clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView image;
            public TextView tvuUsername;
            public TextView tvbody;
            public TextView tvcreateAt;


            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                image = (ImageView)itemView.findViewById(R.id.ivProfieImageUrl);
                tvuUsername = (TextView)itemView.findViewById(R.id.tvUserName);
                tvbody=(TextView)itemView.findViewById(R.id.tvBody);
                tvcreateAt=(TextView)itemView.findViewById(R.id.tvcreated_at);
                // tvAuthor = (TextView)itemView.findViewById(R.id.tvAuthor);
               itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (clickListener!=null)
                    clickListener.onClick(view,getAdapterPosition());

            }
        }


        //public void setClickListener(ItemClickListenerInterface   clickListener) {
          //  this.clickListener = clickListener;
      //  }

       // private ItemClickListenerInterface  clickListener;





        // Usually involves inflating a layout from XML and returning the holder




        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }


}
