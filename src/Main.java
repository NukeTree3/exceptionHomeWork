import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

/*
Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробелом:
Фамилия Имя Отчество датарождения номертелефона пол
Форматы данных:
фамилия, имя, отчество - строки
дата_рождения - строка формата dd.mm.yyyy
номер_телефона - целое беззнаковое число без форматирования
пол - символ латиницей f или m.
Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, вернуть код ошибки, обработать его и
показать пользователю сообщение, что он ввел меньше и больше данных, чем требуется.
Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры. Если форматы данных не совпадают, нужно бросить
исключение, соответствующее типу проблемы. Можно использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано,
пользователю выведено сообщение с информацией, что именно неверно.
Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку должны записаться полученные данные, вида
<Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
Не забудьте закрыть соединение с файлом.
При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        while (true){
            inputData();
        }

    }

    public static void inputData() throws IOException {
        System.out.println("Введите данные в произвольном порядке следующие данные\n"+"фамилия, имя, отчество - отдельные слова\n" + "дата_рождения - строка формата dd.mm.yyyy\n" + "номер_телефона - целое беззнаковое число без форматирования\n" + "пол - символ латиницей f или m.\n");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] strings = input.split(" ");

        int c = 0;
        int cc = 0;

        boolean isFIOCorrect = false;
        boolean isNumberCorrect = false;
        boolean isGenderCorrect = false;
        boolean isBirthDayCorrect = false;

        String name = "";
        String dataToSave = "";

        for (String data: strings) {
            if(data.split("\\.").length==3){
                try {
                    String[] strings1 = data.split("\\.");
                    LocalDate localDate = LocalDate.of(Integer.parseInt(strings1[2]),Integer.parseInt(strings1[1]),Integer.parseInt(strings1[0]));
                    isBirthDayCorrect = true;
                    cc+=1;
                } catch (DateTimeException e) {
                    cc+=1;
                    System.err.println("неверные переменные времени");
                } catch (NumberFormatException ex){
                    cc+=1;
                    System.err.println("неверный формат даты");
                }

            }
            else if(data.matches("-?\\d+")){
                isNumberCorrect = true;
                cc+=1;
            }
            else if(data.equals("f") || data.equals("m")){
                isGenderCorrect = true;
                cc+=1;
            }
            else {
                name+=data;
                c+=1;
                cc+=1;
            }
            if(c==3){
                isFIOCorrect = true;
            }
            dataToSave+=data;
        }

        if (cc==6){
            if (isBirthDayCorrect && isFIOCorrect && isGenderCorrect && isNumberCorrect){
                save(dataToSave,name);
                System.out.println("Вывод  ");
            }
            else{
                System.out.println("ошибка, не хватает следующих аргументов:");
                if (!isBirthDayCorrect){
                    System.out.println("дата рождения");
                }
                if (!isFIOCorrect){
                    System.out.println("ФИО");
                }
                if (!isGenderCorrect){
                    System.out.println("пол");
                }
                if (!isNumberCorrect){
                    System.out.println("номер телефона");
                }
                System.out.println("\n");
            }

        }
        else if (cc>6){
            System.err.println("низя больше 6 аргументов");

        }
        else {
            System.err.println("низя меньше 6 аргументов");
        }
    }

    public static void save(String dataToSave, String name) throws IOException {
        String dataDownload = "";
        try (FileInputStream fileInputStream = new FileInputStream(name+".txt"); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){
            dataDownload = (String) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            System.out.println("Создание нового файла...");
        }
        FileOutputStream fileOutputStream = new FileOutputStream(name+".txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(dataDownload+"\n"+dataToSave);
        objectOutputStream.close();
        fileOutputStream.close();
    }
}