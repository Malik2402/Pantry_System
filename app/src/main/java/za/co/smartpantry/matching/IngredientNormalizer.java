package za.co.smartpantry.matching;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class IngredientNormalizer {
    private static final Map<String, String> WORDS = new HashMap<>();
    static {
        for (String word : new String[]{"egg", "onion", "banana", "carrot", "apple", "mushroom",
                "lentil", "chickpea", "pea", "cucumber", "lemon", "pepper", "scallion"})
            WORDS.put(word + "s", word);
        WORDS.put("tomatoes", "tomato");
        WORDS.put("potatoes", "potato");
        WORDS.put("yogurt", "yoghurt");
        WORDS.put("yoghurts", "yoghurt");
        WORDS.put("yogurts", "yoghurt");
        WORDS.put("courgettes", "zucchini");
        WORDS.put("courgette", "zucchini");
        WORDS.put("cilantro", "coriander");
    }

    private IngredientNormalizer() {}

    public static String normalize(String name) {
        if (name == null) return "";
        String clean = name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if (clean.isEmpty()) return "";
        String[] words = clean.split(" ");
        for (int i = 0; i < words.length; i++) words[i] = WORDS.getOrDefault(words[i], words[i]);
        clean = String.join(" ", words);
        if (clean.equals("spring onion")) return "scallion";
        if (clean.equals("rolled oats") || clean.equals("oatmeal")) return "oats";
        if (clean.equals("all-purpose flour") || clean.equals("plain flour")) return "flour";
        return clean;
    }
}
