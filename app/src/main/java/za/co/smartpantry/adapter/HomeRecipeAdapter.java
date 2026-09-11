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

public final class HomeRecipeAdapter extends RecyclerView.Adapter<HomeRecipeAdapter.Holder> {
    public interface OpenRecipe { void open(long id); }
    private final OpenRecipe action;
    private final List<Recipe> recipes = new ArrayList<>();

    public HomeRecipeAdapter(OpenRecipe action) { this.action = action; }

    public void submit(List<Recipe> entries) {
        int previous = recipes.size();
        recipes.clear();
        if (previous > 0) notifyItemRangeRemoved(0, previous);
        recipes.addAll(entries);
        if (!recipes.isEmpty()) notifyItemRangeInserted(0, recipes.size());
    }

    @Override public Holder onCreateViewHolder(ViewGroup parent, int type) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_recipe, parent, false));
    }

    @Override public void onBindViewHolder(Holder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.name.setText(recipe.name);
        int art = R.drawable.home_food;
        if (recipe.id == 2 || recipe.id == 13 || recipe.id == 28) art = R.drawable.home_food_eggs;
        else if (recipe.id == 3 || recipe.id == 25) art = R.drawable.home_food_smoothie;
        else if (recipe.id == 5 || recipe.id == 12 || recipe.id == 15 || recipe.id == 21 || recipe.id == 22 || recipe.id == 27 || recipe.id == 32) art = R.drawable.home_food_bread;
        else if (recipe.id == 4 || recipe.id == 7 || recipe.id == 14 || recipe.id == 18 || recipe.id == 19 || recipe.id == 23 || recipe.id == 24 || recipe.id == 30 || recipe.id == 34 || recipe.id == 40)
            art = R.drawable.home_food_grains;
        ((android.widget.ImageView) holder.itemView.findViewById(R.id.home_recipe_art)).setImageResource(art);
        holder.meta.setText(holder.itemView.getContext().getString(
                R.string.home_recipe_meta, recipe.ingredients.size()));
        holder.itemView.setContentDescription(recipe.name + ". "
                + holder.itemView.getContext().getString(R.string.home_all_ingredients));
        holder.itemView.setOnClickListener(view -> action.open(recipe.id));
    }

    @Override public int getItemCount() { return recipes.size(); }

    static final class Holder extends RecyclerView.ViewHolder {
        final TextView name, meta;
        Holder(View view) {
            super(view);
            name = view.findViewById(R.id.home_recipe_name);
            meta = view.findViewById(R.id.home_recipe_meta);
        }
    }
}
