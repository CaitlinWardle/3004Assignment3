import java.sql.*;
import java.sql.Date;

public class Main {
    //set up connection to sql database
    private static Connection connection;
    static String url="jdbc:postgresql://localhost:5432/Assignment3Database";
    static String user="postgres";
    static String password="xxxxxx"; //hidden
    private static Statement statement;
    public static void main(String[] args){

        //if database doesnt exist then create it
        try {
            Class.forName("org.postgresql.Driver");
            connection= DriverManager.getConnection(url,user,password);
            if (connection!=null){
                System.out.println("Connected to Database");
            }
            else{
                System.out.println("Connection Failed");
            }
            Statement statement=connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS students( student_id SERIAL, first_name text NOT NULL, last_name text NOT NULL, email text UNIQUE NOT NULL, enrollment_date date, primary key(student_id) )");
        }
        catch(Exception ex){
            System.out.println("Error when attempting to connect to Postgre-Sql");
        }
        
        //show functionality
        System.out.println("show the students originally");
        getAllStudents();

        System.out.println("add new student");
        LocalDate localDate=LocalDate.of(2024,04,01);
        Date date=Date.valueOf(localDate);
        addStudent("Caitlin","Wardle","ExampleEmail",date);
        getAllStudents();

        System.out.println("update a student");
        updateStudentEmail(6,"newEmail");
        getAllStudents();

        System.out.println("delete a student");
        deleteStudent(6);
        getAllStudents();


    }

    //function gets query results from database and prints it to screen
    public static void getAllStudents(){
        try{
            statement=connection.createStatement();
            statement.executeQuery("SELECT * FROM students");
            ResultSet result=statement.getResultSet();
            while(result.next()){
                System.out.println(result.getString("student_id")+" "+result.getString("first_name")+ " "+result.getString("last_name")+" |"+result.getString("email")+" "+result.getString("enrollment_date"));
            }
        }
        catch(Exception ex){
            System.out.println("Error when attempting to get students");
        }

    }
     //function adds student to database
    public static void addStudent(String first_name, String last_name, String email, Date enrollment_date){
        String sql = "INSERT INTO students (first_name, last_name, email,enrollment_date) VALUES (?, ?, ?,?)";

        try (PreparedStatement addStatement = connection.prepareStatement(sql)) {
            addStatement.setString(1, first_name);
            addStatement.setString(2, last_name);
            addStatement.setString(3, email);
            addStatement.setDate(4,enrollment_date);
            addStatement.executeUpdate();
        }
        catch(Exception ex){
            System.out.println("Error when attempting to add student");
        }
    }
    
    //function updates email of student in database if it matches given student_id
    public static void updateStudentEmail(Integer student_id, String new_email){
        String sql="UPDATE students SET email=? WHERE student_id=?";
        try (PreparedStatement updateStatement = connection.prepareStatement(sql)) {
            updateStatement.setString(1, new_email);
            updateStatement.setInt(2, student_id);
            updateStatement.executeUpdate();
        }
        catch(Exception ex){
            System.out.println("Error when attempting to update student email");
        }
    }
    //function deletes student row in database if it matches the student_id
    public static void deleteStudent(Integer student_id){
        String sql="DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(sql)) {
            deleteStatement.setInt(1, student_id);
            deleteStatement.executeUpdate();
        }
        catch(Exception ex){
            System.out.println("Error when attempting to delete a student");
        }
    }
}
