/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smirnoff4;

import java.util.ArrayList;

/**
 *
 * @author Alina
 */
public class Storage {
    String[] nameParam = {"Среднее геометрическое", "Среднее арифметическое", "Стандартное отклонение",
                          "Размах", "Количество элементов", "Дисперсия", "Доверительный низ", "Доверительный верх", 
                          "Коэффициент вариации", "Максимум", "Минимум" };
    String[] nameParam2 = {"Корреляция", "Ковариация"};
    ArrayList<ArrayList<Double>> samples;
    ArrayList<String> name;
    ArrayList<String> name2;
    ArrayList<ArrayList<Object>> result;
 
    public Storage(){        
        samples = new ArrayList<>();
        result = new ArrayList<>();
        name = new ArrayList<>();
        name2 = new ArrayList<>();
    }

    public String[] getNameParam() {
        return nameParam;
    }

    public void setNameParam(String[] nameParam) {
        this.nameParam = nameParam;
    }

    public String[] getNameParam2() {
        return nameParam2;
    }

    public void setNameParam2(String[] nameParam2) {
        this.nameParam2 = nameParam2;
    }

    public ArrayList<ArrayList<Double>> getSamples() {
        return samples;
    }

    public void setSamples(ArrayList<ArrayList<Double>> samples) {
        this.samples = samples;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
        fillName2();
    }

    public ArrayList<String> getName2() {
        return name2;
    }

    public void setName2(ArrayList<String> name2) {
        this.name2 = name2;
    }

    public ArrayList<ArrayList<Object>> getResult() {
        return result;
    }

    public void setResult(ArrayList<ArrayList<Object>> result) {
        this.result = result;
    }
    
    
    private void fillName2(){
        for (int i = 0; i < name.size(); ++i){
            name2.add(name.get(i%name.size()) + " & " + name.get((i + 1)%name.size()));
        }
    }
    
    public void clearData(){
        samples.clear();
        name.clear();
        name2.clear();
    }
}
