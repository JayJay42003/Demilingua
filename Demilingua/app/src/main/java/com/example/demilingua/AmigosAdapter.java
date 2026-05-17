package com.example.demilingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.ViewHolder> {

    private List<Map<String, String>> amigosList;

    public AmigosAdapter(List<Map<String, String>> amigosList) {
        this.amigosList = amigosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amigo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> amigo = amigosList.get(position);
        holder.tvAmigoId.setText("ID Amigo: " + amigo.get("amigo_id"));
        holder.tvAmigoEstado.setText("Estado: " + amigo.get("estado"));
    }

    @Override
    public int getItemCount() {
        return amigosList != null ? amigosList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmigoId, tvAmigoEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmigoId = itemView.findViewById(R.id.tvAmigoId);
            tvAmigoEstado = itemView.findViewById(R.id.tvAmigoEstado);
        }
    }
}