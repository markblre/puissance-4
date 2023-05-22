# puissance-4
Implémentation du jeu Puissance 4 jouable dans la console.

## Fonctionnalités
- Jeu à 1 ou 2 joueurs
- Sauvegarde de partie
- Comptage de score entre les parties
- Choix de la couleur des jetons

## Comment lancer le programme ?
Dans le répertoire du projet :
```
sh shell.sh
```

## Remplacer l'ordinateur par un autre joueur
Il suffit simplement de modifier la ligne 73 du fichier source Jeu.java :  

Actuelle :
```java
this.joueurs[1] = new Ordinateur(couleursPossiblesJeton.toArray(new Couleur[0]));
```

Nouvelle :
```java
this.joueurs[1] = new Humain(couleursPossiblesJeton.toArray(new Couleur[0]));
```

## UML
<img width="608" alt="UML" src="https://github.com/markblre/puissance-4/assets/46789972/2f2eb2de-c59c-4d31-97ee-3e21ed85e4e6">

## Fonctionnement général
Avant de justifier les classes il faut d’abord comprendre comment un jeu va se dérouler.
Un jeu se divise en plusieurs parties. C’est-à-dire qu’une fois une partie terminée (victoire d’un des joueurs ou égalité) les joueurs peuvent choisir de recommencer la partie tout en restant dans le même jeu. Cela permet de faire plusieurs parties en comptant les scores. Le score d’un joueur est le nombre de partie qu’il a gagné dans le jeu.  
De plus, une partie se divise en coup. Un coup correspond au fait qu’un joueur ajoute un jeton à la grille. À chaque coup on vérifie si le joueur a gagné. Si oui alors la partie s’arrête et sinon elle continue. On vérifie également que la grille ne soit pas pleine. Si elle est pleine alors la partie s’arrête et aucun gagnant n’est désigné.  
À chaque coup, le programme enregistre l’avancée du jeu dans un fichier texte qui peut ensuite être rechargé au lancement du programme.  

Voyons maintenant les classes misent en place pour réaliser un jeu fonctionnel.  

### La classe Jeu
<img width="354" alt="Capture d’écran 2023-05-23 à 00 22 38" src="https://github.com/markblre/puissance-4/assets/46789972/0915d9ea-db83-43f5-b185-96d8d3f1415f">

Cette classe a pour but que la partie se déroule correctement. C’est elle qui crée et gère tous les objets nécessaires au bon déroulement de la partie. Elle possède en attribut les données de jeu comme le nombre de coup de la partie en cours, le nombre de parties jouées dans le jeu et le nombre de colonnes de la grille.  
La classe possède également plusieurs méthodes qui répondent à des fonctions bien précisent (voir commentaires du code).  

### La classe Grille
<img width="311" alt="Capture d’écran 2023-05-23 à 00 23 41" src="https://github.com/markblre/puissance-4/assets/46789972/1bfd6eda-d27b-4801-b08c-e8aad26fac34">

Ses attributs sont le nombre de lignes et de colonnes de la grille ainsi qu’un tableau contenant les références vers les dernières cases vides de chaque colonne. Cela permet d’ajouter les jetons dans ces cases lors d’un coup.  
La classe possède également plusieurs méthodes qui répondent à des fonctions bien précisent (voir commentaires du code).  

Dans les attributs de la classe **GRILLE** il y a également une référence vers la première case de la grille.  

### La classe Case
<img width="531" alt="Capture d’écran 2023-05-23 à 00 25 19" src="https://github.com/markblre/puissance-4/assets/46789972/3a9d1c01-a7d0-4f13-90fa-b7bc60de97cc">

La classe **CASE** est utilisée pour représenter les cases qui composent la grille. Seule la première case est référencée dans **GRILLE** car chaque case possède une référence vers ses voisins. On accède donc aux différentes cases de la grilles par références en utilisant les voisins.  
Les voisins de chaque case sont stockés dans une EnumMap. Cela permet de lier à chaque voisin une direction qui est la direction du voisin par rapport à la case actuelle.  
Chaque case possède également un attribut de couleur qui est la couleur du jeton se trouvant dans la case s’il y en a un.  

### Les classes Joueur, Humain et Ordinateur
<img width="790" alt="Capture d’écran 2023-05-23 à 00 26 20" src="https://github.com/markblre/puissance-4/assets/46789972/dfebe9e7-c008-4c99-b09c-7f12033bc947">

La classe **JOUEUR** est une classe abstraite. Elle implémente les attributs et méthodes communes à tous les joueurs. Il y a ensuite les classes **HUMAIN** et **ORDINATEUR** qui héritent de JOUEUR et définissent des méthodes spécifiques au type de joueur.  
Chaque joueur possède donc un nom, une couleur de jeton ainsi qu’un score.  
La classe **HUMAIN** représente les joueurs réels. Ce sont ceux qui vont interagir avec la console pour jouer au jeu. La classe ORDINATEUR quant à elle représente une IA qui choisira au hasard une colonne pour poser ses jetons. Cela permet de lancer une partie en solo. (Pour modifier le type d’un joueur il suffit de se rendre aux lignes 69 et 73 pour modifier la classe instanciée)  
Les classes possèdent également plusieurs méthodes qui répondent à des fonctions bien précisent (voir commentaires du code).
