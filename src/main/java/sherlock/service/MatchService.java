package sherlock.service;

import sherlock.db.ItemDao;
import sherlock.model.Item;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Contains the auto-matching algorithm.
 * Compares a new item against opposite-type items to find the best match.
 */
public class MatchService {

    // Common words to ignore when comparing descriptions.
    private static final Set<String> STOP_WORDS = Set.of(
            "the", "a", "an", "with", "and", "or", "in", "on", "at", "to",
            "for", "of", "is", "it", "my", "was", "i", "this", "that"
    );

    private final ItemDao dao = new ItemDao();

    /**
     * Tries to match a newly reported item with an existing opposite-type item.
     * Returns the 6-character token if a match is found, or null otherwise.
     *
     * How it works:
     *   1. Get keywords from the new item (name + description).
     *   2. For each candidate of the opposite type, compute a similarity score.
     *   3. Score = % of shared keywords + 15 bonus if same location (cap 100).
     *   4. If the best score >= 75 → mark both as MATCHED, generate a token.
     */
    public String tryMatch(Item newItem) throws SQLException {
        // LOST items look for FOUND candidates, and vice versa.
        String oppositeType = newItem.getType().equals("LOST") ? "FOUND" : "LOST";
        List<Item> candidates = dao.findByStatusAndType(oppositeType, oppositeType);

        Set<String> newWords = extractKeywords(newItem.getName() + " " + newItem.getDescription());
        if (newWords.isEmpty()) return null;

        Item bestMatch = null;
        int bestScore = 0;

        // Compare the new item against every candidate.
        for (Item candidate : candidates) {
            Set<String> candidateWords = extractKeywords(
                    candidate.getName() + " " + candidate.getDescription());
            if (candidateWords.isEmpty()) continue;

            // Count how many keywords appear in both items.
            Set<String> common = new HashSet<>(newWords);
            common.retainAll(candidateWords);

            // Score = percentage of the smaller set that overlaps.
            int smaller = Math.min(newWords.size(), candidateWords.size());
            int score = (int) ((common.size() * 100.0) / smaller);

            // +15 bonus points if both items are at the same campus location.
            if (newItem.getLocation().equalsIgnoreCase(candidate.getLocation())) {
                score += 15;
            }
            score = Math.min(score, 100);   // cap at 100

            if (score > bestScore) {
                bestScore = score;
                bestMatch = candidate;
            }
        }

        // Only accept a match if the score is high enough.
        if (bestScore >= 75 && bestMatch != null) {
            String token = generateToken();
            dao.setMatched(newItem.getId(), bestMatch.getId(), token);
            return token;
        }
        return null;
    }

    // Splits text into lowercase words, removes punctuation and stop words.
    private Set<String> extractKeywords(String text) {
        Set<String> keywords = new HashSet<>();
        for (String word : text.toLowerCase().split("\\s+")) {
            String clean = word.replaceAll("[^a-z0-9]", "");
            if (!clean.isEmpty() && !STOP_WORDS.contains(clean)) {
                keywords.add(clean);
            }
        }
        return keywords;
    }

    // Generates a random 6-character token (letters + digits, no ambiguous chars).
    private String generateToken() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
