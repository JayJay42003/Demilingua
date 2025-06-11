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
    private OnLanguageClickListener listener;

    public interface OnLanguageClickListener {
        void onLanguageClick(Idioma idioma);
    }

    public LanguageAdapter(List<Idioma> languages,OnLanguageClickListener listener) {
        this.languages = languages;
        this.listener=listener;
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
        Idioma language = languages.get(position);
        holder.ivIcon.setImageResource(language.getIcon());
        holder.tvName.setText(language.getName());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Seleccionado: " + language.getName(), Toast.LENGTH_SHORT).show();
            listener.onLanguageClick(language);
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