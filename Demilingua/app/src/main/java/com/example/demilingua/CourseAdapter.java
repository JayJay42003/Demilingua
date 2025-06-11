package com.example.demilingua;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demilingua.model.Curso;

import java.util.List;
import java.util.Map;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    public interface OnCourseClickListener {
        void onCourseClick(Curso curso);
    }
    private final List<Curso> items;
    private final OnCourseClickListener listener;

    public CourseAdapter(List<Curso> items, OnCourseClickListener listener) {
        this.items = items;
        this.listener=listener;
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
        Curso curso = items.get(position);
        holder.tvNombre.setText(curso.getNombre());
        holder.tvDescripcion.setText(curso.getDescripcion());
        holder.tvDificultad.setText(new StringBuilder().append("Nivel: ").append(curso.getDificultad()).toString());

        holder.card.setOnClickListener(v -> {
            if (listener != null) listener.onCourseClick(curso);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    // ───────────────────────── ViewHolder ─────────────────────────
    static class CourseViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombre, tvDescripcion, tvDificultad;
        final View card;

        CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre      = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvDificultad  = itemView.findViewById(R.id.tvDificultad);
            card = itemView.findViewById(R.id.cardCurso);
        }
    }
}
