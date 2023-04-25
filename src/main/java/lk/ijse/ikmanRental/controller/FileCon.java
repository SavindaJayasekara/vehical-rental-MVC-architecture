package lk.ijse.ikmanRental.controller;

import java.io.*;
import java.util.Random;

public class FileCon {
    private void write(){
        //write file

        String user="kamal";
        String detail[]={"savinda","jayasekara","dickwalla","200223801534"};
        try {
            BufferedWriter writer=new BufferedWriter(new FileWriter("E:\\ikmanrental\\src\\main\\resources\\file/"+user+".txt"));
            writer.write("thantrige32@gmail.com");

            for (String names :detail) {
                writer.write("\n"+names);
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //read file....

        try {
            String line;

            BufferedReader reder=new BufferedReader(new FileReader("E:\\ikmanrental\\src\\main\\resources\\file/"+user+".txt"));
            while ((line=reder.readLine())!=null){
                if (line.equals("savinda")){
                    System.out.println("savinda's accunt");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("can not find file....! :(");
        }

        String OTP []= new String[4];// genarate OTP

        for (int i = 0; i < 4; i++) {
            Random r=new Random();
            OTP[i]=r.nextInt(9)+"";
        }
        System.out.println(OTP[0]+OTP[1]+OTP[2]+OTP[3]);

        File folder = new File("E:\\ikmanrental\\src\\main\\resources\\file");// check file

        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }
}
