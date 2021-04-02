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

    //Закрытие соединения
    @Override
    public void close() {

        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    //Соединение с БД
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

    //Создание соедиения
    @Override
    public void connection() {
        Connection connection = getConnection();
    }

    //Добавление предмета
    @Override
    public void addSubject() {
        String subjectName = null;
        try {
            System.out.println("--------------------------------------");
            System.out.println("Доступные предметы: ");
            selectSubject();
            System.out.println("--------------------------------------");
            System.out.print("Введите название предмета: ");
            subjectName = scanner.next();
            Subject subject = new Subject(subjectName);

            String query = "INSERT INTO subject (subject_name) VALUES ('" + subject.getSubject() + "') ";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Предмет успешно добавлен!");
        }catch (Exception e){
            System.out.println("Предмет: " + subjectName + " уже существует!");
        }
    }

    //Вывод предмета из БД
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

    //Добавление (создание) группы
    @Override
    public void addGroup() {
        try {
            System.out.println("--------------------------------------");
            System.out.println("Доступные группы: ");
            selectGroup();
            System.out.println("--------------------------------------");
            System.out.print("Введите номер/название группы: ");
            String groupName = scanner.next();
            Group group = new Group(groupName);
            String query = "INSERT INTO groups (group_name) VALUES ('" + group.getGroupName()+ "')";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Группа успешно добавлена!");
        }catch (Exception e){
            System.out.println("Такая группа уже сущетсвует!");
        }
    }

    //Вывод группы
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

    //Добавление (создание) подгруппы
    @Override
    public void addSubGroup() {
        ArrayList<String> subgroup_names = new ArrayList<>();
        try {
            System.out.println("--------------------------------------");
            System.out.println("Доступные группы: ");
            selectGroup();
            System.out.println("--------------------------------------");
            System.out.print("Введите название номер/название группы: ");
            String groupName = scanner.next();
            try {
                Statement statement1 = connection.createStatement();
                String query = "SELECT name_subgroups, groups.group_name\n" +
                        " FROM subgroups INNER JOIN groups \n" +
                        " on subgroups.id_group = groups.id\n" +
                        " WHERE groups.group_name = '"+groupName+"'";
                ResultSet rs = statement1.executeQuery(query);
                int i = 0;
                while(rs.next()){
                    subgroup_names.add(rs.getString("name_subgroups"));
                    System.out.println(subgroup_names.get(i));
                    i++;
                }

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.print("Введите навазние подгруппы: ");
            String subGroupName = scanner.next();
            for (String item: subgroup_names) {
                if (item.equals(subGroupName)){
                    throw new RuntimeException("Такая подгруппа "+subGroupName+" в группе "+groupName+" уже существует!");
                }
            }

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

    //Метод для поиска ID группы
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

    //Вывод подгруппы
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

    //Добавление (создание) студента
    @Override
    public void addStudent() {
        try {
            System.out.print("Введите имя стундента: ");
            String studName = scanner.next();
            System.out.print("Введите фамилию студента: ");
            String studFam = scanner.next();
            selectGroup();
            System.out.print("Введите название номер/название группы: ");
            String groupName = scanner.next();


            int id = id_subgroups(groupName); // для поиска id группы
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

    //Метод для поиска ID подгруппы
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

            Statement statement1 = connection.createStatement();
            System.out.print("Введите название подгруппы - ");
            String subg = scanner.next();
            for(int j=0; j<subgroup_names.size(); j++){
                if (subgroup_names.get(j).equals(subg)) {
                    //String query2 = "SELECT subgroups.id FROM subgroups WHERE name_subgroups = '"+subg+"'";
                    String query2 = "SELECT subgroups.id FROM subgroups INNER JOIN groups on subgroups.id_group " +
                            " = groups.id WHERE groups.id = '"+id+"' AND name_subgroups = '"+subg+"'";
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

    //Вывод студента
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
                //int id_sg = rs.getInt("id_subgroups");
                System.out.println(id+": "+ name+ " "+fam+ "\t|"+nameSGR+ "\t|"+nameGR);
               // System.out.println(id+": "+ name+ " "+fam+ "\t|"+nameSGR+ "\t|"+nameGR+ "\t|"+id_sg);
            }
            rs.close();
            statement.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    //Реализация таблицы (группа, студент, предмет)
    @Override
    public void fill_group_student_subject() {

        String fam = null, name = null, subjName = null;
        try {
            System.out.println("Доступные предметы: ");
            selectSubject();
            System.out.println("--------------------------------------");
            System.out.print("Введите название предмета: ");
            subjName = scanner.next();
            System.out.println("--------------------------------------");
            System.out.println("Доступные группы: ");
            selectGroup();
            System.out.println("--------------------------------------");
            System.out.print("Введите группу: ");
            String groupName = scanner.next();
            System.out.println("--------------------------------------");
            System.out.println("Доступные студенты в группе : " + groupName);
            selectRegStudentInGroup(groupName);
            System.out.println("--------------------------------------");
            System.out.print("Введите фамилию студента: ");
            fam = scanner.next();
            System.out.print("Введите имя студента: ");
            name = scanner.next();

            int id_getStud = id_student(fam, name);
            if(catchEqualsStudents(id_getStud)){
                statement = connection.createStatement();
                int id_Group = id_subgroups(groupName);
                int id_subj = id_subjName(subjName);
                int id_stud = id_student(fam,name);

                String query = "INSERT INTO group_student_subject(id_student, id_subject, id_group) " +
                        " VALUES('"+id_stud+"', '"+id_subj+"', '"+id_Group+"')";
                statement = connection.createStatement();
                statement.executeUpdate(query);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private boolean catchEqualsStudents(int id_getStud) {
        ArrayList <Integer> id_students = new ArrayList<>();
        int id = 0;
        try {
            statement = connection.createStatement();
            String query = "SELECT id_student from group_student_subject";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                id = rs.getInt("id_student");
                id_students.add(id);
            }
            for (Integer i : id_students) {
                if(i == id_getStud){
                    throw new RuntimeException("Студент уже зарегистрирован на данный курс!");
                }
            }
            statement.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void selectRegStudentInGroup(String groupName) {
        try {
            statement = connection.createStatement();
            String query = "SELECT student.student_firstname, student.student_secondname FROM student \n" +
                    " INNER JOIN subgroups ON student.id_subgroups = subgroups.id \n" +
                    " INNER JOIN groups ON subgroups.id_group = groups.id WHERE groups.group_name = '"+groupName+"'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                String stud_name = rs.getString("student_firstname");
                String stud_secName = rs.getString("student_secondname");
                System.out.println(stud_secName+ " "+stud_name);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void addAttendance() {
        try {
            System.out.println("--------------------------------------");
            System.out.println("Доступные предметы: ");
            selectSubject();
            System.out.println("--------------------------------------");
            System.out.print("Введите название предмета: ");
            String subjName = scanner.next();
            System.out.println("--------------------------------------");
            System.out.println("Доступные группы: ");
            selectGroup();
            System.out.println("--------------------------------------");
            System.out.print("Введите группу: ");
            String groupName = scanner.next();
            System.out.println("--------------------------------------");
            System.out.println("Доступные студенты: ");
            selectRegStudentInGroup(groupName);
            System.out.println("--------------------------------------");
            System.out.print("Введите фамилию студента: ");
            String fam = scanner.next();
            System.out.print("Введите имя студента: ");
            String name = scanner.next();

            statement = connection.createStatement();
            int id_Group = id_subgroups(groupName);
            int id_subj = id_subjName(subjName);
            int id_stud = id_student(fam,name);
            int id_gss = id_GetGSS(id_Group, id_subj, id_stud);

            String userAns;
            do {
                System.out.print("Отметить успеваемость/посещаемость:  -  (y/n): ");
                userAns = scanner.next();
                if (userAns.equals("n")){
                    break;
                }
                addMarkAndCheckedStud(id_gss);
            }while(userAns.equals("y"));

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Запрос addAttendance не выполнен!");
        }
    }

    @Override
    public void selectTotalAttendance() {
        ArrayList<Integer> marks = new ArrayList<>();
        ArrayList<Integer> posehaemost = new ArrayList<>();
        double sumAVG = 0, avgMark = 0;
        int count = 0, prozPos = 0;
        int pos = 0;
        int s = 0;
        try {
            System.out.println("--------------------------------------");
            System.out.println("Доступные предметы: ");
            selectSubject();
            System.out.println("--------------------------------------");
            System.out.print("Введите название предмета: ");
            String subjName = scanner.next();
            System.out.println("--------------------------------------");
            System.out.println("Доступные группы: ");
            selectGroup();
            System.out.println("--------------------------------------");
            System.out.print("Введите группу: ");
            String groupName = scanner.next();
            System.out.println("Доступные студенты в группе : " + groupName);
            selectRegStudentInGroup(groupName);
            System.out.println("--------------------------------------");
            System.out.print("Введите фамилию студента: ");
            String fam = scanner.next();
            System.out.print("Введите имя студента: ");
            String name = scanner.next();

            int id_subj = id_subjName(subjName);
            int id_gr = id_subgroups(groupName);
            int id_st = id_student(fam, name);
            int id_gss = id_GetGSS(id_gr, id_subj, id_st);

           if(check(id_subj,id_st)){
                statement = connection.createStatement();
                String query = "SELECT mark FROM attendance WHERE id_group_student_subject = '" + id_gss + "' ";
                ResultSet rs = statement.executeQuery(query);

                while (rs.next()) {
                    int mark = rs.getInt("mark");
                    marks.add(mark);
                }
                if (marks.size() <= 0) {
                    System.out.println("У студента не оценок!");
                }else {

                    for (int i = 0; i < marks.size(); i++) {
                        sumAVG += marks.get(i);
                    }
                    avgMark = sumAVG / marks.size();

                    try {
                        Statement statement1 = connection.createStatement();
                        String qr = "select check_stud from attendance where attendance.id_group_student_subject = '"+id_gss+"'";
                        ResultSet rs1 = statement1.executeQuery(qr);
                        while (rs1.next()){
                            pos = rs1.getInt("check_stud");
                            posehaemost.add(pos);
                        }

                        for (Integer item:posehaemost) {
                            if (item==1){
                                count++;
                            }
                        }
                        prozPos = count*100/posehaemost.size();
                        rs1.close();
                        statement1.close();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }

                }
               System.out.println("--------------------------------------");
               System.out.println("Статистика: ");
               System.out.println("Предмет: " + subjName + "\n" +
                       "Студент: " + fam + " " + name + "\n" +
                       "Средняя оценка: " + avgMark+"\n"+
                       "Процент посещяемости: "+prozPos+"%");

        }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private boolean check(int id_subj, int id_st) {
        ArrayList<Integer> registrStud = new ArrayList<>();
        int ids = 0, s = 0;
        try {
            Statement statement1 = connection.createStatement();
            String query1 = "SELECT group_student_subject.id_student FROM group_student_subject WHERE group_student_subject.id_subject = '"+id_subj+"'";
            ResultSet resultSet = statement1.executeQuery(query1);
            while (resultSet.next()){
                ids = resultSet.getInt("id_student");
                registrStud.add(ids);
            }
            for (Integer item: registrStud) {
                if (id_st!=item){
                    s++;
                }else {
                    return true;
                }
            }
            int z = registrStud.size();
            if (s>=z){
                resultSet.close();
                statement1.close();
                return false;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void addMarkAndCheckedStud(int id_gss) {

        System.out.print("Введите тип занятия: ");
        String less_type = scanner.next();

        int check;
        System.out.print("Отметить посещаемость : ( 1 - был(а) | 0 - не был(а): ");
        check = scanner.nextInt();

        System.out.print("Поставить оценку? (1 - да | 0 - нет): ");
        int yn = scanner.nextInt();
        int mark = 0;
        if (yn == 1){

            System.out.print("Оценка: (1-5) - ");
            mark = scanner.nextInt();
        }

        int month, day, year;
        System.out.println("Введите дату занятия");

        System.out.print("Месяц: ");
        month = scanner.nextInt();

        System.out.print("День: ");
        day = scanner.nextInt();

        System.out.print("Год: ");
        year = scanner.nextInt();

        String dd = String.valueOf(day);
        String mm = String.valueOf(month);
        String yy = String.valueOf(year);
        String date = dd+"."+mm+"."+yy;

        try {
            statement = connection.createStatement();
            String query = "INSERT INTO attendance (check_stud, mark, lesson_date, id_group_student_subject, lesson_type) VALUES ('"+check+"','"+mark+"', '"+date+"','"+id_gss+"', '"+less_type+"')";
            statement.executeUpdate(query);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private int id_GetGSS(int id_group, int id_subj, int id_stud) {
        try {
            statement = connection.createStatement();
            String query = "SELECT id FROM group_student_subject WHERE group_student_subject.id_student = '"+id_stud+"' " +
                    " AND group_student_subject.id_subject = '"+id_subj+"' " +
                    " AND group_student_subject.id_group = '"+id_group+"'";
            ResultSet rs = statement.executeQuery(query);
            int id = rs.getInt("id");
            rs.close();
            statement.close();
            return id;

        }catch (Exception e){
            System.out.println("Данный студент не зарегистрирован!");
        }
        return 0;
    }

    //Метод для поиска ID студента по имени, фамилии
    private int id_student(String fam, String name) {
        try {
            statement = connection.createStatement();
            String query = "SELECT id FROM student WHERE student_firstname = '"+name+"' " +
                    " AND student_secondname = '"+fam+"'";
            ResultSet rs = statement.executeQuery(query);
            int id = rs.getInt("id");
            return id;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Нет такого студента!");
    }

    //Метод для поиска ID предмета по названию
    private int id_subjName(String subjName) {
        try {
            statement = connection.createStatement();
            String query = "SELECT id FROM subject WHERE subject_name = '"+subjName+"'";
            ResultSet rs = statement.executeQuery(query);
            int id = rs.getInt("id");
            return id;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Нет такого предмета!");
    }
}