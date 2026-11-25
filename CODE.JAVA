import java.util.*;

class Student {
    int roll, total, present;
    String name;

    Student(int roll, String name) {
        this.roll = roll;
        this.name = name;
    }

    void mark(boolean isPresent) {
        total++;
        if (isPresent) present++;
    }

    double pct() {
        return total == 0 ? 0 : (present * 100.0) / total;
    }

    void display() {
        System.out.printf("%-10d %-18s %-8d %-8d %.2f%%%n", roll, name, total, present, pct());
    }
}

public class StudentAttendanceManagement {
    static Scanner sc = new Scanner(System.in);
    static List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        int ch;
        do {
            System.out.println("\n1.Add 2.Mark 3.View 4.Search Roll 5.Search Name 6.Sort Name 7.Sort Attendance 8.Report 0.Exit");
            System.out.print("Choice: ");
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> addStudent();
                case 2 -> markAttendance();
                case 3 -> viewStudents();
                case 4 -> searchRoll();
                case 5 -> searchName();
                case 6 -> { students.sort(Comparator.comparing(s -> s.name.toLowerCase())); viewStudents(); }
                case 7 -> { students.sort(Comparator.comparingDouble(Student::pct).reversed()); viewStudents(); }
                case 8 -> report();
                case 0 -> System.out.println("Bye!");
                default -> System.out.println("Invalid!");
            }
        } while (ch != 0);
        sc.close();
    }

    static Student find(int roll) {
        for (Student s : students) if (s.roll == roll) return s;
        return null;
    }

    static void addStudent() {
        System.out.print("Roll: ");
        int roll = sc.nextInt();
        sc.nextLine();
        if (find(roll) != null) { System.out.println("Exists!"); return; }

        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Empty name!"); return; }

        students.add(new Student(roll, name));
        System.out.println("Added!");
    }

    static void markAttendance() {
        System.out.print("Roll: ");
        int roll = sc.nextInt();
        sc.nextLine();
        Student s = find(roll);
        if (s == null) { System.out.println("Not found!"); return; }

        System.out.print("P/A: ");
        String st = sc.nextLine().trim();
        if (st.equalsIgnoreCase("P")) { s.mark(true); System.out.println("Marked Present."); }
        else if (st.equalsIgnoreCase("A")) { s.mark(false); System.out.println("Marked Absent."); }
        else System.out.println("Invalid!");
    }

    static void viewStudents() {
        if (students.isEmpty()) { System.out.println("No records!"); return; }
        header();
        students.forEach(Student::display);
    }

    static void searchRoll() {
        System.out.print("Roll: ");
        Student s = find(sc.nextInt());
        sc.nextLine();
        if (s == null) { System.out.println("Not found!"); return; }
        header();
        s.display();
    }

    static void searchName() {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        List<Student> matches = students.stream()
                .filter(s -> s.name.equalsIgnoreCase(name))
                .toList();
        if (matches.isEmpty()) { System.out.println("Not found!"); return; }
        header();
        matches.forEach(Student::display);
    }

    static void report() {
        if (students.isEmpty()) { System.out.println("No records!"); return; }
        header();
        int eligible = 0;
        for (Student s : students) {
            s.display();
            if (s.pct() >= 75) eligible++;
        }
        System.out.printf("%nTotal: %d | >=75%%: %d | <75%%: %d%n", students.size(), eligible, students.size() - eligible);
    }

    static void header() {
        System.out.printf("%-10s %-18s %-8s %-8s %s%n", "Roll", "Name", "Total", "Present", "Pct");
        System.out.println("----------------------------------------------------");
    }
}
