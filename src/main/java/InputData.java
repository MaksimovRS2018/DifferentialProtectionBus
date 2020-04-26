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
    private String nameFile;
    private ArrayList<SampleValues> sv = new ArrayList<SampleValues>();
    private ArrayList<RMSValues> rms = new ArrayList<RMSValues>();
    private ArrayList<Fourie> filter = new ArrayList<Fourie>();
    private OutputData od = new OutputData();
    private ArrayList<Breaker> breakers = new ArrayList<Breaker>();
    private Vector vectors = new Vector();
    private boolean t = false;
    private Logic logic = new Logic();


    public InputData(String nameFile, int numbers) {
        this.nameFile = nameFile;
        this.numbers = numbers;
    }

    public void start() throws FileNotFoundException {
        int period = 20; // millisec
        double step = 0.001;
        for (int i = 0; i < numbers; i++) {
            sv.add(new SampleValues());
            rms.add(new RMSValues());
            filter.add(new Fourie(i));
            filter.get(i).set();
            filter.get(i).setVector(vectors);
            filter.get(i).setPeriod(period);
            breakers.add(new Breaker());
        }
        try {
            //объект класса Charts для построения графика токов в отдельном окне
            Charts chartss = new Charts("Токи");
            chartss.setTimeStep(step);
            //объект класса Charts для построения графика дискретных значений в отдельном окне
            Charts chartsDiscrete = new Charts("Дискретные сигналы");
            chartsDiscrete.setTimeStep(step);
            //генерируем серии для токов
            String fun;
            for (int i = 0; i < 2*numbers+2; i++) {
                if (i < 5) {
                    fun = "М. зн." + (i + 1); //"Мгн. значение " +i+"-ого фидера"
                } else if (i < 10) {
                    fun = "Д. зн. " + (i - 4); //"Действ. значение " +i+"-ого фидера"
                } else if (i < 11){
                    fun = "Диф. ток";
                } else {
                    fun = "Торм. ток";
                }
                chartss.createAnalogChart(fun, i);
                chartss.addSeries("Фаза А", i, 0);
                chartss.addSeries("Фаза B", i, 1);
                chartss.addSeries("Фаза С", i, 2);
            }


            chartsDiscrete.createAnalogChart("trip", 0);
            chartsDiscrete.addSeries("Trip", 0, 0);
            chartsDiscrete.createAnalogChart("Str", 1);
            chartsDiscrete.addSeries("Str", 1, 0);
            chartsDiscrete.createAnalogChart("trip", 2);
            chartsDiscrete.addSeries("Blk", 2, 0);

            logic.setVectors(vectors);
            logic.setBlkSecondHarmonic(0.15);
            logic.setOd(od);
            logic.setBeginingDiffCurrent(1.082);
            logic.setCoefDrag(0.6);
            logic.setBeginingDragCurrent(0.9);

            od.setBreakers(breakers);
            od.setSetTime(0.04);
            od.setTimeStep(step);

            //путь к файлам comtrade
            comtradeName = nameFile;
            String path = "D:\\education\\Algoritms\\Лабораторная работа №2\\ОпытыComtrade\\DPB\\5 sections\\";
            String cfgname = path + comtradeName + ".cfg";
            String datName = path + comtradeName + ".dat";
            comtrCfg = new File(cfgname);
            comtrDat = new File(datName);

            //открываем cfg файл для получения коэф a и b для расчета y = ax+b
            br = new BufferedReader(new FileReader(comtrCfg));
            int lineNumber = 0, count = 0, numberData = 100;
            try {
                while ((line = br.readLine()) != null) {
//                System.out.println(line);
                    lineNumber++;
                    if (lineNumber == 2) {
                        //получаем количество аналоговых сигналов во 2 строке cfg файла "4,3A,1D"
                        numberData = Integer.parseInt(line.split(",")[1].replaceAll("A", ""));
                        //создаем double " массивы " с размерностью равной количеству
                        k1 = new double[numberData];
                        k2 = new double[numberData];
                    }
                    //коэф находятся на 3,4,5 строке это 5 и 6 элемент строки при парсинге
                    if (lineNumber > 2 && lineNumber < numberData + 3) {
                        k1[count] = Double.parseDouble(line.split(",")[5]);
                        k2[count] = Double.parseDouble(line.split(",")[6]);
                        count++;
                    }
                }
                count = 0;
                br = new BufferedReader(new FileReader(comtrDat));
                while ((line = br.readLine()) != null) {
                    count++;
                    if ((count > 100 && count < 1200)) {
                        lineData = line.split(",");
                        int b = 0;
                        int i = 0;
                        while (i < numbers) { //проходимся по всем фидерам
                            if (!t) {
                                sv.get(i).setPhA(Double.parseDouble(lineData[b + 2]) * k1[b] + k2[b]);
                                sv.get(i).setPhB(Double.parseDouble(lineData[b + 3]) * k1[b + 1] + k2[b + 1]);
                                sv.get(i).setPhC(Double.parseDouble(lineData[b + 4]) * k1[b + 2] + k2[b + 2]);
                            } else {
                                sv.get(i).setPhA(0.);
                                sv.get(i).setPhB(0.);
                                sv.get(i).setPhC(0.);
                            }
                            //вывод мгновенных значений
                            chartss.addAnalogData(i, 0, sv.get(i).getPhA());
                            chartss.addAnalogData(i, 1, sv.get(i).getPhB());
                            chartss.addAnalogData(i, 2, sv.get(i).getPhC());
                            b = b + 3; //чтобы прыгать через фазы для следующего фидера
                            //объект SV помещаем в объект filter,чтобы получать значения
                            filter.get(i).setSv(sv.get(i));
                            //объект rms помещаем в объект filter,чтобы устанавливать значения
                            filter.get(i).setRms(rms.get(i));
                            //расчет ортогональных составляющих
                            filter.get(i).calculate();
                            //вывод средних значений
                            chartss.addAnalogData(i + 5, 0, rms.get(i).getPhA());
                            chartss.addAnalogData(i + 5, 1, rms.get(i).getPhB());
                            chartss.addAnalogData(i + 5, 2, rms.get(i).getPhC());
//                            System.out.println("ФАЗА А = " + rms.get(i).getPhA());
//                            System.out.println("ФАЗА В = " + rms.get(i).getPhB());
//                            System.out.println("ФАЗА С = " + rms.get(i).getPhC());
                            i++;
                        }

                        //отсылка векторов в логику, если токи есть
                        if (!t) logic.setVectors();
                        //отключение (прекращение цикла(для имитации отключения), так как произошло срабатывание, выключатели отключены)
                        breakers.forEach(e-> t=t|(!e.isState()));
                        //диф ток пофазно
                        chartss.addAnalogData(2 * numbers, 0, logic.getDiffCurrent()[0]);
                        chartss.addAnalogData(2 * numbers, 1, logic.getDiffCurrent()[1]);
                        chartss.addAnalogData(2 * numbers, 2, logic.getDiffCurrent()[2]);
                        //ток торможения пофазно
                        chartss.addAnalogData(2 * numbers + 1, 0, logic.getCurrentDrag()[0]);
                        chartss.addAnalogData(2 * numbers + 1, 1, logic.getCurrentDrag()[1]);
                        chartss.addAnalogData(2 * numbers + 1, 2, logic.getCurrentDrag()[2]);
                        //вывод дискретных значений
                        //сигнал блокировки (преобразование boolean -> int)
                        chartsDiscrete.addAnalogData(0, 0, boolToInt(od.getTripper()));
                        chartsDiscrete.addAnalogData(1, 0, boolToInt(od.getStr()));
                        chartsDiscrete.addAnalogData(2, 0, boolToInt(od.isBlk()));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    // функция для преобразования boolean -> int
    public int boolToInt(boolean b) {
        return Boolean.compare(b, false);
    }


}
