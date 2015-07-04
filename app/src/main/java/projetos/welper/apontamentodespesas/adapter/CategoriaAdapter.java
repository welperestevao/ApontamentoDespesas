package projetos.welper.apontamentodespesas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import projetos.welper.apontamentodespesas.R;
import projetos.welper.apontamentodespesas.model.Categoria;

/**
 * Created by welper on 03/07/2015.
 */
public class CategoriaAdapter extends BaseAdapter{

    private List<Categoria> list;
    private Context context;

    public CategoriaAdapter(List<Categoria> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void swapViews(List<Categoria> categorias) {
        this.list = categorias;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Categoria e = list.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = (View) inflater.inflate(R.layout.categoria_detalhe, null);
        TextView categoria = (TextView) v.findViewById(R.id.categoria);
        categoria.setText(e.getDescricao());
        return v;
    }
}
