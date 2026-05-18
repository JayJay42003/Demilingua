package com.example.demilingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demilingua.controller.ApiService;
import com.example.demilingua.controller.RetrofitClient;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.ViewHolder> {

    private List<Map<String, String>> amigosList;
    private int usuarioId;
    private boolean esBusqueda;

    public AmigosAdapter(List<Map<String, String>> amigosList, int usuarioId, boolean esBusqueda) {
        this.amigosList = amigosList;
        this.usuarioId = usuarioId;
        this.esBusqueda = esBusqueda;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amigo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> item = amigosList.get(position);
        ApiService api = RetrofitClient.getApiService();

        if (esBusqueda) {
            String nombre = item.get("nombre");
            int amigoId = Integer.parseInt(item.get("id"));
            holder.tvAmigoId.setText(nombre);
            holder.tvAmigoEstado.setText("ID: " + amigoId);
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnAccept.setImageResource(android.R.drawable.ic_input_add);
            holder.btnDelete.setVisibility(View.GONE);

            holder.btnAccept.setOnClickListener(v -> {
                api.sendFriendRequest(usuarioId, amigoId).enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(holder.itemView.getContext(), "Solicitud enviada", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {}
                });
            });
        } else {
            int amigoId = Integer.parseInt(item.get("amigo_id"));
            String estado = item.get("estado");
            holder.tvAmigoId.setText("Amigo ID: " + amigoId);
            holder.tvAmigoEstado.setText("Estado: " + estado);

            if ("PENDIENTE".equals(estado)) {
                holder.btnAccept.setVisibility(View.VISIBLE);
                holder.btnAccept.setOnClickListener(v -> {
                    api.acceptFriend(usuarioId, amigoId).enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            if (response.isSuccessful()) {
                                item.put("estado", "ACEPTADO");
                                notifyItemChanged(position);
                            }
                        }
                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {}
                    });
                });
            } else {
                holder.btnAccept.setVisibility(View.GONE);
            }

            holder.btnDelete.setOnClickListener(v -> {
                api.deleteFriend(usuarioId, amigoId).enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        if (response.isSuccessful()) {
                            amigosList.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {}
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return amigosList != null ? amigosList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmigoId, tvAmigoEstado;
        ImageButton btnAccept, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmigoId = itemView.findViewById(R.id.tvAmigoId);
            tvAmigoEstado = itemView.findViewById(R.id.tvAmigoEstado);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}