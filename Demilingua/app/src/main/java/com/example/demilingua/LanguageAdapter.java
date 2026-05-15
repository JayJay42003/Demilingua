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
        // Correcciones aplicadas aquí:
        Idioma idioma = languages.get(position);
        holder.tvName.setText(idioma.getName());

        String langName = idioma.getName().toLowerCase();

        if (langName.contains("inglés") || langName.contains("ingles")) {
            holder.ivIcon.setImageResource(R.drawable.reino_unido);
        } else if (langName.contains("francés") || langName.contains("frances")) {
            holder.ivIcon.setImageResource(R.drawable.francia);
        } else if (langName.contains("español") || langName.contains("espanol")) {
            holder.ivIcon.setImageResource(R.drawable.espa_a);
        } else {
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