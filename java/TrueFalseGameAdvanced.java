import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.CardLayout;


public class TrueFalseGameAdvanced extends JFrame {
    // Panels
    private JPanel mainPanel;
    private JCardLayout cardLayout;
    
    // Utilisateurs
    private Map<String, String> users = new HashMap<>();
    private String currentUser = null;

    // Composants d'authentification
    private JTextField usernameField;
    private JPasswordField passwordField;

    // Questions de jeu
    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;

    // Composants de jeu
    private JLabel questionLabel;
    private JButton trueButton;
    private JButton falseButton;
    private JLabel scoreLabel;

    public TrueFalseGameAdvanced() {
        // Configuration de la fenêtre
        setTitle("True or False - Jeu Éducatif");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialisation des questions
        initQuestions();

        // Configuration du layout à cartes
        cardLayout = new JCardLayout();
        mainPanel = new JPanel(cardLayout);

        // Créer les différents écrans
        createWelcomeScreen();
        createLoginScreen();
        createRegistrationScreen();
        createGameScreen();

        // Ajouter le panel principal
        add(mainPanel);

        // Centrer la fenêtre
        setLocationRelativeTo(null);
    }

    private void createWelcomeScreen() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(135, 206, 250)); // Bleu clair
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));

        // Titre
        JLabel titleLabel = new JLabel("True or False - Jeu Culture Générale");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Boutons
        JButton loginButton = createStyledButton("Se Connecter", new Color(70, 130, 180));
        loginButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        JButton registerButton = createStyledButton("S'inscrire", new Color(60, 179, 113));
        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));

        JButton quitButton = createStyledButton("Quitter", new Color(220, 20, 60));
        quitButton.addActionListener(e -> System.exit(0));

        // Ajouter les composants
        welcomePanel.add(Box.createVerticalStrut(50));
        welcomePanel.add(titleLabel);
        welcomePanel.add(Box.createVerticalStrut(30));
        welcomePanel.add(loginButton);
        welcomePanel.add(Box.createVerticalStrut(20));
        welcomePanel.add(registerButton);
        welcomePanel.add(Box.createVerticalStrut(20));
        welcomePanel.add(quitButton);
        welcomePanel.add(Box.createVerticalStrut(50));

        mainPanel.add(welcomePanel, "WELCOME");
    }

    private void createLoginScreen() {
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(173, 216, 230)); // Bleu très clair
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        // Titre
        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Champs de saisie
        usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(250, 30));
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(250, 30));

        // Boutons
        JButton connectButton = createStyledButton("Se Connecter", new Color(70, 130, 180));
        connectButton.addActionListener(e -> loginUser());

        JButton backButton = createStyledButton("Retour", new Color(220, 20, 60));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));

        // Ajouter les composants
        loginPanel.add(Box.createVerticalStrut(50));
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(new JLabel("Nom d'utilisateur"));
        loginPanel.add(usernameField);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(new JLabel("Mot de passe"));
        loginPanel.add(passwordField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(connectButton);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(backButton);

        mainPanel.add(loginPanel, "LOGIN");
    }

    private void createRegistrationScreen() {
        JPanel registerPanel = new JPanel();
        registerPanel.setBackground(new Color(173, 216, 230)); // Bleu très clair
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));

        // Champs de saisie pour l'inscription
        JTextField newUsernameField = new JTextField(20);
        newUsernameField.setMaximumSize(new Dimension(250, 30));
        JPasswordField newPasswordField = new JPasswordField(20);
        newPasswordField.setMaximumSize(new Dimension(250, 30));

        // Bouton d'inscription
        JButton registerButton = createStyledButton("Créer un compte", new Color(60, 179, 113));
        registerButton.addActionListener(e -> {
            String username = newUsernameField.getText();
            String password = new String(newPasswordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs");
                return;
            }

            if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(this, "Ce nom d'utilisateur existe déjà");
                return;
            }

            users.put(username, password);
            JOptionPane.showMessageDialog(this, "Compte créé avec succès!");
            cardLayout.show(mainPanel, "LOGIN");
        });

        JButton backButton = createStyledButton("Retour", new Color(220, 20, 60));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));

        // Ajouter les composants
        registerPanel.add(Box.createVerticalStrut(50));
        registerPanel.add(new JLabel("Créer un compte"));
        registerPanel.add(Box.createVerticalStrut(20));
        registerPanel.add(new JLabel("Nom d'utilisateur"));
        registerPanel.add(newUsernameField);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(new JLabel("Mot de passe"));
        registerPanel.add(newPasswordField);
        registerPanel.add(Box.createVerticalStrut(20));
        registerPanel.add(registerButton);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(backButton);

        mainPanel.add(registerPanel, "REGISTER");
    }

    private void createGameScreen() {
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(240, 248, 255)); // Bleu très très clair

        // Panneau de la question
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionPanel.add(questionLabel, BorderLayout.CENTER);

        // Panneau des boutons de réponse
        JPanel answerPanel = new JPanel(new FlowLayout());
        trueButton = createStyledButton("Vrai", new Color(60, 179, 113));
        falseButton = createStyledButton("Faux", new Color(220, 20, 60));

        trueButton.addActionListener(e -> checkAnswer(true));
        falseButton.addActionListener(e -> checkAnswer(false));

        answerPanel.add(trueButton);
        answerPanel.add(falseButton);

        // Panneau du score et des actions
        JPanel controlPanel = new JPanel(new FlowLayout());
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        JButton quitGameButton = createStyledButton("Quitter le jeu", new Color(70, 130, 180));
        quitGameButton.addActionListener(e -> endGame());

        controlPanel.add(scoreLabel);
        controlPanel.add(quitGameButton);

        // Assembler le panneau de jeu
        gamePanel.add(questionPanel, BorderLayout.NORTH);
        gamePanel.add(answerPanel, BorderLayout.CENTER);
        gamePanel.add(controlPanel, BorderLayout.SOUTH);

        mainPanel.add(gamePanel, "GAME");
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        return button;
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (users.containsKey(username) && users.get(username).equals(password)) {
            currentUser = username;
            startGame();
            cardLayout.show(mainPanel, "GAME");
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants incorrects");
        }
    }

    private void initQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("La Terre est plate.", false));
        questions.add(new Question("Paris est la capitale de la France.", true));
        questions.add(new Question("Il y a 8 planètes dans notre système solaire.", true));
        // Ajoutez plus de questions ici
    }

    private void startGame() {
        currentQuestionIndex = 0;
        score = 0;
        updateQuestion();
    }

    private void updateQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getText());
            scoreLabel.setText("Score: " + score + " (Joueur: " + currentUser + ")");
        } else {
            endGame();
        }
    }

    private void checkAnswer(boolean userAnswer) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        
        if (userAnswer == currentQuestion.isCorrect()) {
            score++;
            JOptionPane.showMessageDialog(this, "Correct!", "Résultat", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect!", "Résultat", JOptionPane.ERROR_MESSAGE);
        }

        currentQuestionIndex++;
        updateQuestion();
    }

    private void endGame() {
        int totalQuestions = questions.size();
        JOptionPane.showMessageDialog(this, 
            "Jeu terminé!\nVotre score: " + score + " / " + totalQuestions, 
            "Résultat Final", 
            JOptionPane.INFORMATION_MESSAGE);
        
        cardLayout.show(mainPanel, "WELCOME");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TrueFalseGameAdvanced game = new TrueFalseGameAdvanced();
            game.setVisible(true);
        });
    }

    private class Question {
        private String text;
        private boolean correct;

        public Question(String text, boolean correct) {
            this.text = text;
            this.correct = correct;
        }

        public String getText() {
            return text;
        }

        public boolean isCorrect() {
            return correct;
        }
    }
}