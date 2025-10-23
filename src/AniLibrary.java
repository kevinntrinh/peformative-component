/**
 * Secondary interface for {@code AniLibraryKernel}.
 * Adds convenience and relational methods for managing anime entries.
 *
 * @mathsubtypes <pre>
 * ANILIBRARY_MODEL as defined in {@code AniLibraryKernel}
 * </pre>
 */
public interface AniLibrary extends AniLibraryKernel {

    /**
     * Updates the genre for a given anime title.
     *
     * @param title
     *            the anime title to update
     * @param newGenre
     *            the new genre for the anime
     * @updates this
     * @requires
     *  title is in the domain of this
     * @ensures
     *  this = (#this minus {(title, oldGenre)}) union {(title, newGenre)}
     */
    void updateGenre(String title, String newGenre);

    /**
     * Reports the number of anime that belong to the specified genre.
     *
     * @param genre
     *            the genre to count
     * @ensures
     *  countByGenre = the number of pairs (t, g) in this such that g = genre
     * @return the number of anime in the given genre
     */
    int countByGenre(String genre);

    /**
     * Combines {@code other} with this library.
     *
     * @param other
     *            the {@code AniLibrary} to be combined with this
     * @updates this
     * @clears other
     * @requires
     *  the domain of this and the domain of other are disjoint
     * @ensures
     *  this = #this union #other
     */
    void mergeWith(AniLibrary other);

    /**
     * Reports whether {@code this} and {@code other} share any titles.
     *
     * @param other
     *            the {@code AniLibrary} to compare with this
     * @ensures
     *  sharesTitleWith = (the domain of this) intersection (the domain of other) is not empty
     * @return true if any title exists in both libraries
     */
    boolean sharesTitleWith(AniLibrary other);

    /**
     * Displays all anime titles that belong to a specified genre.
     *
     * @param genre
     *            the genre to filter by
     * @ensures
     *  this = #this and
     *  every pair (t, g) displayed satisfies g = genre
     */
    void displayByGenre(String genre);

    /**
     * Displays all titles and genres in this library.
     *
     * @ensures
     *  this = #this and
     *  every pair (t, g) in this is displayed
     */
    void displayAll();

    /**
     * Removes all entries from this library.
     *
     * @clears this
     * @ensures
     *  this = {}
     */
    @Override
    void clear();
}
