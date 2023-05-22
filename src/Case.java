import java.util.EnumMap;

public class Case {
    private final EnumMap<Direction, Case> voisins = new EnumMap<>(Direction.class);
    private Couleur couleur;

    /**
     * Constructeur de la classe case.
     */
    public Case () {
        this.couleur = Couleur.VIDE;
    }

    /**
     * Retourne le voisin dans la direction indiquée.
     *
     * @param direction La direction vers laquelle on veut le voisin
     *
     * @return Le voisin dans la direction demandée
     */
    public Case getVoisin (Direction direction)
    {
        return voisins.get(direction);
    }

    /**
     * Retourne le voisin dans la suite de directions indiquées.
     *
     * @param directions La suite de directions vers laquelle on veut le voisin
     *
     * @return Le voisin dans la suite de direction demandée
     */
    public Case getVoisin (Direction... directions)
    {
        Case voisin = this;
        for (Direction direction : directions)
        {
            // Si le prochain voisin est null alors on retourne null
            if (voisin.getVoisin(direction) != null)
            {
                voisin = voisin.getVoisin(direction);
            }
            else
            {
                return null;
            }
        }

        return voisin;
    }

    /**
     * Retourne la couleur du jeton présent dans la case
     *
     * @return La couleur du jeton de la case (VIDE si pas de jeton)
     */
    public Couleur getColor ()
    {
        return this.couleur;
    }

    /**
     * Permet de lier la case à ses voisins
     *
     * @param voisinHaut    Le voisin du haut
     * @param voisinBas     Le voisin du bas
     * @param voisinDroite  Le vosiin de droite
     * @param voisinGauche  Le voisin de gauche
     */
    public void linkCase (Case voisinHaut, Case voisinBas, Case voisinDroite, Case voisinGauche)
    {
        if (voisinHaut != null) voisins.put(Direction.HAUT, voisinHaut);
        if (voisinBas != null) voisins.put(Direction.BAS, voisinBas);
        if (voisinDroite != null) voisins.put(Direction.DROITE, voisinDroite);
        if (voisinGauche != null) voisins.put(Direction.GAUCHE, voisinGauche);
    }

    /**
     * Ajoute un jeton à la case
     *
     * @param couleur La couleur du jeton ajouté
     */
    public void addJeton (Couleur couleur)
    {
        this.couleur = couleur;
    }

    /**
     * Enlève le jeton présent dans la case
     */
    public void viderCase ()
    {
        this.couleur = Couleur.VIDE;
    }

    /**
     * Verifie si la case est dans un alignement de 4 cases ou plus avec des jetons similaires
     *
     * @return  true si la case est dans l'alignement de 4 ou plus possèdant les mêmes jetons
     *          false sinon
     */
    public boolean check () {
        int count;

        // Test horizontal
        count = 1;
        // Droite
        if (this.getVoisin(Direction.DROITE) != null)
        {
            count = this.getVoisin(Direction.DROITE).check(count, this.getColor(), Direction.DROITE);
        }

        // Gauche
        if (this.getVoisin(Direction.GAUCHE) != null)
        {
            count = this.getVoisin(Direction.GAUCHE).check(count, this.getColor(), Direction.GAUCHE);
        }

        // Si il y a 4 jetons ou plus alignés horizontalement, on retoune true
        // Sinon on continue
        if (count >= 4)
        {
            return true;
        }

        // Test vertical
        count = 1;
        // Haut
        if (this.getVoisin(Direction.HAUT) != null)
        {
            count = this.getVoisin(Direction.HAUT).check(count, this.getColor(), Direction.HAUT);
        }

        // Bas
        if (this.getVoisin(Direction.BAS) != null)
        {
            count = this.getVoisin(Direction.BAS).check(count, this.getColor(), Direction.BAS);
        }

        // Si il y a 4 jetons ou plus alignés verticalement, on retoune true
        // Sinon on continue
        if (count >= 4)
        {
            return true;
        }

        // Test diagonale montante
        count = 1;
        // Bas Gauche
        if (this.getVoisin(Direction.BAS, Direction.GAUCHE) != null)
        {
            count = this.getVoisin(Direction.BAS, Direction.GAUCHE).check(count, this.getColor(), Direction.BAS, Direction.GAUCHE);
        }

        // Haut Droite
        if (this.getVoisin(Direction.HAUT, Direction.DROITE) != null)
        {
            count = this.getVoisin(Direction.HAUT, Direction.DROITE).check(count, this.getColor(), Direction.HAUT, Direction.DROITE);
        }

        // Si il y a 4 jetons ou plus alignés sur la diagonale montante, on retoune true
        // Sinon on continue
        if (count >= 4)
        {
            return true;
        }

        // Test diagonale descendante
        count = 1;
        // Haut Gauche
        if (this.getVoisin(Direction.HAUT, Direction.GAUCHE) != null)
        {
            count = this.getVoisin(Direction.HAUT, Direction.GAUCHE).check(count, this.getColor(), Direction.HAUT, Direction.GAUCHE);
        }

        // Bas Droite
        if (this.getVoisin(Direction.BAS, Direction.DROITE) != null)
        {
            count = this.getVoisin(Direction.BAS, Direction.DROITE).check(count, this.getColor(), Direction.BAS, Direction.DROITE);
        }

        // Si il y a 4 jetons ou plus alignés sur la diagonale descendante, on retoune true
        // Sinon on retourne false
        return count >= 4;
    }

    /**
     * Vérifie si la case voisine dans la direction indiquée possède un jeton de même couleur.
     *
     * @param count         Le nombre de cases avec des jetons identiques comptés
     * @param directions    La suite de directions dans laquelle vérifier
     * @param couleur       La couleur des jetons à vérifier
     *
     * @return  true si la case vosine possède un jeton de même couleur
     *          false sinon
     */
    private int check (int count, Couleur couleur, Direction... directions) {
        // Si la case est de même couleur que la case précédente, on ajoute un au compteur
        // Sinon on retourne la valeur du compteur
        if (this.getColor() == couleur)
        {
            count++;
            // Si il y a une case voisine, on test la case voisine
            // Sinon on retourne la valeur du compteur
            if (this.getVoisin(directions) != null)
            {
                return this.getVoisin(directions).check(count, couleur, directions);
            }
            else
            {
                return count;
            }
        }
        else
        {
            return count;
        }
    }
}
