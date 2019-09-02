package com.inc.ocapop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inc.ocapop.R;

import java.util.ArrayList;
import java.util.Arrays;

public class AdapterCategoriasCadastro extends RecyclerView.Adapter<AdapterCategoriasCadastro.CategoriaCadastroItem> {

    private ArrayList<String> arrayList;
    private ArrayList<String> categoriasSelecionadas;
    private Context context;
    private MudancaDeCategoria mudancaDeCategoria;

    public AdapterCategoriasCadastro(Context context, MudancaDeCategoria mudancaDeCategoria, ArrayList<String> selecionadas) {
        this.arrayList = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.categorias)));
        this.context = context;
        this.mudancaDeCategoria = mudancaDeCategoria;
        this.categoriasSelecionadas = selecionadas;
    }

    @Override
    public int getItemViewType(int position) {
        if (categoriasSelecionadas != null) {

            if (categoriasSelecionadas.contains(String.valueOf(position))) {
                return 1;
            }
        }

        return 0;
    }

    @NonNull
    @Override
    public CategoriaCadastroItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categoria_cadastro, parent, false);
        CategoriaCadastroItem cadastroItem = new CategoriaCadastroItem(view);
        if (viewType == 1) {
            cadastroItem.checkBox.setChecked(true);
        }
        return cadastroItem;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaCadastroItem holder, int position) {
        holder.checkBox.setText(arrayList.get(position) + " ");

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface MudancaDeCategoria {
        void mudanca(String id, int pos, boolean adicionar);
    }



    class CategoriaCadastroItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CheckBox checkBox;

        public CategoriaCadastroItem(@NonNull View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_categoria_cadastro);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mudancaDeCategoria.mudanca(arrayList.get(getAdapterPosition()), getAdapterPosition(), checkBox.isChecked());
        }
    }

}
