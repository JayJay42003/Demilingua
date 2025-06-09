package com.example.demilingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demilingua.model.RankingItem;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.VH> {

    private final List<RankingItem> data;
    public RankingAdapter(List<RankingItem> data){ this.data = data; }

    @NonNull
    @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v){
        View view = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_ranking, p, false);
        return new VH(view);
    }
    @Override public void onBindViewHolder(@NonNull VH h,int i){
        RankingItem item = data.get(i);
        h.tvPos.setText(String.valueOf(i+1));
        h.tvUser.setText(item.getUsuario());
        h.tvIdioma.setText(item.getIdioma());
        h.tvPts.setText(String.valueOf(item.getPuntos()));
    }
    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder{
        TextView tvPos,tvUser,tvIdioma,tvPts;
        VH(View v){
            super(v);
            tvPos   = v.findViewById(R.id.tvPos);
            tvUser  = v.findViewById(R.id.tvUser);
            tvIdioma= v.findViewById(R.id.tvIdioma);
            tvPts   = v.findViewById(R.id.tvPts);
        }
    }
}
