import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class InputData {

    public File comtrCfg, comtrDat;
    private BufferedReader br;
    private String line;
    private String[] lineData;
    private double[] k1;
    private double[] k2;
    private String comtradeName;
    private int numbers;
    private String path;
    private String cfgname = path+comtradeName+".cfg";
    private  String datName = path+comtradeName+".dat";
    private String nameFile;
    private ArrayList<SampleValues> sv = new ArrayList<SampleValues>();
    private ArrayList<RMSValues> rms = new ArrayList<RMSValues>();
    private ArrayList<Fourie> filter = new ArrayList<Fourie>();
    private Vector vectors = new Vector();



    public InputData(String nameFile, int numbers) {
        this.nameFile = nameFile;
        this.numbers = numbers;
    }



//    private RMSValues[] rms = new RMSValues[numbers];
    //    private Filter filter = new MiddleValue(); //rms
    private Logic logic = new Logic();
    private OutputData od = new OutputData();



    public  void start() throws FileNotFoundException {

        for (int i =0;i<numbers;i++){
            sv.add(new SampleValues());
            rms.add(new RMSValues());
            filter.add(new Fourie(i));
            filter.get(i).set();
        }

        vectors.setLogic(logic);



        comtradeName = nameFile;
        path = "D:\\education\\Algoritms\\Лабораторная работа №2\\ОпытыComtrade\\DPB\\5 sections\\";

        cfgname = path+comtradeName+".cfg";
        datName = path+comtradeName+".dat";
        comtrCfg = new File(cfgname);
        comtrDat = new File(datName);

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
                        sv.get(i).setPhA(Double.parseDouble(lineData[b+2]) * k1[b] + k2[b]);
                        sv.get(i).setPhB(Double.parseDouble(lineData[b+3]) * k1[b+1] + k2[b+1]);
                        sv.get(i).setPhC(Double.parseDouble(lineData[b+4]) * k1[b+2] + k2[b+2]);
                        Charts.addAnalogData(i, 0, sv.get(i).getPhA());
                        Charts.addAnalogData(i, 1, sv.get(i).getPhB());
                        Charts.addAnalogData(i, 2, sv.get(i).getPhC());
                        b = b+3;
                        filter.get(i).setSv(sv.get(i));                        //объект SV помещаем в объект filter,чтобы получать значения
                        filter.get(i).setRms(rms.get(i));                         //объект rms помещаем в объект filter,чтобы устанавливать значения
                        filter.get(i).calculate();
//                        ArrayList<double[]> x = new ArrayList<double[]>();
//                        ArrayList<double[]> y = new ArrayList<double[]>();
//                        for (int j =0; j<filter.size();j++){
//                            x.add(filter.get(j).getAk1());
//                            y.add(filter.get(j).getBk1());
//                        }




                        Charts.addAnalogData(i+5, 0, rms.get(i).getPhA());
                        Charts.addAnalogData(i+5, 1, rms.get(i).getPhB());
                        Charts.addAnalogData(i+5, 2, rms.get(i).getPhC());
                    }

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
