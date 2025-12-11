package components.anilibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit test fixture for {@code AniLibrary}'s secondary methods.
 *
 * @author Kevin Trinh
 */
public abstract class AniLibrarySecondaryTest {

    /**
     * Invokes the appropriate {@code AniLibrary} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new anilibrary
     * @ensures constructorTest = 0
     */
    protected abstract AniLibrary constructorTest();

    /**
     * Build a library from alternating title/genre pairs.
     *
     * @param pairs
     *            alternating sequence: title, genre, title, genre, ...
     * @return a new {@code AniLibrary} containing exactly the given pairs
     * @requires pairs.length is even and all titles are unique
     * @ensures result contains each (title, genre) from {@code pairs}
     */
    private AniLibrary build(String... pairs) {
        assert pairs.length % 2 == 0 : "pairs must be even length";
        AniLibrary lib = this.constructorTest();
        for (int i = 0; i < pairs.length; i += 2) {
            assert !lib.hasSeries(pairs[i]) : "duplicate title in args";
            lib.addSeries(pairs[i], pairs[i + 1]);
        }
        return lib;
    }

    /**
     * Take a non-destructive snapshot of a library’s contents.
     *
     * @param lib
     *            the library to snapshot
     * @return a map title -> genre representing {@code lib}
     * @ensures lib is restored to its original state
     */
    private java.util.Map<String, String> snapshot(AniLibrary lib) {
        java.util.Map<String, String> m = new HashMap<>();
        AniLibrary hold = lib.newInstance();
        while (lib.size() > 0) {
            String[] e = lib.removeAny();
            m.put(e[0], e[1]);
            hold.addSeries(e[0], e[1]);
        }
        while (hold.size() > 0) {
            String[] e = hold.removeAny();
            lib.addSeries(e[0], e[1]);
        }
        return m;
    }

    /**
     * Assert that the library contents equal the expected map.
     *
     * @param expected
     *            expected title->genre
     * @param actual
     *            the library to compare
     */
    private void assertEqualsMap(java.util.Map<String, String> expected,
            AniLibrary actual) {
        assertEquals(expected, this.snapshot(actual));
    }

    /**
     * Testing: updateGenre replaces the genre only for the specified title.
     */
    @Test
    public void testUpdateGenre() {
        AniLibrary lib = this.build("AOT", "Action", "Haikyuu", "Sports");
        lib.updateGenre("Haikyuu", "Slice");
        Map<String, String> exp = new HashMap<>();
        exp.put("AOT", "Action");
        exp.put("Haikyuu", "Slice");
        this.assertEqualsMap(exp, lib);
    }

    /**
     * Testing: countByGenre returns the correct counts for existing and missing
     * genres.
     */
    @Test
    public void testCountByGenre() {
        AniLibrary lib = this.build("A", "X", "B", "Y", "C", "X");
        assertEquals(2, lib.countByGenre("X"));
        assertEquals(1, lib.countByGenre("Y"));
        assertEquals(0, lib.countByGenre("Z"));
    }

    /**
     * Testing: mergeWith moves all pairs from other into this and clears other.
     */
    @Test
    public void testMergeWith() {
        AniLibrary a = this.build("A", "X");
        AniLibrary b = this.build("B", "Y");
        a.mergeWith(b);
        Map<String, String> exp = new HashMap<>();
        exp.put("A", "X");
        exp.put("B", "Y");
        this.assertEqualsMap(exp, a);
        assertEquals(0, b.size());
    }

    /**
     * Testing: sharesTitleWith returns true when libraries share at least one
     * title.
     */
    @Test
    public void testSharesTitleWith() {
        AniLibrary a = this.build("A", "X", "B", "Y");
        AniLibrary b = this.build("C", "Z", "B", "Q");
        assertTrue(a.sharesTitleWith(b));
    }

    /**
     * Testing: displayByGenre prints only titles with the requested genre and
     * is non-destructive.
     */
    @Test
    public void testDisplayByGenreOutputsOnlyRequestedGenre() {
        AniLibrary lib = this.build("AOT", "Action", "Haikyuu", "Sports", "OPM",
                "Action");
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream oldOut = System.out;
        System.setOut(new java.io.PrintStream(baos));
        try {
            lib.displayByGenre("Action");
        } finally {
            System.setOut(oldOut);
        }
        String out = baos.toString();
        assertTrue(out.contains("AOT") && out.contains("Action"));
        assertTrue(out.contains("OPM") && out.contains("Action"));
        assertTrue(!out.contains("Haikyuu"));
        java.util.Map<String, String> exp = new java.util.HashMap<>();
        exp.put("AOT", "Action");
        exp.put("Haikyuu", "Sports");
        exp.put("OPM", "Action");
        this.assertEqualsMap(exp, lib);
    }

    /**
     * Testing: displayAll prints every (title, genre) pair and is
     * non-destructive.
     */
    @Test
    public void testDisplayAllPrintsEverything() {
        AniLibrary lib = this.build("A", "X", "B", "Y");
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream oldOut = System.out;
        System.setOut(new java.io.PrintStream(baos));
        try {
            lib.displayAll();
        } finally {
            System.setOut(oldOut);
        }
        String out = baos.toString();
        assertTrue(out.contains("A") && out.contains("X"));
        assertTrue(out.contains("B") && out.contains("Y"));
        java.util.Map<String, String> exp = new java.util.HashMap<>();
        exp.put("A", "X");
        exp.put("B", "Y");
        this.assertEqualsMap(exp, lib);
    }

    /**
     * Testing: equals returns true for two libraries with identical contents
     * (order-insensitive).
     */
    @Test
    public void testEqualsTrueSameContentDifferentOrder() {
        AniLibrary a = this.build("A", "X", "B", "Y", "C", "Z");
        AniLibrary b = this.build("C", "Z", "B", "Y", "A", "X");
        assertTrue(a.equals(b));
    }

    /**
     * Testing: equals returns false when contents differ.
     */
    @Test
    public void testEqualsFalseDifferentContent() {
        AniLibrary a = this.build("A", "X", "B", "Y");
        AniLibrary b = this.build("A", "X", "B", "Q");
        assertTrue(!a.equals(b));
    }

    /**
     * Testing: hashCode is consistent with equals.
     */
    @Test
    public void testHashCodeConsistentWithEquals() {
        AniLibrary a = this.build("A", "X", "B", "Y");
        AniLibrary b = this.build("B", "Y", "A", "X");
        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());
    }

    /**
     * Testing: toString prints all (title, genre) pairs in braces and is
     * non-destructive.
     */
    @Test
    public void testToStringFormatAndNonDestructive() {
        AniLibrary lib = this.build("A", "X", "B", "Y");
        String s = lib.toString();
        assertTrue(s.startsWith("{") && s.endsWith("}"));
        assertTrue(s.contains("(A, X)") && s.contains("(B, Y)"));
        java.util.Map<String, String> exp = new java.util.HashMap<>();
        exp.put("A", "X");
        exp.put("B", "Y");
        this.assertEqualsMap(exp, lib);
    }
}
