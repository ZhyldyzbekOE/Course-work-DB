package com.company;
import java.util.Scanner;
import com.company.services.StudSubjectServices;
import com.company.services.impl.StudSubjectServicesImpl;

public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        StudSubjectServices studSubjectServices = new StudSubjectServicesImpl();

        while (true){
            studSubjectServices.connection();
            System.out.println("---------------------------------------------------------------------------------------");
            System.out.println("Выберите дейтсвие: ");
            System.out.println("" +
                    "1. Создать новый предмет.\n" + // +
                    "2. Создать новую группу.\n" + // +
                    "3. Создать подруппу.\n" +    // -
                    "4. Создать (добавить) студента к группе.\n" + // +
                    "5. Регистрация студента на предмет.\n" + // +
                    "6. Отметить успеваемость/посещаемость.\n" + // -
                    "7. Выход.");
            System.out.print("Ваш выбор: ");
            byte choose = scan.nextByte();

            if (choose == 1){
                System.out.println("-------------------Создание нового предмета-------------------");
                studSubjectServices.addSubject();
            }else if (choose == 2){
                System.out.println("-------------------Создание новой группы-------------------");
                studSubjectServices.addGroup();
            }else if (choose == 3){
                System.out.println("-------------------Создание новой подгруппы-------------------");
                studSubjectServices.addSubGroup();
            }else if (choose == 4){
                System.out.println("-------------------Создание (добавление) студента к группе-------------------");
                studSubjectServices.addStudent();
            }else if (choose == 5){
                System.out.println("-------------------Регистрация студента на предмет-------------------");
                studSubjectServices.fill_group_student_subject();
            }else if (choose == 6){
                System.out.println("-------------------Успеваемость/Посещаемость-------------------");
                studSubjectServices.addAttendance();
            }else if (choose == 7){
                System.out.println("-------------------До свидания!-------------------");
                break;
            }else {
                System.out.println("Некорректная команда!");
            }
        }
        studSubjectServices.close();

//        studSubjectServices.connection();
////        studSubjectServices.addSubject();
//        studSubjectServices.selectSubject();
////        studSubjectServices.addGroup();
//       studSubjectServices.selectGroup();
////        studSubjectServices.addSubGroup();
//       studSubjectServices.selectSubGroup();
////        studSubjectServices.addStudent();
//     studSubjectServices.selectStudent();
////
//       // studSubjectServices.fill_group_student_subject();
//        studSubjectServices.addAttendance();
//        studSubjectServices.close();
    }
}
