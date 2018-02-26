package com.codepath.apps.restclienttemplate.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.Adapter.ItemClickListenerInterface;
import com.codepath.apps.restclienttemplate.Adapter.TweetAdapter;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.service.TwiterApp;
import com.codepath.apps.restclienttemplate.service.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.service.EndlessRecyclerViewScrollListener;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		//Toast.makeText(this,"Succes",Toast.LENGTH_LONG).show();
		Intent i = new Intent(this, TimelineActivity.class);
		startActivity(i);
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

	public static class TimelineActivity extends AppCompatActivity implements ItemClickListenerInterface {
		ProgressDialog pd;
        private TwitterClient client;
        private TweetAdapter tweetAdapter;
        private RecyclerView rvTweets;
        private ArrayList<Tweet> tweets;
        private EndlessRecyclerViewScrollListener scrollListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_timeline);
            setupViews();

            populateTimeline(1);
        }

        @Override
        public void onClick(View view, int position) {

            Toast.makeText(this,"test click",Toast.LENGTH_LONG);
        }

        private void setupViews(){
        client = TwiterApp.getRestClient();
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        tweets =new ArrayList<>();
        tweetAdapter = new TweetAdapter(this,tweets);
        tweetAdapter.setClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetAdapter);

        //rvTweets.setLayoutManager(gridLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list

                loadNextDataFromApi(page);


            }
        };
        rvTweets.addOnScrollListener(scrollListener);


    }
        private void loadNextDataFromApi(int offset) {
            Toast.makeText(this, "Searching for test " , Toast.LENGTH_LONG).show();
            populateTimeline(offset+1);
            // Send an API request to retrieve appropriate paginated data
            //  --> Send the request including an offset value (i.e `page`) as a query parameter.
            //  --> Deserialize and construct new model objects from the API response
            //  --> Append the new data objects to the existing set of items inside the array of items
            //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
            //onArticleSearchSroll(queryt, offset);
        }

        private void populateTimeline(int i) {
            pd= new ProgressDialog(this);
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.show();

            client.getHomeTimeline(i,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //super.onSuccess(statusCode, headers, response);
                    Log.d("Tweetersucces",response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

					pd.dismiss();
                   // try {


						final ArrayList<Tweet> listTweet = Tweet.fromJson(response);

						// Parse json array into array of model objects
						// Remove all books from the adapter


						// Remove all books from the adapter
						//tweets.clear();
						//tweetAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
// 3. Reset endless scroll listener when performing a new search
						//scrollListener.resetState();
						// Load model objects into the adapter
						for (Tweet tweet : listTweet) {
                            tweets.add(tweet);
                            tweetAdapter.notifyItemInserted(tweets.size()-1);; // add book through the adapter
						}
						//tweetAdapter.notifyDataSetChanged();
						Log.d("Debug", "test");


					//} catch (JSONException e) {
                        // Invalid JSON format, show appropriate error.t
                            // e.printStackTrace();
                       //   }

                   /* try {
                        JSONArray articleJSONArray;
                        if (response != null) {
                            // Get the docs json array
                            articleJSONArray = response.getJSONObject("response").getJSONArray("docs");
                            // Parse json array into array of model objects
                            final ArrayList<Article> articles = Article.fromJasonArray(articleJSONArray);

                            // Parse json array into array of model objects
                            // Remove all books from the adapter


                            // Remove all books from the adapter
                            arrayArticle.clear();
                            adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
    // 3. Reset endless scroll listener when performing a new search
                            scrollListener.resetState();
                            // Load model objects into the adapter
                            for (Article article : articles) {
                                arrayArticle.add(article); // add book through the adapter
                            }
                            adapter.notifyDataSetChanged();
                            Log.d("Debug", "test");
                        }
                    } catch (JSONException e) {
                        // Invalid JSON format, show appropriate error.t
                        e.printStackTrace();
                    }


                }
            });
        } else {
            Toast.makeText(this, "Mauvaise connection ", Toast.LENGTH_SHORT).show();
        }
                    //super.onSuccess(statusCode, headers, response);
                    Log.d("Tweetersucces",response.toString());
                */}

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("Tweeter",errorResponse.toString());
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                   // super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("Tweeter",errorResponse.toString());
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                   // super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("Tweeter",responseString);
                    throwable.printStackTrace();
                }
            });
        }
    }
}
