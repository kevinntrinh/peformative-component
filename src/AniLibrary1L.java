import components.map.Map;
import components.map.Map1L;

public class AniLibrary1L {

    private Map<String, String> library;

    public AniLibrary1L() {
        this.library = new Map1L<>();
    }

    public void addSeries(String title, String genre) {
        this.library.add(title, genre);
    }

    public String removeSeries(String title) {
        Map.Pair<String, String> removedPair = this.library.remove(title);
        return removedPair.value();
    }

    public boolean hasSeries(String title) {
        return this.library.hasKey(title);
    }

    public int size() {
        return this.library.size();
    }

    public void updateGenre(String title, String newGenre) {
        if (this.library.hasKey(title)) {
            this.library.replaceValue(title, newGenre);
        }
    }

    public void displayByGenre(String genre) {
        System.out.println("--- Series in genre: " + genre + " ---");
        for (Map.Pair<String, String> p : this.library) {
            if (p.value().equalsIgnoreCase(genre)) {
                System.out.println(p.key());
            }
        }
    }

    public void displayAll() {
        System.out.println("--- AniLibrary Contents ---");
        for (Map.Pair<String, String> p : this.library) {
            System.out.println(p.key() + " (" + p.value() + ")");
        }
    }

    public static void main(String[] args) {
        AniLibrary1L myLibrary = new AniLibrary1L();

        myLibrary.addSeries("Attack on Titan", "Action");
        myLibrary.addSeries("Your Lie in April", "Romance");
        myLibrary.addSeries("Haikyuu!!", "Sports");

        System.out.println("Library size: " + myLibrary.size());
        System.out.println("Has Haikyuu?: " + myLibrary.hasSeries("Haikyuu!!"));

        myLibrary.displayByGenre("Action");

        myLibrary.updateGenre("Haikyuu!!", "Slice of Life");

        myLibrary.displayAll();

        myLibrary.removeSeries("Your Lie in April");
        System.out.println("Library size after removal: " + myLibrary.size());
    }
}
