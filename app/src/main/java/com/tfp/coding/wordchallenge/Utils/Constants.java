package com.tfp.coding.wordchallenge.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Constants {

    public static List<String> beginnerLevelWordList = Arrays.asList(
            "book", "door", "fish", "cake", "jump",
            "kite", "ring", "lamp", "tree", "boat",
            "ball", "rain", "frog", "sand", "card",
            "pink", "corn", "lion", "blue", "wolf",
            "star", "wind", "rock", "gold", "bear",
            "ship", "desk", "milk", "flag", "king",
            "hand", "leaf", "shoe", "duck", "sing",
            "nest", "coin", "lock", "gift", "song",
            "leaf", "frog", "bake", "food", "love",
            "cook", "read", "send", "walk", "play",
            "chair", "brush", "house", "plant", "water",
            "table", "piano", "dress", "blank", "track",
            "bread", "river", "glass", "shirt", "fruit",
            "clock", "grass", "grape", "stone", "horse",
            "snake", "mouse", "sweet", "chair", "brush",
            "stick", "sword", "beach", "crane", "swing",
            "train", "apple", "grill", "fruit", "bread",
            "truck", "phone", "floor", "shelf", "field",
            "storm", "child", "block", "crown", "trail",
            "shade", "light", "earth", "river", "space"
    );

    public static List<String> mediumLevelWordList = Arrays.asList(
            "animal", "bottle", "garden", "father", "yellow",
            "circle", "planet", "singer", "winter", "orange",
            "bridge", "pocket", "camera", "farmer", "forest",
            "guitar", "jacket", "flower", "pillow", "people",
            "balloon", "picture", "kitchen", "holiday", "teacher",
            "monster", "morning", "silence", "library", "blanket",
            "diamond", "bicycle", "weather", "cupcake", "drawing",
            "concert", "horizon", "airport", "builder", "brother",
            "notebook", "battery", "sunrise", "counter", "country",
            "package", "journey", "perfect", "village", "manager",
            "mountain", "sandwich", "elephant", "hospital", "football",
            "computer", "complete", "umbrella", "treasure", "friendly",
            "daughter", "backpack", "calendar", "festival", "haircut",
            "baseball", "distance", "firework", "football", "necklace",
            "activity", "birthday", "chicken", "champion", "sunshine",
            "fountain", "diamonds", "building", "triangle", "daylight",
            "elevator", "exercise", "director", "hospital", "medicine",
            "elephant", "sandwich", "umbrella", "resource", "strategy",
            "treasure", "fountain", "shoulder", "festival", "internet",
            "butterfly", "bathroom", "painting", "keyboard", "breakfast",
            "magazine", "explorer", "airplane", "pineapple", "chocolate"
    );

    public static List<String> advanceLevelWordList = Arrays.asList(
            "adventure", "discovery", "strawberry", "wonderland",
            "volleyball", "firefighter", "technology", "chocolate",
            "dangerous", "connection", "restaurant", "television",
            "government", "literature", "vegetables", "blackboard",
            "basketball", "notebook", "mountainous", "adrenaline",
            "happiness", "celebration", "experience", "invitation",
            "university", "challenges", "collection", "detectives",
            "conference", "electricity", "watermelon", "appearance",
            "landscapes", "directions", "friendship", "definition",
            "completion", "importance", "generation", "admissions",
            "chemistry", "historical", "recreation", "remarkable",
            "enthusiasm", "opposition", "motivation", "butterflies",
            "impossible", "reputation", "leadership", "statistics",
            "protection", "expression", "achievement", "confidence",
            "population", "difference", "foundation", "successful",
            "translator", "reflection", "management", "blueberries",
            "expression", "definition", "philosophy", "collection",
            "connection", "equipment", "independent", "television",
            "protection", "historical", "conference", "delightful",
            "musician", "negotiation", "procedure", "relaxation",
            "friendship", "correction", "department", "scientific",
            "invitation", "suggestion", "difference", "confidence",
            "chocolate", "conclusion", "blueprints", "successful",
            "fascinated", "blueberries", "headphones", "detectives",
            "satisfaction", "collection", "reflection", "importance"
    );

    public static String shuffleString(String input) {
        if (input == null || input.length() <= 1) {
            return input;  // Return the original string if it can't be shuffled
        }

        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }

        String shuffled;
        do {
            Collections.shuffle(characters);
            StringBuilder sb = new StringBuilder();
            for (char c : characters) {
                sb.append(c);
            }
            shuffled = sb.toString();
        } while (shuffled.equals(input)); // Ensure the shuffled string is different from the original

        return shuffled;
    }

    public static String getRandomItem(List<String> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(items.size());
        return items.get(randomIndex);
    }
}
