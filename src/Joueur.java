public abstract class Joueur {
    private final String nom;
    protected Couleur couleurJeton;
    private int score;

    /**
     * Constructeur de la classe Joueur
     *
     * @param nom Le nom du joueur
     */
    public Joueur (String nom) {
        this.nom = nom;
        this.score = 0;
    }

    /**
     * Constructeur alternatif utilisé pour le chargement d'une partie à partir d'un fichier.
     *
     * @param nom           le nom du joueur
     * @param couleurJeton  La couleur du jeton du joueur
     * @param score         Le score du joueur
     */
    public Joueur (String nom, Couleur couleurJeton, int score) {
        this.nom = nom;
        this.couleurJeton = couleurJeton;
        this.score = score;
    }

    /**
     * Initialise la couleur du jeton du joueur.
     *
     * @param couleursPossiblesJeton Tableau des couleurs de jeton possible pour ce joueur
     */
    protected abstract void initCouleurJeton (Couleur[] couleursPossiblesJeton);

    /**
     * Retourne le score du joueur.
     *
     * @return le score du joueur
     */
    public int getScore ()
    {
        return this.score;
    }

    public void addOneToScore () {
        this.score++;
    }

    /**
     * Retourne le nom du joueur.
     *
     * @return le nom du joueur
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Retourne la ouleur des jetons du joueur.
     *
     * @return La couleur des jetons du joueur
     */
    public Couleur getCouleurJeton() {
        return couleurJeton;
    }

    /**
     * Fais jouer le joueur.
     *
     * @param nbColonne le nombre de colonnes de la grille
     *
     * @return le numero de la colonne où le joueur veut jouer
     */
    public int jouerJeton (int nbColonne)
    {
        return choisirColonne(nbColonne);
    }

    /**
     * Choisi la colonne où jouer.
     *
     * @param nbColonne Le nombre de colonnes de la grille
     *
     * @return Le numéro de la colonne choisie
     */
    protected abstract int choisirColonne (int nbColonne);
}
