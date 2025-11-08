/**
 * Layered implementations of secondary methods for {@code AniLibrary}.
 *
 * All methods are implemented strictly using kernel + {@code Standard} methods.
 */
public abstract class AniLibrarySecondary implements AniLibrary {

    /*
     * Common methods (from Object)
     * --------------------------------------------------------------------
     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        AniLibrary hold = this.newInstance();
        boolean first = true;

        while (this.size() > 0) {
            String[] e = this.removeAny();
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append("(").append(e[0]).append(", ").append(e[1]).append(")");
            hold.addSeries(e[0], e[1]);
        }
        while (hold.size() > 0) {
            String[] e = hold.removeAny();
            this.addSeries(e[0], e[1]);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        boolean result = true;
        if (o == null || o.getClass() != this.getClass()) {
            result = false;
        } else {
            AniLibrary other = (AniLibrary) o;

            if (o == this) {
                result = true;
            } else if (this.size() != other.size()) {
                result = false;
            } else {
                AniLibrary hold = this.newInstance();
                while (result && this.size() > 0) {
                    String[] e = this.removeAny();
                    hold.addSeries(e[0], e[1]);

                    if (!other.hasSeries(e[0])) {
                        result = false;
                    } else {
                        String g2 = other.genreOf(e[0]);
                        if (!e[1].equals(g2)) {
                            result = false;
                        }
                    }
                }
                while (hold.size() > 0) {
                    String[] e = hold.removeAny();
                    this.addSeries(e[0], e[1]);
                }
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int h = 1;
        AniLibrary hold = this.newInstance();

        while (this.size() > 0) {
            String[] e = this.removeAny();
            h = prime * h + e[0].hashCode();
            h = prime * h + e[1].hashCode();
            hold.addSeries(e[0], e[1]);
        }
        while (hold.size() > 0) {
            String[] e = hold.removeAny();
            this.addSeries(e[0], e[1]);
        }
        return h;
    }

    @Override
    public void updateGenre(String title, String newGenre) {
        assert this.hasSeries(
                title) : "Violation of: title is in the domain of this";

        AniLibrary hold = this.newInstance();
        boolean updated = false;

        while (this.size() > 0) {
            String[] e = this.removeAny();
            if (!updated && e[0].equals(title)) {
                hold.addSeries(title, newGenre);
                updated = true;
            } else {
                hold.addSeries(e[0], e[1]);
            }
        }
        while (hold.size() > 0) {
            String[] e = hold.removeAny();
            this.addSeries(e[0], e[1]);
        }
    }

    @Override
    public int countByGenre(String genre) {
        int count = 0;
        AniLibrary hold = this.newInstance();

        while (this.size() > 0) {
            String[] e = this.removeAny();
            if (e[1].equals(genre)) {
                count++;
            }
            hold.addSeries(e[0], e[1]);
        }
        while (hold.size() > 0) {
            String[] e = hold.removeAny();
            this.addSeries(e[0], e[1]);
        }
        return count;
    }

    @Override
    public void mergeWith(AniLibrary other) {
        assert other != null : "Violation of: other is not null";
        assert !this.sharesTitleWith(
                other) : "Violation of: the domain of this and other are disjoint";

        while (other.size() > 0) {
            String[] e = other.removeAny();
            this.addSeries(e[0], e[1]);
        }
    }

    @Override
    public boolean sharesTitleWith(AniLibrary other) {
        assert other != null : "Violation of: other is not null";

        boolean shares = false;
        AniLibrary hold = this.newInstance();

        while (!shares && this.size() > 0) {
            String[] e = this.removeAny();
            if (other.hasSeries(e[0])) {
                shares = true;
            }
            hold.addSeries(e[0], e[1]);
        }
        while (hold.size() > 0) {
            String[] e = hold.removeAny();
            this.addSeries(e[0], e[1]);
        }
        return shares;
    }

    @Override
    public void displayByGenre(String genre) {
        AniLibrary hold = this.newInstance();

        while (this.size() > 0) {
            String[] e = this.removeAny();
            if (e[1].equals(genre)) {
                System.out.println(e[0] + " : " + e[1]);
            }
            hold.addSeries(e[0], e[1]);
        }
        while (hold.size() > 0) {
            String[] e = hold.removeAny();
            this.addSeries(e[0], e[1]);
        }
    }

    @Override
    public void displayAll() {
        AniLibrary hold = this.newInstance();

        while (this.size() > 0) {
            String[] e = this.removeAny();
            System.out.println(e[0] + " : " + e[1]);
            hold.addSeries(e[0], e[1]);
        }
        while (hold.size() > 0) {
            String[] e = hold.removeAny();
            this.addSeries(e[0], e[1]);
        }
    }
}
