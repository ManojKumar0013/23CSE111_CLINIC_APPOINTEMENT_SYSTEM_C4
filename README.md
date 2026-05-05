# Clinic Appointment Scheduler System

##  Project Title  
Clinic Appointment Scheduler System

---

## Team Members  

- **Bondada Manoj Kumar (AM.SC.U4CSE25210)** – System design, core logic implementation  
- **Kommineni Yashwanth (AM.SC.U4CSE25228)** – UML diagrams and documentation  
- **Pagidela Obulesu (AM.SC.U4CSE25267)** – Testing and debugging  
- **Bandela Gowtham Sai (AM.SC.U4CSE25208)** – File handling and system integration  

---

##  Problem Description  

This project is a **Clinic Appointment Scheduler System** designed to efficiently manage patient appointments in a clinic.

Patients can book appointments with doctors based on available time slots. Doctors can manage their schedules and view appointments. Receptionists assist in generating reports and managing daily schedules.

The system reduces manual errors, improves scheduling efficiency, and ensures smooth coordination between patients, doctors, and clinic staff.

---

## How to Run the Code  

1. Open the project in any Java IDE (Eclipse / IntelliJ / VS Code)  
2. Navigate to:  
src/clinic/Main.java

3. Run the `Main` class  
4. Follow the console instructions (Login / Signup / Booking)

---

##  Sample Input / Output  

###  Input  
Login / Signup
Enter details
Book appointment

---

###  Output  

- Patient registration with auto-generated ID  
- Login with role-based access  
- Doctor assignment based on disease  
- Available time slots displayed  
- Appointment booking confirmation  
- Appointment cancellation and viewing  

---

##  Tools and Technologies Used  

- Java (JDK 8 or above)  
- Java Collections Framework (HashMap, ArrayList, Set)  
- File Handling (TXT files)  

###  OOP Concepts Used  

- Inheritance  
- Encapsulation  
- Abstraction  
- Composition  
- Aggregation  

---

## UML Diagrams  

- Class Diagram  
- Use Case Diagram  
- Sequence Diagrams:
  - Booking Appointment  
  - Cancel Appointment  
  - Generate Daily Report  

---

##  Repository Structure  
/Phase1 → Problem Analysis
/Phase2 → UML Diagrams
/src → Java Source Code
/data → Stored data files
/output → Output screenshots
README.md → Project Documentation

---

##  Key Features  

###  Patient  

- Signup & Login  
- Auto-generated Patient ID  
- Book appointment (based on available slots)  
- Automatic doctor assignment (based on disease)  
- View appointments  
- Cancel appointments  

---

###  Doctor  

- View assigned appointments  
- Manage schedule availability  

---

###  Receptionist  

- Generate daily appointment reports  
- Assist with scheduling  

---

###  Admin  

- Add doctors  
- View doctors  
- Manage system  

---

##  Design Notes  

- The system follows a **modular object-oriented design**  
- `Clinic` acts as the central controller  
- `Schedule` handles calendar-based slot generation  
- `AuthManager` manages login and role-based access  
- `FileManager` ensures data persistence using text files  
- Patients are linked to login credentials to avoid duplication  

---

##  Special Highlights  

- Calendar-based scheduling using `LocalDate` & `LocalTime`  
- Dynamic slot generation (no hardcoding)  
- Role-based dashboards (Patient, Doctor, Admin, Receptionist)  
- File-based persistence (simulating database)  
- Prevention of duplicate data and invalid bookings  

---

## Conclusion  

This project demonstrates how a real-world clinic system can be designed using UML and implemented using Java. It effectively applies object-oriented principles, file handling, and modular design to build a scalable and maintainable system.

---


