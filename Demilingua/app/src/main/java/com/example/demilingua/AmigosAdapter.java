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
import com.example.demilingua.model.Amistad;
import com.example.demilingua.model.GenericResponse;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.ViewHolder> {

    private List<Amistad> amigosList;
    private int usuarioId;
    private boolean esBusqueda;
    private ApiService api;

    public AmigosAdapter(List<Amistad> amigosList, int usuarioId, boolean esBusqueda, ApiService api) {
        this.amigosList = amigosList;
        this.usuarioId = usuarioId;
        this.esBusqueda = esBusqueda;
        this.api = api;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amigo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Amistad item = amigosList.get(holder.getAdapterPosition());

        if (esBusqueda) {
            holder.tvAmigoId.setText(item.getNombre());
            holder.tvAmigoEstado.setText("Puntos: " + item.getPuntos());
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnAccept.setImageResource(android.R.drawable.ic_input_add);
            holder.btnDelete.setVisibility(View.GONE);

            holder.btnAccept.setOnClickListener(v -> {
                api.sendFriendRequest(usuarioId, item.getAmigoId()).enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GenericResponse> call, @NonNull Response<GenericResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(holder.itemView.getContext(), "Solicitud enviada", Toast.LENGTH_SHORT).show();
                            holder.btnAccept.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {
                        Toast.makeText(holder.itemView.getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            String displayName = item.getNombre() != null ? item.getNombre() : "Usuario #" + item.getAmigoId();
            holder.tvAmigoId.setText(displayName);
            holder.tvAmigoEstado.setText("Puntos: " + item.getPuntos() + " (" + item.getEstado() + ")");
            holder.btnDelete.setVisibility(View.VISIBLE);

            if ("PENDIENTE".equals(item.getEstado())) {
                holder.btnAccept.setVisibility(View.VISIBLE);
                holder.btnAccept.setImageResource(android.R.drawable.ic_menu_save);
                holder.btnAccept.setOnClickListener(v -> {
                    api.acceptFriend(usuarioId, item.getAmigoId()).enqueue(new Callback<GenericResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<GenericResponse> call, @NonNull Response<GenericResponse> response) {
                            if (response.isSuccessful()) {
                                item.setEstado("ACEPTADO");
                                notifyItemChanged(holder.getAdapterPosition());
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {}
                    });
                });
            } else {
                holder.btnAccept.setVisibility(View.GONE);
            }

            holder.btnDelete.setOnClickListener(v -> {
                int currentPos = holder.getAdapterPosition();
                api.deleteFriend(usuarioId, item.getAmigoId()).enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GenericResponse> call, @NonNull Response<GenericResponse> response) {
                        if (response.isSuccessful()) {
                            amigosList.remove(currentPos);
                            notifyItemRemoved(currentPos);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {}
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