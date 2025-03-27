import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrueOrFalseGame extends JFrame {
    private JLabel questionLabel;
    private JButton trueButton;
    private JButton falseButton;
    private JLabel scoreLabel;
    private JLabel statusLabel;

    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private static final int MAX_QUESTIONS = 10;

    private static class Question {
        String text;
        boolean answer;

        Question(String text, boolean answer) {
            this.text = text;
            this.answer = answer;
        }
    }

    public TrueOrFalseGame() {
        initializeDatabase();
        questions = loadQuestionsFromDatabase();
        Collections.shuffle(questions);

        setTitle("Jeu Vrai ou Faux");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeComponents();
        startGame();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:questions.db")) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS questions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "text TEXT, " +
                    "answer BOOLEAN)");

            // Insérer quelques questions par défaut si la table est vide
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM questions");
            if (rs.getInt(1) == 0) {
                PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO questions (text, answer) VALUES (?, ?)");
                
                String[][] defaultQuestions = {
                    {"La Terre est ronde", "true"},
                    {"Paris est la capitale de la France", "true"},
                    {"L'océan Pacifique est le plus petit océan", "false"},
                    {"Le corps humain a 206 os", "true"},
                    {"Le soleil tourne autour de la Terre", "false"}
                };

                for (String[] q : defaultQuestions) {
                    pstmt.setString(1, q[0]);
                    pstmt.setBoolean(2, Boolean.parseBoolean(q[1]));
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Question> loadQuestionsFromDatabase() {
        List<Question> loadedQuestions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:questions.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT text, answer FROM questions")) {

            while (rs.next()) {
                loadedQuestions.add(new Question(
                    rs.getString("text"), 
                    rs.getBoolean("answer")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loadedQuestions;
    }

    private void initializeComponents() {
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        trueButton = new JButton("Vrai");
        falseButton = new JButton("Faux");

        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        statusLabel = new JLabel("", SwingConstants.CENTER);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.add(questionLabel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(trueButton);
        buttonPanel.add(falseButton);
        centerPanel.add(buttonPanel);
        centerPanel.add(statusLabel);

        add(centerPanel, BorderLayout.CENTER);
        add(scoreLabel, BorderLayout.SOUTH);

        trueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(true);
            }
        });

        falseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(false);
            }
        });
    }

    private void startGame() {
        currentQuestionIndex = 0;
        score = 0;
        updateScore();
        nextQuestion();
    }

    private void nextQuestion() {
        if (currentQuestionIndex < MAX_QUESTIONS && currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText(q.text);
            statusLabel.setText("");
        } else {
            endGame();
        }
    }

    private void checkAnswer(boolean userAnswer) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        
        if (userAnswer == currentQuestion.answer) {
            score++;
            statusLabel.setText("Correct !");
            statusLabel.setForeground(Color.GREEN);
        } else {
            statusLabel.setText("Incorrect !");
            statusLabel.setForeground(Color.RED);
        }

        updateScore();
        currentQuestionIndex++;

        // Petit délai avant la prochaine question
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextQuestion();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + score + "/" + Math.min(MAX_QUESTIONS, questions.size()));
    }

    private void endGame() {
        int totalQuestions = Math.min(MAX_QUESTIONS, questions.size());
        JOptionPane.showMessageDialog(this, 
            "Jeu terminé !\nVotre score final : " + score + "/" + totalQuestions, 
            "Résultat", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Relancer ou fermer le jeu
        int choice = JOptionPane.showConfirmDialog(
            this, 
            "Voulez-vous recommencer ?", 
            "Nouvelle partie", 
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            startGame();
        } else {
            dispose();
        }
    }

    public static void main(String[] args) {
        // Charger le pilote SQLite
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC non trouvé. Assurez-vous d'avoir le jar sqlite-jdbc.");
            return;
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TrueOrFalseGame().setVisible(true);
            }
        });
    }
}