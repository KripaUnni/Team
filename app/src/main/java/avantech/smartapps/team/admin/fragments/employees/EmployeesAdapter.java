package avantech.smartapps.team.admin.fragments.employees;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import avantech.smartapps.team.R;
import avantech.smartapps.team.model.EmployeesModel;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.ViewHolder>{
    ArrayList<EmployeesModel> employeesData;
    Context applicationContext;
    private static final String TAG = "EMPLOYEES FRAGMENT";
    public EmployeesAdapter(ArrayList<EmployeesModel> employeesData, Context applicationContext) {
        this.employeesData = employeesData;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_item,parent,false);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.name.setText(employeesData.get(position).getName());
        holder.role.setText(employeesData.get(position).getDesignation());
    }

    @Override
    public int getItemCount() {
        return employeesData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,role;
        LinearLayout ll_worker_item;
        public ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.worker_name);
            role = itemView.findViewById(R.id.worker_role);
            ll_worker_item = itemView.findViewById(R.id.ll_worker_item);
        }
    }
}
