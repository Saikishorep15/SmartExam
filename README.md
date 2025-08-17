# SmartExam 🎓  

SmartExam is an intelligent **exam sitting management system** that automates seating arrangements, reduces errors, and saves valuable time for colleges and universities.  

---

## 🚀 Features  
- **Automated Seating Plan:** Generate seating based on subjects, rooms, and student count.  
- **User-Friendly GUI:** Simple interface with a light blue theme.  
- **Database Integration:** Store and retrieve student/room details using MySQL.  
- **Error Reduction:** Avoids manual mistakes in seating allocation.  
- **Scalable Design:** Works for schools, colleges, and universities.  

---

## 🛠️ Built With  
- **Java (Swing GUI)** – for desktop interface  
- **MySQL (XAMPP)** – for storing student, subject, and room data  
- **NetBeans IDE** – development environment  
- **GitHub** – version control & collaboration  

---

## 📖 How It Works  
1. Enter the **subject**, **room**, and **number of students**.  
2. Click **Generate Seating Plan**.  
3. System automatically creates a seating arrangement.  
4. Faculty can save/export the plan for use during exams.  

---

## ⚙️ Configuration  

### Requirements  
- Java JDK 8+  
- NetBeans IDE (or IntelliJ/Eclipse)  
- MySQL (XAMPP / WAMP / Local MySQL Server)  

### Database Setup  
1. Import the provided `smartexam.sql` file into MySQL.  
2. Update DB connection in `DBConnection.java`:  
   ```java
   String url = "jdbc:mysql://localhost:3306/smartexam";
   String user = "root";        // default MySQL username
   String password = "";        // default is empty in XAMPP
