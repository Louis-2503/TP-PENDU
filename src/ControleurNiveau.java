import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;

/**
 * Controleur des radio boutons gérant le niveau
 */
public class ControleurNiveau implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    private Pendu pendu;


    /**
     * @param modelePendu modèle du jeu
     */
    public ControleurNiveau(MotMystere modelePendu, Pendu vue) {
        this.modelePendu = modelePendu;
        this.pendu = vue;
    }

    /**
     * gère le changement de niveau
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        // A implémenter
        RadioButton radiobouton = (RadioButton) actionEvent.getTarget();
        String niveauChoisi = radiobouton.getText();

                switch (niveauChoisi) {
            case "Facile":
                modelePendu.setNiveau(MotMystere.FACILE);
                break;
            case "Médium":
                modelePendu.setNiveau(MotMystere.MOYEN);
                break;
            case "Difficile":
                modelePendu.setNiveau(MotMystere.DIFFICILE);
                break;
            case "Expert":
                modelePendu.setNiveau(MotMystere.EXPERT);
                break;
        }

        this.pendu.setLeNiveau(niveauChoisi);

        modelePendu.setMotATrouver(); // choisit un nouveau mot
        this.pendu.majAffichage(); // met à jour la vue






    }
}
