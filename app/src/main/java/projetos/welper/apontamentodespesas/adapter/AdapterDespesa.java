package projetos.welper.apontamentodespesas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import projetos.welper.apontamentodespesas.R;
import projetos.welper.apontamentodespesas.model.Despesa;

/**
 * Created by welper on 27/06/2015.
 */
public class AdapterDespesa extends BaseAdapter {

    private List<Despesa> list;
    private Context context;

    public AdapterDespesa(List<Despesa> list, Context context) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Despesa e = list.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.despesa_list,null);

        TextView categoria = (TextView) v.findViewById(R.id.categoria);
        categoria.setText("Categoria: " + e.getCategoria());

        TextView despesa = (TextView) v.findViewById(R.id.despesa);
        despesa.setText("Despesa: " + e.getDescricao());

        TextView data = (TextView) v.findViewById(R.id.data);
        data.setText("Data: " + e.getDataFormatada());

        TextView valor = (TextView) v.findViewById(R.id.valor);
        valor.setText("Valor: " + NumberFormat.getCurrencyInstance().format( new Double(e.getValor().toString()) ));

        TextView pgto = (TextView) v.findViewById(R.id.formaPgto);
        pgto.setText("Forma Pagamento: " + e.getFormaPgto());

        return v;
    }

    public void swapViews(List<Despesa> despesas) {
        this.list = despesas;
        notifyDataSetChanged();
    }
}
