package services;

    import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {
    
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final boolean auth;
    private final boolean tls;
    
    // Configuration par défaut pour Gmail (modifiable selon ton serveur SMTP)
    public EmailService() {
        this.host = "smtp.gmail.com";
        this.port = "587";
        this.username = System.getenv("EMAIL_USERNAME");
        this.password = System.getenv("EMAIL_PASSWORD");
        this.auth = true;
        this.tls = true;
    }
    
    // Configuration personnalisée
    public EmailService(String host, String port, String username, String password, boolean auth, boolean tls) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.auth = auth;
        this.tls = tls;
    }
    
    public boolean envoyerEmail(String destinataire, String sujet, String contenu) {
        // Vérification des credentials
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.err.println("ERREUR : EMAIL_USERNAME ou EMAIL_PASSWORD non définis !");
            return false;
        }
        
        System.out.println("Tentative d'envoi d'email avec :");
        System.out.println("  Host: " + host);
        System.out.println("  Port: " + port);
        System.out.println("  Username: " + username);
        
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            // Debug mode pour voir la conversation SMTP
            props.put("mail.debug", "true");
            
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setContent(contenu, "text/html; charset=utf-8");
            
            Transport.send(message);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("Erreur d'envoi d'email : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean envoyerCredentials(String email, String nomUtilisateur, String motDePasse, String prenom, String nom) {
        String sujet = "Vos identifiants de connexion - Univ Scheduler";
        
        String contenu = String.format(
            "<html>" +
            "<body style='font-family: Arial, sans-serif;'>" +
            "<h2 style='color: #4F46E5;'>Bienvenue sur Univ Scheduler !</h2>" +
            "<p>Bonjour <strong>%s %s</strong>,</p>" +
            "<p>Votre compte a été créé avec succès. Voici vos identifiants de connexion :</p>" +
            "<div style='background: #f3f4f6; padding: 15px; border-radius: 8px; margin: 20px 0;'>" +
            "<p><strong>Nom d'utilisateur :</strong> %s</p>" +
            "<p><strong>Mot de passe :</strong> <span style='color: #DC2626; font-family: monospace;'>%s</span></p>" +
            "</div>" +
            "<p style='color: #6B7280; font-size: 12px;'>" +
            "Pour des raisons de sécurité, nous vous recommandons de changer votre mot de passe après votre première connexion." +
            "</p>" +
            "<hr style='border: none; border-top: 1px solid #e5e7eb; margin: 20px 0;'>" +
            "<p style='color: #9CA3AF; font-size: 11px;'>" +
            "Ceci est un email automatique, merci de ne pas y répondre." +
            "</p>" +
            "</body>" +
            "</html>",
            prenom, nom, nomUtilisateur, motDePasse
        );
        
        return envoyerEmail(email, sujet, contenu);
    }
}
