package com.company.services.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.company.models.Group;
import com.company.models.Student;
import com.company.models.SubGroups;
import com.company.models.Subject;
import com.company.services.StudSubjectServices;

public class StudSubjectServicesImpl implements StudSubjectServices {

    Scanner scanner = new Scanner(System.in);

    Connection connection;
    Statement statement;

    @Override
    public void close() {
        try{
            connection.close();
        }catch(Exception e)
        {
            System.out.print(e.getMessage());
        }
    }
    @Override
    public void addSubject() {

        try {
            System.out.print("Введите название предмета: ");
            String subjectName = scanner.next();
            Subject subject = new Subject(subjectName);

            String query = "INSERT INTO subject (subject_name) VALUES ('" + subject.getSubject() + "') ";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Предмет успешно добавлен!");
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }

    @Override
    public void connection() {
        Connection connection = getConnection();
    }

    @Override
    public void selectSubject() {
        try {
            statement  = connection.createStatement ();
            String query = "SELECT id, subject_name FROM subject";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("subject_name");
                System.out.println(id+"\t| "+ name);
            }
            rs.close();
            statement.close();
//            connection.close();
        }catch(Exception e)
        {
            System.out.print(e.getMessage());
        }
    }

    @Override
    public void addGroup() {
        try {
            System.out.print("Введите номер/название группы: ");
            String groupName = scanner.next();
            Group group = new Group(groupName);
            String query = "INSERT INTO groups (group_name) VALUES ('" + group.getGroupName()+ "')";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Группа успешно добавлена!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void selectGroup() {
        try {
            statement = connection.createStatement();
            String query = "SELECT id, group_name FROM groups";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String groupName = resultSet.getString("group_name");
                System.out.println(id+"\t| "+ groupName);
            }
            resultSet.close();
            statement.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addSubGroup() {
        try {
            System.out.print("Введите название номер/название группы: ");
            String groupName = scanner.next();
            System.out.print("Введите навазние подгруппы: ");
            String subGroupName = scanner.next();

            int id = id_subgroups(groupName);

            SubGroups subGroups = new SubGroups(subGroupName, id);
            String query = "INSERT INTO subgroups(name_subgroups, id_group) " +
                    "VALUES ('" + subGroups.getSubGroupName() + "', '" + id_subgroups(groupName) + "')";
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public int id_subgroups(String groupName) {
        int id = 0;
        try {
            statement = connection.createStatement();
            String query = "SELECT id FROM groups " +
                    "WHERE groups.group_name = '"+groupName+"'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                id = rs.getInt("id");
                return id;
            }
            rs.close();
            statement.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Нет группы с таким названием");
    }

    @Override
    public void selectSubGroup() {
        try {
            statement = connection.createStatement();
            String query = "SELECT id, name_subgroups, id_group FROM subgroups";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name_subgroups");
                System.out.println(id+ "\t|"+ name);
            }
            rs.close();
            statement.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addStudent() {
        try {
            System.out.print("Введите имя стундента: ");
            String studName = scanner.next();
            System.out.print("Введите фамилию студента: ");
            String studFam = scanner.next();
          System.out.print("Введите название номер/название группы: ");
            String groupName = scanner.next();

            int id = id_subgroups(groupName); // для поиска id группы
            //int idSubGr = id_groupSub(groupName,id);
            int idSubGr = id_groupSub(id);

            Student student = new Student(studName, studFam, idSubGr);
            String query  = "INSERT INTO student (student_firstname, student_secondname, id_subgroups)" +
                    " VALUES ('"+student.getFirstName()+"','"+student.getSecondName()+"','"+student.getId_subgroup()+"')";

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    private int id_groupSub(int id) {
        try{
            statement = connection.createStatement();
            //String query = "SELECT name_subgroups FROM subgroups  ";
            String query = "SELECT name_subgroups, groups.group_name\n" +
                    " FROM subgroups INNER JOIN groups \n" +
                    " ON subgroups.id_group = groups.id " +
                    " WHERE groups.id = '"+id+"'";

            ResultSet rs = statement.executeQuery(query);

            ArrayList<String> subgroup_names = new ArrayList<>();
            int i=0;
            while(rs.next()){
                subgroup_names.add(rs.getString("name_subgroups"));
                System.out.println(subgroup_names.get(i));
                i++;
            }
            //
            //SELECT subgroups.id, groups.id FROM subgroups INNER JOIN groups on subgroups.id_group = groups.id WHERE groups.id = 1 AND
            //name_subgroups = '(b)'

            Statement statement1 = connection.createStatement();
            System.out.print("Введите название подгруппы - ");
            String subg = scanner.next();
            for(int j=0; j<subgroup_names.size(); j++){
                if (subgroup_names.get(j).equals(subg)) {
                    //String query2 = "SELECT subgroups.id FROM subgroups WHERE name_subgroups = '"+subg+"'";
                    String query2 = "SELECT subgroups.id FROM subgroups INNER JOIN groups on subgroups.id_group = groups.id WHERE groups.id = '"+id+"' AND name_subgroups = '"+subg+"'";
                    ResultSet rsSG = statement1.executeQuery(query2);
                    while (rsSG.next()){
                        int idsg = rsSG.getInt("id");
                        return idsg;
                    }
                }
            }

            rs.close();
            statement.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Такой подгруппы нет");

    }

    @Override
    public void selectStudent() {
        try {
            statement  = connection.createStatement ();
           // String query = "SELECT id, student_firstname, student_secondname, id_subgroups FROM student";
            String query = "SELECT  student.id , student_firstname, student_secondname, subgroups.name_subgroups, groups.group_name, id_subgroups\n" +
                    " from subgroups INNER JOIN student\n" +
                    " on student.id_subgroups = subgroups.id  INNER JOIN groups on\n" +
                    " subgroups.id_group = groups.id";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("student_firstname");
                String fam = rs.getString("student_secondname");
                String nameSGR = rs.getString("name_subgroups");
                String nameGR = rs.getString("group_name");
                int id_sg = rs.getInt("id_subgroups");
                System.out.println(id+"\t|"+ name+ "\t"+fam+ "\t|"+nameSGR+ "\t|"+nameGR+ "\t|"+id_sg);
            }
            rs.close();
            statement.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private Connection getConnection() {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:D:\\COURSEWORK\\course.db");
            return connection;

        }catch (Exception e){
            System.out.print(e.getMessage());
        }
        return null;
    }
}
