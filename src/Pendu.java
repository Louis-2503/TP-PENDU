import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.TitledPane;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;

/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {
    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */
    public List<String> niveaux;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Text motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Text leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    /**
     * le bouton Accueil / Maison
     */
    private Button boutonHome;
    /**
     * le bouton qui permet de (lancer ou relancer une partie
     */
    private Button bJouer;
    private Stage stage;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono
     * ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./img");
        // A terminer d'implementer
    }

    /**
     * @return le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene() {
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private Pane titre() {
        // A implementer
        Pane banniere = new Pane();
        return banniere;
    }

    /**
     * @return le panel du chronomètre
     */
    private TitledPane leChrono() {
        // A implementer
        TitledPane res = new TitledPane();
        return res;
    }

    /**
     * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     *         de progression et le clavier
     */
    private Pane fenetreJeu() {
        this.dessin = new ImageView(this.lesImages.get(0));
        this.dessin.setFitWidth(450);
        this.dessin.setPreserveRatio(true);

        BorderPane imageBox = new BorderPane(this.dessin);
        imageBox.setPadding(new Insets(10));

        Button nouveauMotBtn = new Button("Nouveau mot");
        nouveauMotBtn.setOnAction(e -> lancePartie());

        VBox panneauDroite = new VBox(20, leNiveau, leChrono(), nouveauMotBtn);
        panneauDroite.setAlignment(Pos.TOP_CENTER);
        panneauDroite.setPadding(new Insets(20));

        this.pg = new ProgressBar(0);

        this.clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ-", new ControleurLettres(modelePendu, this));

        VBox clavierBox = new VBox(10, this.pg, this.clavier);
        clavierBox.setAlignment(Pos.CENTER);

        VBox centre = new VBox(15);
        centre.getChildren().addAll(this.motCrypte, imageBox, clavierBox);
        centre.setAlignment(Pos.TOP_CENTER);
        centre.setPadding(new Insets(20));

        BorderPane res = new BorderPane();
        res.setCenter(centre);
        res.setRight(panneauDroite);

        return res;
    }

    /**
     * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de
     *         jeu
     */

    private Pane fenetreAccueil() {
        VBox contenu = new VBox(20);
        contenu.setPadding(new Insets(20));
        contenu.setAlignment(Pos.TOP_CENTER);

        // Titre
        Text titre = new Text("Jeu du Pendu");
        titre.setFont(Font.font("Arial", 28));

        // Boutons du haut
        HBox topButtons = new HBox(10);
        topButtons.setAlignment(Pos.TOP_RIGHT);

        Button boutonHome = new Button();
        boutonHome.setGraphic(new ImageView(new Image("file:./img/home.png", 30, 30, true, true)));

        Button boutonParametres = new Button();
        boutonParametres.setGraphic(new ImageView(new Image("file:./img/parametres.png", 30, 30, true, true)));

        Button boutonInfo = new Button();
        boutonInfo.setGraphic(new ImageView(new Image("file:./img/info.png", 30, 30, true, true)));

        topButtons.getChildren().addAll(boutonHome, boutonParametres, boutonInfo);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(titre);
        topPane.setRight(topButtons);
        topPane.setPadding(new Insets(10));
        topPane.setStyle("-fx-background-color: #eaeaff;");

        // Bouton "Lancer une partie"
        bJouer = new Button("Lancer une partie");
        bJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        bJouer.setOnAction(e -> {
            this.lancePartie();
        });

        VBox vboxCenter = new VBox(5);

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton radioB1 = new RadioButton("facile");
        RadioButton radioB2 = new RadioButton("moyen");
        RadioButton radioB3 = new RadioButton("difficile");
        RadioButton radioB4 = new RadioButton("expert");

        radioB1.setToggleGroup(toggleGroup);
        radioB2.setToggleGroup(toggleGroup);
        radioB3.setToggleGroup(toggleGroup);
        radioB4.setToggleGroup(toggleGroup);

        // Connecte chaque RadioButton au contrôleur
        radioB1.setOnAction(new ControleurNiveau(modelePendu, this));
        radioB2.setOnAction(new ControleurNiveau(modelePendu, this));
        radioB3.setOnAction(new ControleurNiveau(modelePendu, this));
        radioB4.setOnAction(new ControleurNiveau(modelePendu, this));

        vboxCenter.getChildren().addAll(radioB1, radioB2, radioB3, radioB4);
        vboxCenter.setPadding(new Insets(10));

        TitledPane choixNiveau = new TitledPane("Niveau de difficulté", vboxCenter);
        choixNiveau.setExpanded(true);

        contenu.getChildren().addAll(bJouer, choixNiveau);

        return new VBox(topPane, contenu);
    }

    public void setLeNiveau(String niveau) {
        this.leNiveau = new Text(niveau);
    }

    /**
     * charge les images à afficher en fonction des erreurs
     * 
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire) {
        for (int i = 0; i < this.modelePendu.getNbErreursMax() + 1; i++) {
            File file = new File(repertoire + "/pendu" + i + ".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil() {
        this.panelCentral.setCenter(this.fenetreAccueil());
    }

    public void modeJeu() {
        this.panelCentral.setCenter(this.fenetreJeu());
    }

    public void modeParametres() {
        // A implémenter
    }

    /** lance une partie */

    public void lancePartie() {
        this.modelePendu.setMotATrouver(); // tire un nouveau mot
        this.majAffichage();
        this.motCrypte = new Text(modelePendu.getMotCrypte());
        this.motCrypte.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        this.chrono = new Chronometre();
        this.modeJeu();
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage() {
        if (this.motCrypte != null) {
            this.motCrypte.setText(modelePendu.getMotCrypte());
        }

        if (this.dessin != null) {
            int nbErreurs = modelePendu.getNbErreursMax() - modelePendu.getNbErreursRestants();
            if (nbErreurs >= 0 && nbErreurs < lesImages.size()) {
                this.dessin.setImage(this.lesImages.get(nbErreurs));
            }
        }

        if (this.pg != null) {
            double progress = (double) modelePendu.getNbEssais() / modelePendu.getNbErreursMax();
            this.pg.setProgress(progress);
        }
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * 
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono() {
        // A implémenter
        return null; // A enlever
    }

    public Alert popUpPartieEnCours() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }

    public Alert popUpReglesDuJeu() {
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    public Alert popUpMessageGagne() {
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    public Alert popUpMessagePerdu() {
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * 
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        this.panelCentral = new BorderPane();
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
        stage.show();
    }

    /**
     * Programme principal
     * 
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void desactiveTouches(Set<String> touchesDesactivees) {
        this.clavier.desactiveTouches(touchesDesactivees);
    }
}
