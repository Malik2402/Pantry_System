package za.co.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import za.co.smartpantry.R;
import za.co.smartpantry.model.PantryItem;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.Holder> {
    public interface Actions {
        void edit(PantryItem item);
        void delete(PantryItem item);
    }
    private final List<PantryItem> items = new ArrayList<>();
    private final Actions actions;
    private boolean expiryIndicators;

    public void setExpiryIndicators(boolean enabled) { expiryIndicators = enabled; }

    public PantryAdapter(Actions actions) { this.actions = actions; }

    public void submit(List<PantryItem> updated) {
        items.clear();
        items.addAll(updated);
        notifyDataSetChanged();
    }

    @Override public Holder onCreateViewHolder(ViewGroup parent, int type) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false));
    }

    @Override public void onBindViewHolder(Holder holder, int position) {
        PantryItem item = items.get(position);
        holder.name.setText(item.name);
        holder.amount.setText(item.amountLabel());
        holder.expiry.setText(item.expiry == null ? "No expiry date" : "Expiry: " + item.expiry);
        if (expiryIndicators && item.expiry != null) {
            java.time.LocalDate date = java.time.LocalDate.parse(item.expiry);
            java.time.LocalDate today = java.time.LocalDate.now();
            if (date.isBefore(today)) holder.expiry.setText("Expired · " + item.expiry);
            else if (!date.isAfter(today.plusDays(7))) holder.expiry.setText("Expiring soon · " + item.expiry);
        }
        holder.itemView.findViewById(R.id.edit_item).setContentDescription("Edit " + item.name);
        holder.itemView.findViewById(R.id.delete_item).setContentDescription("Delete " + item.name);
        holder.itemView.findViewById(R.id.edit_item).setOnClickListener(view -> actions.edit(item));
        holder.itemView.findViewById(R.id.delete_item).setOnClickListener(view -> actions.delete(item));
    }

    @Override public int getItemCount() { return items.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView amount;
        final TextView expiry;
        Holder(View view) {
            super(view);
            name = view.findViewById(R.id.item_name);
            amount = view.findViewById(R.id.item_amount);
            expiry = view.findViewById(R.id.item_expiry);
        }
    }
}
