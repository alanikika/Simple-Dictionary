package com.example.bening_2.alansdictionary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bening_2.alansdictionary.R;
import com.example.bening_2.alansdictionary.model.Kamus;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Bening_2 on 3/9/2018.
 */

public class KamusAdapter extends RecyclerView.Adapter<KamusAdapter.KamusHolder>{

    private ArrayList<Kamus> mData = new ArrayList<>();

    private Context context;

    private LayoutInflater mInflater;

    public KamusAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setListWordAdapter(ArrayList<Kamus> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public KamusHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_result, parent, false);
        return new KamusHolder(view);
    }

    @Override
    public void onBindViewHolder(KamusHolder holder, int position) {
        holder.tvWord.setText(mData.get(position).getKata());
        holder.tvDefiniton.setText(mData.get(position).getTerjemahan());
    }

    public void addItem(ArrayList<Kamus> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class KamusHolder extends RecyclerView.ViewHolder {

        private TextView tvWord, tvDefiniton;

        public KamusHolder(View itemView) {
            super(itemView);

            tvWord = (TextView) itemView.findViewById(R.id.tv_word);
            tvDefiniton = (TextView) itemView.findViewById(R.id.tv_definition);
        }
    }

}
