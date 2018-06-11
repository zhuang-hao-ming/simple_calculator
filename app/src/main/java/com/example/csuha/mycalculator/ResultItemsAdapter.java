package com.example.csuha.mycalculator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by csuha on 2018/6/10.
 */

class ResultItemViewHolder extends RecyclerView.ViewHolder {
    public TextView input;
    public TextView result;
    public ResultItemViewHolder(View v) {
        super(v);

        input = (TextView) v.findViewById(R.id.input);
        result = (TextView) v.findViewById(R.id.result);
    }
}


public class ResultItemsAdapter extends RecyclerView.Adapter<ResultItemViewHolder>{
    private List<ResultItem> resultItemList;

    public ResultItemsAdapter(List<ResultItem> dataset) {
        resultItemList = dataset;
    }

    @Override
    public ResultItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_view, parent, false);
        return  new ResultItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ResultItemViewHolder holder, int position) {
        ResultItem item = resultItemList.get(position);
        holder.input.setText(item.input  + " = ");
        holder.result.setText(item.result);
    }

    @Override
    public int getItemCount() {
        return this.resultItemList.size();
    }
}
