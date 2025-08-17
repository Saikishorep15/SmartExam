import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SmartExamGUI extends JFrame {
    private JTextField subjectField, roomField, studentCountField;
    private JTextArea outputArea;
    private JButton generateButton;

    public SmartExamGUI() {
        setTitle("SmartExam - Exam Seating Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblSubject = new JLabel("Subject:");
        lblSubject.setBounds(30, 30, 100, 25);
        add(lblSubject);

        subjectField = new JTextField();
        subjectField.setBounds(120, 30, 200, 25);
        add(subjectField);

        JLabel lblRoom = new JLabel("Room:");
        lblRoom.setBounds(30, 70, 100, 25);
        add(lblRoom);

        roomField = new JTextField();
        roomField.setBounds(120, 70, 200, 25);
        add(roomField);

        JLabel lblStudents = new JLabel("No. of Students:");
        lblStudents.setBounds(30, 110, 150, 25);
        add(lblStudents);

        studentCountField = new JTextField();
        studentCountField.setBounds(180, 110, 140, 25);
        add(studentCountField);

        generateButton = new JButton("Generate Seating Plan");
        generateButton.setBounds(30, 160, 290, 30);
        add(generateButton);

        outputArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBounds(30, 210, 520, 120);
        add(scroll);

        // Action listener
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generatePlan();
            }
        });

        setVisible(true);
    }

    private void generatePlan() {
        String subject = subjectField.getText();
        String roomName = roomField.getText();
        int studentCount = Integer.parseInt(studentCountField.getText());

        outputArea.setText("Generating seating plan...\n");

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                outputArea.append("Database connection failed!\n");
                return;
            }

            // Fetch room
            PreparedStatement psRoom = conn.prepareStatement("SELECT * FROM rooms WHERE room_name=?");
            psRoom.setString(1, roomName);
            ResultSet rsRoom = psRoom.executeQuery();

            if (!rsRoom.next()) {
                outputArea.append("Room not found in database!\n");
                return;
            }

            int roomId = rsRoom.getInt("room_id");
            int capacity = rsRoom.getInt("capacity");

            if (studentCount > capacity) {
                outputArea.append("Room capacity exceeded!\n");
                return;
            }

            // Get students for subject
            PreparedStatement psStudents = conn.prepareStatement(
                "SELECT * FROM students WHERE subject=? LIMIT ?"
            );
            psStudents.setString(1, subject);
            psStudents.setInt(2, studentCount);
            ResultSet rsStudents = psStudents.executeQuery();

            int seatNo = 1;
            while (rsStudents.next()) {
                String name = rsStudents.getString("name");
                String roll = rsStudents.getString("roll_no");

                outputArea.append("Seat " + seatNo + ": " + name + " (" + roll + ")\n");

                // Insert into seating_plan table
                PreparedStatement psInsert = conn.prepareStatement(
                    "INSERT INTO seating_plan (student_id, room_id, seat_no, exam_date) VALUES (?, ?, ?, CURDATE())"
                );
                psInsert.setInt(1, rsStudents.getInt("student_id"));
                psInsert.setInt(2, roomId);
                psInsert.setInt(3, seatNo);
                psInsert.executeUpdate();

                seatNo++;
            }

            outputArea.append("\n✅ Seating plan generated successfully!");

        } catch (Exception ex) {
            ex.printStackTrace();
            outputArea.append("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new SmartExamGUI();
    }
}
