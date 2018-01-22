package com.example.exalogic.gitsquare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;

import com.example.exalogic.gitsquare.ModelClass.SquareListDetailModel;

import java.util.ArrayList;
import java.util.Collections;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SquareContribsActivity extends AppCompatActivity {
    private RecyclerView square_list_item_view;
    private ArrayList<SquareListDetailModel> squareArraylist;
    private SquareListAdapter listAdapter;
    private Context context = this;
    private SquareListDetailModel listDetail;
    private static final String JSON_URL = "https://api.github.com/repos/square/retrofit/contributors";
    private Button filterBtn;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        square_list_item_view = (RecyclerView) findViewById(R.id.list_item);
        filterBtn = (Button) findViewById(R.id.filterBtn);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        squareArraylist = new ArrayList<>();
        listAdapter = new SquareListAdapter(this, squareArraylist, context);

        square_list_item_view.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        square_list_item_view.setLayoutManager(llm);

        square_list_item_view.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(squareArraylist);
                listAdapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSquareListDetail();
            }
        });


        listAdapter.SetOnItemClickListener(new SquareListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onUrlClick(View view, int position) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(squareArraylist.get(position).getReposUrl())));
            }
        });

        getSquareListDetail();

    }

    private void getSquareListDetail() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            squareArraylist.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    listDetail = new SquareListDetailModel(jsonObject.getString("login"),
                                            jsonObject.getInt("id"), jsonObject.getString("avatar_url"),
                                            jsonObject.getString("gravatar_id"), jsonObject.getString("url"),
                                            jsonObject.getString("html_url"), jsonObject.getString("followers_url"),
                                            jsonObject.getString("following_url"), jsonObject.getString("gists_url"),
                                            jsonObject.getString("starred_url"), jsonObject.getString("subscriptions_url"),
                                            jsonObject.getString("organizations_url"), jsonObject.getString("repos_url"),
                                            jsonObject.getString("events_url"), jsonObject.getString("received_events_url"),
                                            jsonObject.getString("type"), jsonObject.getBoolean("site_admin"),
                                            jsonObject.getInt("contributions"));
                                    squareArraylist.add(listDetail);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            listAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }
}
