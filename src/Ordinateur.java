import java.util.Random;

public class Ordinateur extends Joueur {

    private final Random random;

    /**
     * Constructeur de Ordinateur.
     *
     * @param couleursPossiblesJeton Tableau des couleurs de jeton possible pour ce joueur
     */
    public Ordinateur (Couleur[] couleursPossiblesJeton)
    {
        super("Ordi");

        this.random = new Random();

        this.initCouleurJeton(couleursPossiblesJeton);
    }

    /**
     * Constructeur alternatif utilisé pour le chargement d'une partie à partir d'un fichier.
     *
     * @param nom           le nom du joueur
     * @param couleurJeton  La couleur du jeton du joueur
     * @param score         Le score du joueur
     */
    public Ordinateur (String nom, Couleur couleurJeton, int score) {
        super(nom, couleurJeton, score);

        this.random = new Random();
    }

    /**
     * Choisi aléatoirement une couleur de jeton pour le joueur parmis les couleurs disponibles.
     *
     * @param couleursPossiblesJeton Tableau des couleurs de jeton possible pour ce joueur
     */
    @Override
    protected void initCouleurJeton(Couleur[] couleursPossiblesJeton) {
        this.couleurJeton = couleursPossiblesJeton[random.nextInt(0, couleursPossiblesJeton.length)];
    }

    /**
     * Choisi aléatoirement une colonne de la grille pour jouer.
     *
     * @param nbColonne Le nombre de colonnes de la grille
     *
     * @return Le numéro de la colonne choisie
     */
    @Override
    public int choisirColonne(int nbColonne) {
        return random.nextInt(0, nbColonne);
    }

    /**
     * Retourne les données de l'instance
     *
     * @return Les données de l'instance
     */
    @Override
    public String toString() {
        return "Ordinateur{\n" +
                "nom=" + this.getNom() + "\n" +
                "couleurJeton=" + this.getCouleurJeton() + "\n" +
                "score=" + this.getScore() + "\n" +
                '}';
    }
}
