package com.example.demilingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demilingua.model.Idioma;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private List<Idioma> languages;

    public interface OnLanguageClickListener {
        void onLanguageClick(Idioma idioma);
    }

    public LanguageAdapter(List<Idioma> languages) {
        this.languages = languages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.idioma_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Idioma language = languages.get(position);
        holder.ivIcon.setImageResource(language.getIcon());
        holder.tvName.setText(language.getName());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Seleccionado: " + language.getName(), Toast.LENGTH_SHORT).show();
            // Aquí puedes abrir una actividad de aprendizaje del idioma

        });
    }

    @Override
    public int getItemCount() { return languages.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvLevel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvLevel = itemView.findViewById(R.id.tvLevel);
        }
    }
}