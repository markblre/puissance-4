public class Humain extends Joueur {

    /**
     * Constructeur de Humain.
     *
     * @param couleursPossiblesJeton Tableau des couleurs de jeton possible pour ce joueur
     */
    public Humain (Couleur[] couleursPossiblesJeton)
    {
        super(System.console().readLine("Nom du joueur : "));

        this.initCouleurJeton(couleursPossiblesJeton);
    }

    /**
     * Constructeur alternatif utilisé pour le chargement d'une partie à partir d'un fichier.
     *
     * @param nom           le nom du joueur
     * @param couleurJeton  La couleur du jeton du joueur
     * @param score         Le score du joueur
     */
    public Humain (String nom, Couleur couleurJeton, int score) {
        super(nom, couleurJeton, score);
    }

    /**
     * Demande au joueur de choisir une couleur de jeton.
     *
     * @param couleursPossiblesJeton Tableau des couleurs de jeton possible pour ce joueur
     */
    @Override
    protected void initCouleurJeton(Couleur[] couleursPossiblesJeton) {
        System.out.println("Choisissez votre couleur...");

        // Affichage des couleurs disponibles
        for (int i = 0; i < couleursPossiblesJeton.length; i++)
        {
            System.out.println(i + " : " + couleursPossiblesJeton[i]);
        }

        while (true)
        {
            String entree = System.console().readLine("Index de la couleur : ");

            try {
                // Si l'entré n'est pas valide on redemande
                if ((Integer.parseInt(entree) >= 0) && (Integer.parseInt(entree) < couleursPossiblesJeton.length))
                {
                    this.couleurJeton = couleursPossiblesJeton[Integer.parseInt(entree)];
                    return;
                }
                else
                {
                    System.out.println("L'entrée est invalide (doit être un nombre entre 1 et " + couleursPossiblesJeton.length + ")");
                }
            }
            catch (NumberFormatException ex) {
                System.out.println("L'entrée est invalide (doit être un nombre entre 1 et " + couleursPossiblesJeton.length + ")");
            }
        }
    }

    /**
     * Demande au joueur d'entrer le numéro de la colonne où il veut jouer.
     *
     * @param nbColonne Le nombre de colonnes de la grille
     *
     * @return Le numéro de la colonne choisie
     */
    @Override
    public int choisirColonne(int nbColonne) {
        while (true)
        {
            String entree = System.console().readLine("Choix de la colonne : ");

            try {
                // Si l'entré n'est pas valide on redemande
                if ((Integer.parseInt(entree) >= 1) && (Integer.parseInt(entree) <= nbColonne))
                {
                    return (Integer.parseInt(entree) - 1);
                }
                else
                {
                    System.out.println("L'entrée est invalide : doit être un nombre entre 1 et " + nbColonne + ".");
                }
            }
            catch (NumberFormatException ex) {
                System.out.println("L'entrée est invalide : doit être un nombre entre 1 et " + nbColonne + ".");
            }
        }
    }

    /**
     * Retourne les données de l'instance
     *
     * @return Les données de l'instance
     */
    @Override
    public String toString() {
        return "Humain{\n" +
                "nom=" + this.getNom() + "\n" +
                "couleurJeton=" + this.getCouleurJeton() + "\n" +
                "score=" + this.getScore() + "\n" +
                '}';
    }
}
