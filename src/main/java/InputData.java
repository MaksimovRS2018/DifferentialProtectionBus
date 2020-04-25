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
    private ArrayList<OutputData> od = new ArrayList<OutputData>();
    private Vector vectors = new Vector();
    private boolean t = true;
    private Logic logic = new Logic();



    public InputData(String nameFile, int numbers) {
        this.nameFile = nameFile;
        this.numbers = numbers;
    }

    public  void start() throws FileNotFoundException {

        for (int i =0;i<numbers;i++){
            sv.add(new SampleValues());
            rms.add(new RMSValues());
            filter.add(new Fourie(i));
            filter.get(i).set();
            filter.get(i).setVector(vectors);
            od.add(new OutputData());
        }
        try {

         Charts chartss = new Charts("Токи");
         Charts chartsDiscrete = new Charts("Дискретные сигналы");

        String fun;
        for(int i = 0; i < 11; i++) {
            if (i < 5) {
                fun = "Мг. зн." +(i+1); //"Мгн. значение " +i+"-ого фидера"
            } else if (i < 10){
                fun = "Д. зн. " +(i-4); //"Действ. значение " +i+"-ого фидера"
            } else {
                fun = "Диф. ток";
            }
            chartss.createAnalogChart(fun,i);
            chartss.addSeries("Фаза А", i, 0);
            chartss.addSeries("Фаза B", i, 1);
            chartss.addSeries("Фаза С", i, 2);
        }

        for (int i = 0; i < numbers; i++) {
            chartsDiscrete.createAnalogChart("Trip" + (i+1),i);
            chartsDiscrete.addSeries("Trip" + (i+1), i, 0);
            chartsDiscrete.addSeries("Str" + (i+1), i, 1);
        }
        chartsDiscrete.createAnalogChart("Blk" ,numbers);
        chartsDiscrete.addSeries("Blk", numbers, 0);

        logic.setVectors(vectors);
        logic.setBlkSecondHarmonic(0.15);
        logic.setOd(od);
        logic.setBeginingDiffCurrent(0.9);


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
            count =0;
            br = new BufferedReader(new FileReader(comtrDat));
            while ((line=br.readLine())!=null) {
                count++;
                if ((count > 100 && count < 1200)) {

                    lineData = line.split(",");
                    int b = 0;
                    int i = 0;
                    while (i < numbers ) { //проходимся по всем фидерам
                        if (t) {
                            sv.get(i).setPhA(Double.parseDouble(lineData[b + 2]) * k1[b] + k2[b]);
                            sv.get(i).setPhB(Double.parseDouble(lineData[b + 3]) * k1[b + 1] + k2[b + 1]);
                            sv.get(i).setPhC(Double.parseDouble(lineData[b + 4]) * k1[b + 2] + k2[b + 2]);
                            chartss.addAnalogData(i, 0, sv.get(i).getPhA());
                            chartss.addAnalogData(i, 1, sv.get(i).getPhB());
                            chartss.addAnalogData(i, 2, sv.get(i).getPhC());
                            b = b + 3; //чтобы прыгать через фазы для следующего фидера
                            filter.get(i).setSv(sv.get(i));//объект SV помещаем в объект filter,чтобы получать значения
                            filter.get(i).setRms(rms.get(i));//объект rms помещаем в объект filter,чтобы устанавливать значения
                            filter.get(i).calculate(); //расчет ортогональных составляющих
                            chartss.addAnalogData(i + 5, 0, rms.get(i).getPhA());
                            chartss.addAnalogData(i + 5, 1, rms.get(i).getPhB());
                            chartss.addAnalogData(i + 5, 2, rms.get(i).getPhC());
                        } else {
                            //имитация отключения
                            chartss.addAnalogData(i, 0, 0.);
                            chartss.addAnalogData(i, 1, 0.);
                            chartss.addAnalogData(i, 2, 0.);
                            chartss.addAnalogData(i + 5, 0, 0.);
                            chartss.addAnalogData(i + 5, 1, 0.);
                            chartss.addAnalogData(i + 5, 2, 0.);

                        }
                        //сигнал срабатывания защиты
                        chartsDiscrete.addAnalogData(i, 0, boolToInt(od.get(i).getTripper()));
                        //сигнал пуска защиты
                        chartsDiscrete.addAnalogData(i, 1, boolToInt(od.get(i).getStr()));
                        i++;
                    }

                    //отсылка векторов в логику
                    logic.setVectors();
                    //отключение (прекращение цикла(для имитация отключение), так как произошла срабатывание)
                    t = t & (!od.get(0).getTripper());
                    //диф ток пофазно
                    chartss.addAnalogData(2*numbers, 0, logic.getDiffCurrent()[0]);
                    chartss.addAnalogData(2*numbers, 1, logic.getDiffCurrent()[0]);
                    chartss.addAnalogData(2*numbers, 2, logic.getDiffCurrent()[0]);
                    //сигнал блокировки
                    chartsDiscrete.addAnalogData(5, 0, boolToInt(logic.isBlock_2harmonic()));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static int boolToInt(boolean b) {
        return Boolean.compare(b, false);
    }



}
