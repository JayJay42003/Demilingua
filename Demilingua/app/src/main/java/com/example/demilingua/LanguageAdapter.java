package com.example.demilingua;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demilingua.model.Idioma;
import java.util.List;

import com.bumptech.glide.Glide;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private List<Idioma> languages;
    private OnLanguageClickListener listener;

    public interface OnLanguageClickListener {
        void onLanguageClick(Idioma idioma);
    }

    public LanguageAdapter(List<Idioma> languages, OnLanguageClickListener listener) {
        this.languages = languages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Idioma idioma = languages.get(position);
        holder.tvName.setText(idioma.getName());

        // --- LÓGICA DE CARGA DINÁMICA DE IMÁGENES ---
        // 1. Intentar cargar desde una URL si el modelo la tuviera (ej: idioma.getIconUrl())
        // 2. Como solución robusta, usamos un CDN de banderas basado en el nombre del idioma
        
        String langName = idioma.getName().toLowerCase();
        String flagUrl = "";

        if (langName.contains("inglés") || langName.contains("ingles")) {
            flagUrl = "https://flagcdn.com/w160/gb.png";
        } else if (langName.contains("francés") || langName.contains("frances")) {
            flagUrl = "https://flagcdn.com/w160/fr.png";
        } else if (langName.contains("español") || langName.contains("espanol")) {
            flagUrl = "https://flagcdn.com/w160/es.png";
        } else if (langName.contains("alemán") || langName.contains("aleman")) {
            flagUrl = "https://flagcdn.com/w160/de.png";
        } else if (langName.contains("italiano")) {
            flagUrl = "https://flagcdn.com/w160/it.png";
        } else if (langName.contains("portugués") || langName.contains("portugues")) {
            flagUrl = "https://flagcdn.com/w160/pt.png";
        }

        if (!flagUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(flagUrl)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.ivIcon);
        } else {
            // Fallback a recurso local si no hay URL
            holder.ivIcon.setImageResource(R.drawable.logo);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onLanguageClick(idioma);
        });
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

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