package com.example.demilingua;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demilingua.controller.CourseResponse;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final List<CourseResponse> items;

    public CourseAdapter(List<CourseResponse> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseResponse curso = items.get(position);
        holder.tvNombre.setText(curso.getNombre());
        holder.tvDescripcion.setText(curso.getDescripcion());
        holder.tvDificultad.setText("Nivel: " + curso.getDificultad());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ───────────────────────── ViewHolder ─────────────────────────
    static class CourseViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombre, tvDescripcion, tvDificultad;

        CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre      = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvDificultad  = itemView.findViewById(R.id.tvDificultad);
        }
    }
}
