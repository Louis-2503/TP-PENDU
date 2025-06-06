import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * Controleur du clavier
 */
public class ControleurLettres implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    /**
     * vue du jeu
     */
    private Pendu vuePendu;

    /**
     * @param modelePendu modèle du jeu
     * @param vuePendu    vue du jeu
     */
    ControleurLettres(MotMystere modelePendu, Pendu vuePendu) {
        // A implémenter
        this.modelePendu = modelePendu;
        this.vuePendu = vuePendu;
    }

    /**
     * Actions à effectuer lors du clic sur une touche du clavier
     * Il faut donc: Essayer la lettre, mettre à jour l'affichage et vérifier si la
     * partie est finie
     * 
     * @param actionEvent l'événement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button boutonLettre = (Button) actionEvent.getSource();
        String lettreTexte = boutonLettre.getText();
        if (lettreTexte.length() == 1) {
            char lettre = lettreTexte.charAt(0);
            modelePendu.essaiLettre(lettre);
            vuePendu.majAffichage();
            vuePendu.desactiveTouches(this.modelePendu.getLettresEssayees());
            vuePendu.majAffichage();
            if (modelePendu.gagne()) {
                vuePendu.getChrono().stop();
                vuePendu.popUpMessageGagne();
            } else if (modelePendu.perdu()) {
                vuePendu.getChrono().stop();
                vuePendu.popUpMessagePerdu();
            }
        }
    }
}
