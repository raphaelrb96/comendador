package com.inc.comendador.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inc.comendador.R;
import com.inc.comendador.objects.Usuario;

import java.util.ArrayList;

public class AdapterAfilidos extends RecyclerView.Adapter<AdapterAfilidos.AfiliadosViewHolder> {


    private ArrayList<Usuario> usuarios;
    private Context context;

    public AdapterAfilidos(ArrayList<Usuario> usuarios, Context context) {
        this.usuarios = usuarios;
        this.context = context;
    }

    @NonNull
    @Override
    public AfiliadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario_afiliado, parent, false);
        return new AfiliadosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AfiliadosViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);

        Glide.with(context).load(usuario.getPathFoto()).into(holder.img_perfil_usuario_afiliado);

        if (usuario.isAdmConfirmado()) {
            holder.icon_afiliado_em_autenticado.setVisibility(View.VISIBLE);
            holder.icon_afiliado_em_analise.setVisibility(View.GONE);
        } else {
            holder.icon_afiliado_em_autenticado.setVisibility(View.GONE);
            holder.icon_afiliado_em_analise.setVisibility(View.VISIBLE);
        }

        if (usuario.getUserName() != null) {
            holder.tv_apelido_afiliado.setText(usuario.getUserName());
        } else {
            holder.tv_apelido_afiliado.setText("Usuario sem nome");
        }

        if (usuario.getUserName() != null) {
            holder.tv_nome_afiliado.setText(usuario.getNome());
        } else {
            holder.tv_nome_afiliado.setText("Usuario sem nome");
        }

    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    class AfiliadosViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_perfil_usuario_afiliado, icon_afiliado_em_autenticado, icon_afiliado_em_analise;
        private TextView tv_nome_afiliado, tv_apelido_afiliado;

        public AfiliadosViewHolder(@NonNull View itemView) {
            super(itemView);
            img_perfil_usuario_afiliado = (ImageView) itemView.findViewById(R.id.img_perfil_usuario_afiliado);
            icon_afiliado_em_autenticado = (ImageView) itemView.findViewById(R.id.icon_afiliado_em_autenticado);
            icon_afiliado_em_analise = (ImageView) itemView.findViewById(R.id.icon_afiliado_em_analise);
            tv_nome_afiliado = (TextView) itemView.findViewById(R.id.tv_nome_afiliado);
            tv_apelido_afiliado = (TextView) itemView.findViewById(R.id.tv_apelido_afiliado);
        }
    }

}
