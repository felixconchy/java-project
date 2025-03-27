import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Charger le pilote SQLite
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC non trouvé. Assurez-vous d'avoir le jar sqlite-jdbc.");
            return;
        }

        // Lancer l'application Swing de manière thread-safe
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TrueOrFalseGame().setVisible(true);
            }
        });
    }
}