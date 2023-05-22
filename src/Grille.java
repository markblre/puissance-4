public class Grille
{

    private Case premiereCase;
    private final int nbLigne;
    private final int nbColonne;

    private Case[] indexCasesVide;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    /**
     * Constructeur de la classe Grille
     * 
     * @param nbLigne   Nombre de lignes de la grille
     * @param nbColonne Nombre de colonnes de la grille
     */
    public Grille (int nbLigne, int nbColonne)
    {
        this.nbLigne = nbLigne;
        this.nbColonne = nbColonne;
        this.initGrille();
    }

    /**
     * Constructeur alternatif de la classe Grille
     *
     * @param nbLigne       Nombre de lignes de la grille
     * @param nbColonne     Nombre de colonnes de la grille
     * @param jetonsCases   Tableaux contenant les couleurs des jetons dans les cases de la grille (de bas en haut et de gauche à droite)
     */
    public Grille (int nbLigne, int nbColonne, Couleur[] jetonsCases)
    {
        this.nbLigne = nbLigne;
        this.nbColonne = nbColonne;
        this.initGrille();

        for (int i = 0; i < jetonsCases.length; i++)
        {
            if (jetonsCases[i] != Couleur.VIDE)
            {
                this.addJeton((i % this.nbColonne), jetonsCases[i]);
            }
        }
    }

    /**
     * Retourne su la grille est pleine ou non.
     *
     * @return  true si la grille est pleine
     *          false sinon
     */
    public boolean estPleine ()
    {
        // Si le tableau indexCasesVide ne contient plus aucune référence vers une case libre, alors la grille est pleine
        for (Case c : indexCasesVide)
        {
            if (c != null)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Initialise la grille en liens les cases avec leurs voisins
     *
     * @author Mark et Loïs (créateurs de la méthode :))
     */
    private void initGrille()
    {
        int nbTotalcase = this.nbLigne * this.nbColonne;

        Case[] cases = new Case[nbTotalcase];

        // Création du nombre de cases nécessaires pour remplir la grille
        for (int i = 0; i < nbTotalcase; i++)
        {
            cases[i] = new Case();
        }

        // Première ligne
        for (int i = 0; i < this.nbColonne; i++)
        {
            Case voisinBas, voisinDroite, voisinGauche;

            // les voisins bas des cases de la première ligne sont les cases à (i + nbColonne) dans le tableau de cases.
            voisinBas = cases[i + this.nbColonne];

            if (i == 0)
            {
                // La première case de la ligne n'a pas de voisin gauche
                voisinDroite = cases[i + 1];
                voisinGauche = null;
            }
            else if (i == (this.nbColonne - 1))
            {
                // La dernière case de la ligne n'a pas de voisin droit
                voisinDroite = null;
                voisinGauche = cases[i - 1];
            }
            else
            {
                voisinDroite = cases[i + 1];
                voisinGauche = cases[i - 1];
            }

            // Les cases de la première ligne n'ont pas de voisin haut
            cases[i].linkCase(null, voisinBas, voisinDroite, voisinGauche);
        }

        // Lignes centrales
        for (int i = this.nbColonne; i < (nbTotalcase - this.nbColonne); i++)
        {
            Case voisinHaut, voisinBas, voisinDroite, voisinGauche;

            // les voisins haut et bas des cases de la première ligne sont les cases à (i - nbColonne) et (i + nbColonne) dans le tableau de cases.
            voisinHaut = cases[i - this.nbColonne];
            voisinBas = cases[i + this.nbColonne];

            if ((i % this.nbColonne) == 0)
            {
                // La première case de la ligne n'a pas de voisin gauche
                voisinDroite = cases[i + 1];
                voisinGauche = null;
            }
            else if ((i % this.nbColonne) == (this.nbColonne - 1))
            {
                // La dernière case de la ligne n'a pas de voisin droit
                voisinDroite = null;
                voisinGauche = cases[i - 1];
            }
            else
            {
                voisinDroite = cases[i + 1];
                voisinGauche = cases[i - 1];
            }

            cases[i].linkCase(voisinHaut, voisinBas, voisinDroite, voisinGauche);
        }


        // Dernière ligne
        for (int i = (nbTotalcase - this.nbColonne); i < nbTotalcase; i++)
        {
            Case voisinHaut, voisinDroite, voisinGauche;

            // les voisins haut des cases de la dernière ligne sont les cases à (i - nbColonne) dans le tableau de cases.
            voisinHaut = cases[i - this.nbColonne];

            if(i == (nbTotalcase - this.nbColonne))
            {
                // La première case de la ligne n'a pas de voisin gauche
                voisinDroite = cases[i + 1];
                voisinGauche = null;
            }
            else if (i == (nbTotalcase - 1))
            {
                // La dernière case de la ligne n'a pas de voisin droit
                voisinDroite = null;
                voisinGauche = cases[i - 1];
            }
            else
            {
                voisinDroite = cases[i + 1];
                voisinGauche = cases[i - 1];
            }

            // Les cases de la dernière ligne n'ont pas de voisin bas
            cases[i].linkCase(voisinHaut, null, voisinDroite, voisinGauche);
        }

        // Initialisation de indexCasesVide
        this.indexCasesVide = new Case[this.nbColonne];

        // La case vide de la première colonne est la première case de la dernière ligne
        this.indexCasesVide[0] = cases[nbTotalcase - this.nbColonne];
        // Les cases vides des autres colonnes sont les voisins droit de la colonne précédente
        for (int i = 1; i < this.nbColonne; i++)
        {
            this.indexCasesVide[i] = this.indexCasesVide[i - 1].getVoisin(Direction.DROITE);
        }

        this.premiereCase = cases[0];
    }

    /**
     * Affiche la grille dans le terminal.
     */
    public void afficherGrille ()
    {
        Case    caseCourante,
                premiereCaseLigne = this.premiereCase;
        StringBuilder strLigne = new StringBuilder();

        // On parcourt la grille de haut en bas et de gauche à droite
        for (int i = 0; i < this.nbLigne; i++)
        {
            System.out.println("----------------------------");
            caseCourante = premiereCaseLigne;
            for (int j = 0; j < this.nbColonne; j++)
            {
                // On affiche un "o" de la couleur du jeton dans la case courante
                // On affiche un " " si aucun jeton n'est dans la case courante
                switch (caseCourante.getColor())
                {
                    case JAUNE -> strLigne.append("|" + ANSI_YELLOW + " o " + ANSI_RESET);
                    case ROUGE -> strLigne.append("|" + ANSI_RED + " o " + ANSI_RESET);
                    case BLEU -> strLigne.append("|" + ANSI_BLUE + " o " + ANSI_RESET);
                    case VIDE -> strLigne.append("|   ");
                }

                // Si on ne traite pas la dernière case de la ligne, on passe au voisin de droite
                if (j != (this.nbColonne - 1))
                {
                    caseCourante = caseCourante.getVoisin(Direction.DROITE);
                }
            }
            strLigne.append("|");

            System.out.println(strLigne); // Affichage de la ligne

            strLigne.delete(0, strLigne.length()); // Réinitialisation de strLigne

            // Si on ne traite pas la dernière ligne de la grille, on passe au voisin du bas
            if (i != (this.nbLigne - 1))
            {
                premiereCaseLigne = premiereCaseLigne.getVoisin(Direction.BAS);
            }
        }
        System.out.println("----------------------------");
    }

    /**
     * Vide les cases de la grille de tout les jetons
     */
    public void viderGrille ()
    {
        Case    caseCourante,
                premiereCaseLigne = this.premiereCase;

        // On parcourt la grille de haut en bas et de gauche à droite
        for (int i = 0; i < this.nbLigne; i++)
        {
            caseCourante = premiereCaseLigne;
            for (int j = 0; j < this.nbColonne; j++)
            {
                caseCourante.viderCase();

                // Si on ne traite pas la dernière case de la ligne, on passe au voisin de droite
                if (j != (this.nbColonne - 1))
                {
                    caseCourante = caseCourante.getVoisin(Direction.DROITE);
                }
            }

            // Si on ne traite pas la dernière ligne de la grille, on passe au voisin du bas
            if (i != (this.nbLigne - 1))
            {
                premiereCaseLigne = premiereCaseLigne.getVoisin(Direction.BAS);
            }
        }

        // On reinitialise indexCasesVide
        this.indexCasesVide[0] = premiereCaseLigne;
        for (int i = 1; i < this.nbColonne; i++)
        {
            this.indexCasesVide[i] = this.indexCasesVide[i - 1].getVoisin(Direction.DROITE);
        }
    }

    /**
     * Ajoute un jeton à la colonne renseignée.
     *
     * @param colonne   La colonne où il faut ajouter un jeton
     * @param couleur   La couleur du jeton à ajouter
     *
     * @return La case où le jeton a été ajouté
     */
    public Case addJeton (int colonne, Couleur couleur)
    {
        // Si la colonne est pleine on lève une exception qui sera récupérée
        if (this.indexCasesVide[colonne] != null)
        {
            Case returnCase = this.indexCasesVide[colonne];
            this.indexCasesVide[colonne].addJeton(couleur);
            this.indexCasesVide[colonne].addJeton(couleur);
            if (this.indexCasesVide[colonne].getVoisin(Direction.HAUT) != null)
            {
                this.indexCasesVide[colonne] = this.indexCasesVide[colonne].getVoisin(Direction.HAUT);
            }
            else
            {
                this.indexCasesVide[colonne] = null;
            }

            return returnCase;
        }
        else
        {
            throw new theColumnIsFull();
        }
    }

    /**
     * Retourne les données de l'instance
     *
     * @return Les données de l'instance
     */
    @Override
    public String toString() {
        StringBuilder strGrilleCases = new StringBuilder(); // Suite des couleurs des cases de la grille

        strGrilleCases.append("Cases{\n");

        Case    caseCourante,
                premiereCaseLigne = this.premiereCase;

        // On commence par la dernière ligne
        for (int i = 1; i < this.nbLigne; i++)
        {
            premiereCaseLigne = premiereCaseLigne.getVoisin(Direction.BAS);
        }

        // On parcours les cases du bas vers le haut (plus pratique pour le chargement plus tard)
        for (int i = (this.nbLigne - 1); i >= 0; i--)
        {
            caseCourante = premiereCaseLigne;
            for (int j = 0; j < this.nbColonne; j++)
            {
                strGrilleCases.append(caseCourante.getColor()).append("\n");

                // Si on ne traite pas la dernière case de la ligne, on passe au voisin de droite
                if (j != (this.nbColonne - 1))
                {
                    caseCourante = caseCourante.getVoisin(Direction.DROITE);
                }
            }

            // Si on ne traite pas la dernière ligne de la grille, on passe au voisin du haut
            if (i != 0)
            {
                premiereCaseLigne = premiereCaseLigne.getVoisin(Direction.HAUT);
            }
        }

        strGrilleCases.append("}");


        return "Grille{\n" +
                "nbLigne=" + this.nbLigne + "\n" +
                "nbColonne=" + this.nbColonne + "\n" +
                strGrilleCases.toString() + "\n" +
                '}';
    }
}
