/**
 * Figure Klasse
 * <p>
 * Beinhaltet die unterschiedlichen Figuren die dem Spielfeld hinzugefügt werden können.
 * Diese haben eine boolean matrix die angeben welche Zellen leben (true) oder tot (false) sind.
 *
 * @author Felix Ruess (199261)
 * @author Lukas Reichert (199034)
 * @author Peter Tim Oliver Nauroth (198322)
 * @version 1.0.0
 */
public enum Figure {
    GLIDER(new boolean[][]{{false, true, false}, {false, false, true}, {true, true, true}}),
    GLIDER_CANON(new boolean[][]{
            {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, true, true},
            {false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, true, false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, true, true},
            {true, true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true, true, false, false, false, false, false, false, false, false, true, false, false, false, true, false, true, true, false, false, false, false, true, false, true, false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}
    });

    private final boolean[][] mapping;

    /**
     * Konstruktor.
     *
     * @param mapping {boolean[][]}
     */
    Figure(boolean[][] mapping) {
        this.mapping = mapping;
    }

    /**
     * Gibt ein "Alive - Dead" boolean mapping zurueck.
     *
     * @return {boolean[][]}
     */
    public boolean[][] getMapping() {
        return mapping;
    }
}
