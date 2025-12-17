import components.anilibrary.AniLibrary;
import components.anilibrary.AniLibrary1L;

/**
 * Showcasing the usage of AniLibrary in a personal manner.
 *
 * @author Kevin Trinh
 *
 */
public final class AniLibraryPersonalDemo {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private AniLibraryPersonalDemo() {
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        AniLibrary lib = new AniLibrary1L();

        // Build a small library
        lib.addSeries("Attack on Titan", "Action");
        lib.addSeries("Haikyuu!!", "Sports");
        lib.addSeries("Your Lie in April", "Romance");
        lib.addSeries("Mob Psycho 100", "Action");

        System.out.println("== Full library ==");
        lib.displayAll();

        // Querying
        System.out.println("\nHas 'Haikyuu!!'? " + lib.hasSeries("Haikyuu!!"));
        System.out.println(
                "Genre of 'Mob Psycho 100': " + lib.genreOf("Mob Psycho 100"));
        System.out.println("Count(Action): " + lib.countByGenre("Action"));

        // Update
        System.out.println("\nUpdating 'Haikyuu!!' -> 'Slice of Life'");
        lib.updateGenre("Haikyuu!!", "Slice of Life");

        System.out.println("\n== By genre: Slice of Life ==");
        lib.displayByGenre("Slice of Life");

        System.out.println("\n== Final library ==");
        lib.displayAll();
    }
}
