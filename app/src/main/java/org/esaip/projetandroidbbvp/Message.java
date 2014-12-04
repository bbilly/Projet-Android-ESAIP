package org.esaip.projetandroidbbvp;

/**
 * Created by Baptiste on 04/12/2014.
 */
public class Message {

    private String auteur;
    private String contenu_message;

    public Message(String auteur, String message) {
        this.auteur = auteur;
        this.contenu_message = message;
    }

    public String getContenu_message() {
        return contenu_message;
    }

    public void setContenu_message(String contenu_message) {
        this.contenu_message = contenu_message;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }
}
