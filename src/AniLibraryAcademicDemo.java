import components.anilibrary.AniLibrary;
import components.anilibrary.AniLibrary1L;

/**
 * Showcasing the usage of AniLibrary in an academic manner.
 *
 * @author Kevin Trinh
 *
 */
public final class AniLibraryAcademicDemo {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private AniLibraryAcademicDemo() {
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        // Two separate collections (e.g., two club officers)
        AniLibrary kevins = new AniLibrary1L();
        kevins.addSeries("Violet Evergarden", "Drama");
        kevins.addSeries("Jujutsu Kaisen", "Action");

        AniLibrary jamies = new AniLibrary1L();
        jamies.addSeries("Spy x Family", "Comedy");
        jamies.addSeries("Sousou no Frieren", "Fantasy");

        System.out.println("Kevin's library:");
        kevins.displayAll();
        System.out.println("\nJamie's library:");
        jamies.displayAll();

        // Check overlap before merge (should be false here)
        System.out.println(
                "\nShares any title? " + kevins.sharesTitleWith(jamies));

        // Merge Jamie's into Kevin's (and clear Jamie's)
        kevins.mergeWith(jamies);

        System.out.println("\nAfter merge, Kevin's library:");
        kevins.displayAll();
        System.out.println(
                "\nJamie's library size (should be 0): " + jamies.size());

        // Remove a title and show result
        System.out.println("\nRemoving 'Spy x Family'...");
        String removedGenre = kevins.removeSeries("Spy x Family");
        System.out.println("Removed genre: " + removedGenre);

        System.out.println("\nFinal Kevin library:");
        kevins.displayAll();
    }
}
