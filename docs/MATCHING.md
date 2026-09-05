# Strict matching rules

RecipeMatcher requires every aggregated ingredient requirement to be met. It normalizes names, converts amounts into a base unit and sums duplicate pantry entries by normalized name and unit category. It also sums repeated requirements inside a recipe so the same stock cannot be counted twice. BigDecimal performs exact decimal multiplication, addition and comparison. Each suggestion is independently cookable; making one recipe does not reserve or deduct stock for another.

Mass: 1 kg = 1000 g. Volume: 1 l = 1000 ml; metric teaspoon = 5 ml; metric tablespoon = 15 ml. Count: item, piece and whole are equivalent for the same ingredient. No mass/volume/count conversion or food-density assumption is made. Unrecognized units cannot match.

Names are trimmed, repeated spaces collapsed and compared in lower case using Locale.ROOT. Documented plural words include eggs, onions, bananas, carrots, apples, mushrooms, lentils, chickpeas, peas, cucumbers, lemons, peppers, scallions, tomatoes and potatoes. Phrase words are normalized, so cooked chickpeas and sweet potatoes work too. Other words are retained rather than blindly stripping s (which would damage oats).

Aliases: yogurt/yoghurt; courgette/zucchini; cilantro/coriander; spring onion/scallion; rolled oats or oatmeal/oats; all-purpose flour or plain flour/flour. Cooked rice and dry rice remain distinct; cooked chickpeas and dry chickpeas remain distinct. Generic oil is not assumed to be olive oil. The alias list is deliberately small.

The pantry accepts quantities greater than zero through 1,000,000,000 with up to six decimal places and units selected from a fixed list. Recipe matching uses quantity and compatibility; expiry dates support display indicators and do not automatically deduct pantry stock. The user remains responsible for keeping actual stock current.

No partial recipes are shown. The empty Suggestions message is exactly: “No recipes match your pantry yet—add more ingredients.”
