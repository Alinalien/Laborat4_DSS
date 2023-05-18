/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smirnoff4;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author Alina
 */


public class DataManipulation {
    private static DescriptiveStatistics MakeStatistic (ArrayList<Double> s){
        DescriptiveStatistics makeStatistic = new DescriptiveStatistics();
        for (double v : s) {
            makeStatistic.addValue(v);
        }
        return makeStatistic;
    }
    
    public static ArrayList<ArrayList<Object>> Counting(ArrayList<ArrayList<Double>> samples) {
        ArrayList<ArrayList<Object>> param = new ArrayList<>();
        int max_length = -1;
        for (int i = 0; i < samples.size(); ++i){
            if (samples.get(i).size()>max_length){
                max_length = samples.get(i).size();
            }
        }
        for (int i = 0; i < samples.size(); ++i){
            while(samples.get(i).size() < max_length){
                samples.get(i).add(0.0);
            }
            System.out.println(samples.get(i).size());
        }
        for (ArrayList<Double> sample : samples) {
            param.add(CountingSample(sample));
        }
       CountingCorelation(samples, param);
        return param;
   
    }
    
    public static void CountingCorelation(ArrayList<ArrayList<Double>> samples,ArrayList<ArrayList<Object>> param ) {
        PearsonsCorrelation ps = new PearsonsCorrelation(); 
        
//        double[] xArray = new double[samples.get(0).size()];
//        samples.get(0).forEach(a -> );
//        ps.correlation(xArray, yArray);
//        Covariance c = new Covariance();
        for (int i = 0; i < samples.size(); ++i){
            ArrayList<Object> sampleParam = param.get(i);
            ArrayList<Double> s = samples.get(i);
              try {
                int next_i = (i+1)%samples.size();
                double[] X = s.stream().mapToDouble(d -> d).toArray();
                double[] Y = samples.get(next_i).stream().mapToDouble(d -> d).toArray();
                sampleParam.add(ps.correlation(X,Y));
                Covariance c = new Covariance();
                sampleParam.add(c.covariance(X,Y));
            } catch (MathIllegalArgumentException ex) {
                sampleParam.add(null);
                sampleParam.add(null);
            }
//            param.add(sampleParam);
        }
      
        
    }
    
    
    public static  ArrayList<Object> CountingSample(ArrayList<Double> sample){
            ArrayList<Object> sampleParam = new ArrayList<>();
//            ArrayList<Double> s = sample.get(i);
            
       
            DescriptiveStatistics ms = MakeStatistic(sample);
//            double count = 10000;
            
            double minElem = ms.getMin();
            double maxElem = ms.getMax();
            
            
            //1.	Рассчитать среднее геометрическое для каждой выборки 
            double meanG = ms.getGeometricMean();
            sampleParam.add((meanG));

            //2.	Рассчитать среднее арифметическое для каждой выборки 
            double mean = ms.getMean();
            sampleParam.add(mean);
            //3.	Рассчитать оценку стандартного отклонения для каждой выборки
            double standardDeviation = ms.getStandardDeviation();
            sampleParam.add((standardDeviation));
            //4.	Рассчитать размах каждой выборки                        
            sampleParam.add(((maxElem - minElem)));
            //6.	Рассчитать количество элементов в каждой выборке     
            sampleParam.add(ms.getN());
            //7.	Рассчитать дисперсию для каждой выборки
            sampleParam.add((ms.getVariance()));
            //8.	Рассчитать для каждой выборки построить доверительный интервал для мат. ожидания (Случайные числа подчиняются нормальному закону распределения)
//            sampleParam.add((ms.getPercentile(5)));
//            sampleParam.add((ms.getPercentile(95)));

            TDistribution t = new TDistribution(sample.size()-1);
            double tlevel = t.inverseCumulativeProbability(0.05);
            double conf = tlevel*standardDeviation/Math.sqrt(sample.size());
            sampleParam.add(mean - conf);
            sampleParam.add(mean + conf);
            //9.	Рассчитать коэффицент вариации для каждой выборки 
            sampleParam.add((standardDeviation/ Math.abs(mean)));
            //10.	Рассчитать максимумы и минимумы для каждой выборки
            sampleParam.add((minElem));
            sampleParam.add((maxElem));
            //5.	Рассчитать коэффициенты ковариации для всех пар случайных чисел
            PearsonsCorrelation ps = new PearsonsCorrelation(); 
            
        
        
        return sampleParam;        
    }
    

    public static DefaultTableModel MakeDTM(ArrayList<ArrayList<Object>> param, 
        ArrayList<String> name, String[] nameParam, int next){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Параметры");
        for (int i = 0; i < name.size(); ++i){
            model.addColumn(name.get(i));
        }
        
        for (int i = 0; i < nameParam.length; i++) {
            Object[] values = new Object[name.size()+1];
            values[0] = nameParam[i];
            for (int j = 0; j < name.size(); j++){
                values[j+1] = param.get(j).get(i + next);
                System.out.print(values[j+1] + " ");
            }
            System.out.println("***");
            model.addRow(values);
        }
        return model;
    }
}
