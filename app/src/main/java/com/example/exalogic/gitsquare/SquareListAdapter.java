package com.example.exalogic.gitsquare;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exalogic.gitsquare.ModelClass.SquareListDetailModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SquareListAdapter extends RecyclerView.Adapter<SquareListAdapter.ViewHolder> {

    private ArrayList<SquareListDetailModel> arrayList;
    private Activity activity;
    public static OnItemClickListener mItemClickListener;
    private Context context;

    public SquareListAdapter(Activity activity, ArrayList<SquareListDetailModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item__details, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        SquareListDetailModel list = arrayList.get(position);

        holder.tvLoginName.setText("" + list.getLogin());
        holder.tvReposUrl.setText("" + list.getReposUrl());
        holder.tvContribution.setText("Contributions" + "  " + list.getContributions());

        Picasso.with(context).load(arrayList.get(position).getAvatarUrl()).resize(50, 50)
                .transform(new CircleTransform()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvLoginName, tvReposUrl, tvContribution;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);

            this.tvLoginName = (TextView) v.findViewById(R.id.tv_name);
            this.tvReposUrl = (TextView) v.findViewById(R.id.tvReposUrl);
            this.tvContribution = (TextView) v.findViewById(R.id.tvContribution);
            this.imageView = (ImageView) v.findViewById(R.id.imageview);

            this.tvReposUrl.setOnClickListener(this);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tvReposUrl:
                    mItemClickListener.onUrlClick(v, getAdapterPosition());
                    break;
                default:
                    mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);
        public void onUrlClick(View view, int position);


    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public ArrayList<SquareListDetailModel> getCurrentDada() {
        return this.arrayList;
    }
}
