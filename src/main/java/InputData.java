import java.io.*;
import java.util.Arrays;

public class InputData {

    public File comtrCfg, comtrDat;
    private BufferedReader br;
    private String line;
    private String[] lineData;
    private double[] k1;
    private double[] k2;
    private boolean t = false;
    private String comtradeName;
    private int numbers;
    private String path;
    private String cfgname = path+comtradeName+".cfg";
    private  String datName = path+comtradeName+".dat";
    private String nameFile;



    public InputData(String nameFile, int numbers) {
        this.nameFile = nameFile;
        this.numbers = numbers;
    }

    private SampleValues sv1 = new SampleValues();
    private SampleValues sv2 = new SampleValues();
    private SampleValues sv3 = new SampleValues();
    private SampleValues sv4 = new SampleValues();
    private SampleValues sv5 = new SampleValues();

    private RMSValues rms1 = new RMSValues();
    private RMSValues rms2 = new RMSValues();
    private RMSValues rms3 = new RMSValues();
    private RMSValues rms4 = new RMSValues();
    private RMSValues rms5 = new RMSValues();



    private Fourie filter = new Fourie(); //фурье
//    private RMSValues[] rms = new RMSValues[numbers];
    //    private Filter filter = new MiddleValue(); //rms
    private Logic logic = new Logic();
    private OutputData od = new OutputData();



    public  void start() throws FileNotFoundException {
        SampleValues[] sv = new SampleValues[numbers];
        sv[0] = sv1;
        sv[1] = sv2;
        sv[2] = sv3;
        sv[3] = sv4;
        sv[4] = sv5;

        RMSValues[] rms = new RMSValues[numbers];
        rms[0] = rms1;
        rms[1] = rms2;
        rms[2] = rms3;
        rms[3] = rms4;
        rms[4] = rms5;

        comtradeName = nameFile;
        path = "D:\\education\\Algoritms\\Лабораторная работа №2\\ОпытыComtrade\\DPB\\5 sections\\";
        t = false;

        cfgname = path+comtradeName+".cfg";
        datName = path+comtradeName+".dat";
        comtrCfg = new File(cfgname);
        comtrDat = new File(datName);
        filter.set();
        filter.setSv(sv); //объект SV помещаем в объект filter,чтобы получать значения
        filter.setRms(rms); //объект rms помещаем в объект filter,чтобы устанавливать значения

        //открываем cfg файл для получения коэф a и b для расчета y = ax+b
        br = new BufferedReader(new FileReader(comtrCfg));
        int lineNumber =0, count =0, numberData = 100;
        try {
            while ((line=br.readLine())!=null)  {
//                System.out.println(line);
                lineNumber++;
                if (lineNumber ==2) {
                    //получаем количество аналоговых сигналов во 2 строке cfg файла "4,3A,1D"
                    numberData = Integer.parseInt(line.split(",")[1].replaceAll("A",""));
                    //создаем double " массивы " с размерностью равной количеству
                    k1 = new double[numberData];
                    k2 = new double[numberData];
                }
                //коэф находятся на 3,4,5 строке это 5 и 6 элемент строки при парсинге
                if (lineNumber>2 && lineNumber <numberData+3) {
                    k1[count] = Double.parseDouble(line.split(",")[5]);
                    k2[count] = Double.parseDouble(line.split(",")[6]);
                    count++;
                };
            }
            System.out.println(Arrays.toString(k1));
            count =0;
//            открываем data файл для получения значений
            br = new BufferedReader(new FileReader(comtrDat));
            while ((line=br.readLine())!=null) {
                count++;
                if ((count > 100 && count < 1200)) {
                    lineData = line.split(",");
                    int b = 0;
                    for(int i = 0; i < numbers; i++) {
                        sv[i].setPhA(Double.parseDouble(lineData[b+2]) * k1[b] + k2[b]);
                        sv[i].setPhB(Double.parseDouble(lineData[b+3]) * k1[b+1] + k2[b+1]);
                        sv[i].setPhC(Double.parseDouble(lineData[b+4]) * k1[b+2] + k2[b+2]);
                        Charts.addAnalogData(i, 0, sv[i].getPhA());
                        Charts.addAnalogData(i, 1, sv[i].getPhB());
                        Charts.addAnalogData(i, 2, sv[i].getPhC());
                        b = b+3;
                    }

                    filter.calculate();
                    for(int i = 0; i < numbers; i++) {
                        Charts.addAnalogData(i+5, 0, rms[i].getPhA());
                        Charts.addAnalogData(i+5, 1, rms[i].getPhB());
                        Charts.addAnalogData(i+5, 2, rms[i].getPhC());
                    }

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
