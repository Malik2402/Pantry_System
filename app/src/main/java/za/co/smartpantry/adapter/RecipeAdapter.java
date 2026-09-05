package za.co.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import za.co.smartpantry.R;
import za.co.smartpantry.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.Holder> {
    public interface Selection { void open(long recipeId); }
    private final List<Recipe> recipes = new ArrayList<>();
    private final Selection selection;

    public RecipeAdapter(Selection selection) { this.selection = selection; }

    public void submit(List<Recipe> updated) {
        recipes.clear();
        recipes.addAll(updated);
        notifyDataSetChanged();
    }

    @Override public Holder onCreateViewHolder(ViewGroup parent, int type) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false));
    }

    @Override public void onBindViewHolder(Holder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.name.setText(recipe.name);
        holder.count.setText(recipe.ingredients.size() + " ingredients · all available");
        holder.itemView.findViewById(R.id.open_recipe).setContentDescription("View " + recipe.name);
        holder.itemView.findViewById(R.id.open_recipe).setOnClickListener(view -> selection.open(recipe.id));
    }

    @Override public int getItemCount() { return recipes.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView count;
        Holder(View view) {
            super(view);
            name = view.findViewById(R.id.recipe_name);
            count = view.findViewById(R.id.recipe_count);
        }
    }
}
