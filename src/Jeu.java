import java.io.*;
import java.util.ArrayList;

public class Jeu {
    private Grille grille;
    private final Joueur[] joueurs;

    private int nbCoup;
    private int nbPartie;
    private int nbColonne;


    /**
     * Constructeur de la classe Jeu.
     *
     * @param nbLigne   Nombre de lignes de la grille
     * @param nbColonne Nombre de colonnes de la grille
     */
    public Jeu (int nbLigne, int nbColonne)
    {
        this.joueurs = new Joueur[2];

        while (true)
        {
            String entree = System.console().readLine("Voulez-vous charger une partie ? o/n\n");

            if (entree.equals("o"))
            {
                this.load(System.console().readLine("Chemin du fichier de sauvegarde (save.txt) : "));
                break;
            }
            if (entree.equals("n"))
            {
                this.nouveauJeu(nbLigne, nbColonne);
                break;
            }
            else
            {
                System.out.println("Entrée invalide");
            }
        }
    }

    /**
     * Permet de créer un nouveau jeu.
     *
     * @param nbLigne   Nombre de lignes de la grille
     * @param nbColonne Nombre de colonnes de la grille
     */
    private void nouveauJeu (int nbLigne, int nbColonne)
    {
        this.grille = new Grille(nbLigne, nbColonne);

        this.nbCoup = 1;
        this.nbPartie = 1;

        ArrayList<Couleur> couleursPossiblesJeton = new ArrayList<>();

        // On ajoute les couleur de l'énumération au tableau des couleurs disponibles (sauf VIDE)
        for (Couleur couleur : Couleur.values())
        {
            if (couleur != Couleur.VIDE)
            {
                couleursPossiblesJeton.add(couleur);
            }
        }

        // Création du joueur 1
        this.joueurs[0] = new Humain(couleursPossiblesJeton.toArray(new Couleur[0]));
        couleursPossiblesJeton.remove(this.joueurs[0].getCouleurJeton()); // Suppression de la couleur choisie des couleurs disponibles

        // Création du joueur 2
        this.joueurs[1] = new Ordinateur(couleursPossiblesJeton.toArray(new Couleur[0]));

        this.nbColonne = nbColonne;
    }

    /**
     * Réinitialise la grille et les variables necessaire pour une nouvelle partie.
     */
    private void nouvellePartie ()
    {
        this.grille.viderGrille();
        this.nbCoup = 1;
        this.nbPartie++;
    }

    /**
     * Lance le déroulement de la partie.
     */
    public void jouerPartie ()
    {
        this.grille.afficherGrille();

        // Tant qu'il n'y a pas de gagnant les joueurs jouent
        Joueur winner;
        while ((winner = this.jouerCoup(this.joueurs[(nbCoup - (nbPartie % 2)) % 2])) == null)
        {
            if (this.grille.estPleine())
            {
                // Si la grille est pleine on arrête la partie.
                System.out.println("La grille est pleine.\nAucun gagnant.");
                break;
            }
            nbCoup++;
        }

        // S'il y a un gagnant on lui ajoute un point
        if (winner != null)
        {
            winner.addOneToScore();
            System.out.println(winner.getNom() + " gagne la partie!");
        }

        System.out.println("Scores :");
        for (Joueur joueur : this.joueurs)
        {
            System.out.println(joueur.getNom() + " : " + joueur.getScore() + " points.");
        }


        // Fin de partie
        while (true)
        {
            String entree = System.console().readLine("Nouvelle partie ? o/n\n");

            if (entree.equals("o"))
            {
                this.nouvellePartie();
                this.jouerPartie();
                break;
            }
            if (entree.equals("n"))
            {
                this.nouvellePartie();
                this.save("save.txt");
                this.finDuJeu();
            }
            else
            {
                System.out.println("Entrée invalide");
            }
        }
    }

    /**
     * Fait jouer un joueur
     *
     * @param joueur Le joueur qui doit jouer
     *
     * @return  Un joueur s'il gagne la partie
     *          null sinon
     */
    private Joueur jouerCoup (Joueur joueur)
    {
        this.save("save.txt");

        System.out.println("Au tour de " + joueur.getNom() + "...");

        // Tant que le joueur n'a pas joué, on lui demande de jouer
        while (true) {
            try {
                Case c = this.grille.addJeton(joueur.jouerJeton(this.nbColonne), joueur.getCouleurJeton());
                if (c.check()) {
                    // Partie gagnée
                    this.grille.afficherGrille();
                    return joueur;
                }
                break;
            } catch (theColumnIsFull e) {
                System.out.println("Colonne pleine");
            }
        }

        this.grille.afficherGrille();

        return null;
    }

    /**
     * Arrête le jeu
     */
    private void finDuJeu ()
    {
        System.out.println("Scores finaux");
        Joueur winner = joueurs[0];

        for (Joueur joueur : this.joueurs)
        {
            System.out.println(joueur.getNom() + " : " + joueur.getScore() + " points.");

            if (joueur.getScore() > winner.getScore())
            {
                winner = joueur;
            }
        }

        System.out.println("Bravo " + winner.getNom());

        System.exit(0);
    }

    /**
     * Sauvegarde de la partie
     *
     * @param path Chemin où sauvegarder la partie
     */
    private void save (String path) {
        // On écrit dans un fichier les données du jeu
        try {
            File file = new File(path);

            // Création du fichier s'il n'existe pas
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(this.toString());
            bw.close();
        } catch (IOException e) {
            System.out.println("Sauvegarde impossible.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Chargement d'une partie sauvegardée
     *
     * @param path Chemin du fichier de sauvegarde
     */
    private void load (String path)
    {
        // Valeurs à récupérer
        int nbLigne = 0, nbColonne = 0, nbPartie = 0, nbCoup = 0;
        Couleur[] jetonsCases = new Couleur[0];
        String J1type = null, J2type = null, J1nom = "", J2nom = "";
        Couleur J1couleur = null, J2couleur = null;
        int J1score = 0, J2score = 0;

        // Récupération des données
        try
        {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int i = 0;
            // On lit ligne par ligne et analyse selon la ligne lue
            while ((line = br.readLine()) != null) {
                if (i == 1)
                {
                    nbPartie = Integer.parseInt(line.split("=")[1]);
                }
                else if (i == 2)
                {
                    nbCoup = Integer.parseInt(line.split("=")[1]);
                }
                else if (i == 4)
                {
                    nbLigne = Integer.parseInt(line.split("=")[1]);
                }
                else if (i == 5)
                {
                    nbColonne = Integer.parseInt(line.split("=")[1]);
                    jetonsCases = new Couleur[nbLigne * nbColonne];
                }
                else if ((i > 6) && (i <= (6 + (nbLigne * nbColonne))))
                {
                    switch (line) {
                        case "VIDE" -> jetonsCases[i - 6 - 1] = Couleur.VIDE;
                        case "ROUGE" -> jetonsCases[i - 6 - 1] = Couleur.ROUGE;
                        case "BLEU" -> jetonsCases[i - 6 - 1] = Couleur.BLEU;
                        case "JAUNE" -> jetonsCases[i - 6 - 1] = Couleur.JAUNE;
                    }
                }
                else if (i == ((6 + (nbLigne * nbColonne)) + 4))
                {
                    J1type = line.split("\\{")[0];
                }
                else if (i == ((6 + (nbLigne * nbColonne)) + 5))
                {
                    J1nom = line.split("=")[1];
                }
                else if (i == ((6 + (nbLigne * nbColonne)) + 6))
                {
                    switch (line.split("=")[1])
                    {
                        case "ROUGE" -> J1couleur = Couleur.ROUGE;
                        case "JAUNE" -> J1couleur = Couleur.JAUNE;
                        case "BLEU" -> J1couleur = Couleur.BLEU;
                    }
                }
                else if (i == ((6 + (nbLigne * nbColonne)) + 7))
                {
                    J1score = Integer.parseInt(line.split("=")[1]);
                }
                else if (i == ((6 + (nbLigne * nbColonne)) + 9))
                {
                    J2type = line.split("\\{")[0];
                }
                else if (i == ((6 + (nbLigne * nbColonne)) + 10))
                {
                    J2nom = line.split("=")[1];
                }
                else if (i == ((6 + (nbLigne * nbColonne)) + 11))
                {
                    switch (line.split("=")[1])
                    {
                        case "ROUGE" -> J2couleur = Couleur.ROUGE;
                        case "JAUNE" -> J2couleur = Couleur.JAUNE;
                        case "BLEU" -> J2couleur = Couleur.BLEU;
                    }
                }
                else if (i == ((6 + (nbLigne * nbColonne)) + 12))
                {
                    J2score = Integer.parseInt(line.split("=")[1]);
                }

                i++;
            }
        }
        catch(Exception e)
        {
            System.out.println("Chargement impossible.");
            e.printStackTrace();
            System.exit(-1);
        }

        // Création du jeu
        // Données
        this.nbPartie = nbPartie;
        this.nbCoup = nbCoup;
        this.nbColonne = nbColonne;

        // Création de la grille
        this.grille = new Grille(nbLigne, nbColonne, jetonsCases);

        // Création des joueurs
        switch (J1type) {
            case "Humain" -> this.joueurs[0] = new Humain(J1nom, J1couleur, J1score);
            case "Ordinateur" -> this.joueurs[0] = new Ordinateur(J1nom, J1couleur, J1score);
        }

        switch (J2type) {
            case "Humain" -> this.joueurs[1] = new Humain(J2nom, J2couleur, J2score);
            case "Ordinateur" -> this.joueurs[1] = new Ordinateur(J2nom, J2couleur, J2score);
        }

        System.out.println("Chargement de la partie réussi!");

        System.out.println("Scores des joueurs :");

        for (Joueur joueur : this.joueurs)
        {
            System.out.println(joueur.getNom() + " : " + joueur.getScore() + " points.");
        }

    }

    /**
     * Retourne les données de l'instance
     *
     * @return Les données de l'instance
     */
    @Override
    public String toString()
    {
        return "Jeu{\n" +
                "nbPartie=" + nbPartie + "\n" +
                "nbCoup=" + nbCoup + "\n" +
                this.grille.toString() + "\n" +
                "Joueurs{\n" +
                this.joueurs[0].toString() + "\n" +
                this.joueurs[1].toString() + "\n" +
                "}\n" +
                '}';
    }

    public static void main (String[] args )
    {
        Jeu jeu = new Jeu(6, 7);

        jeu.jouerPartie();
    }
}
