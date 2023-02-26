package fileUtil;

import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResultReader {
    List<Integer> threeInARow = new ArrayList<>();
    List<Integer> twoInARow = new ArrayList<>();
    List<Integer> centerValue = new ArrayList<>();
    List<Double> winPercentage = new ArrayList<>();

    // read from file function
    public void readFile() throws FileNotFoundException {
        File file = new File("/Users/leonbeitz/Documents/uni/s05/is/intelligentesystemehausarbeit/resources/4.txt");
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.contains("three in a row")) threeInARow.add(Integer.parseInt(line.split(" ")[0]));
            else if (line.contains("two in a row")) twoInARow.add(Integer.parseInt(line.split(" ")[0]));
            else if (line.contains("center value")) centerValue.add(Integer.parseInt(line.split(" ")[0]));
            else if (line.contains("with win"))
                winPercentage.add(Double.parseDouble(line.split(" ")[line.split(" ").length - 1]));
        }
    }

    void printResults() {
        System.out.println("threeInARow");
        System.out.println(threeInARow);
        System.out.println("twoInARow");
        System.out.println(twoInARow);
        System.out.println("centerValue");
        System.out.println(centerValue);
        System.out.println("winPercentage");
        System.out.println(winPercentage);
    }

    void getBestPlayersForEachGeneration() {
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < winPercentage.size(); i++) {
            if (winPercentage.get(i) > 60) indexList.add(i);
        }

        List<Integer> threeInARowList = new ArrayList<>();
        List<Integer> twoInARowList = new ArrayList<>();
        List<Integer> centerValueList = new ArrayList<>();
        for (
                int i :
                indexList) {
            threeInARowList.add(this.threeInARow.get(i));
            twoInARowList.add(this.twoInARow.get(i));
            centerValueList.add(this.centerValue.get(i));
        }
        System.out.println("threeInARow");
        System.out.println(threeInARowList);
        System.out.println(threeInARowList.size());
        System.out.println("twoInARow");
        System.out.println(twoInARowList);
        System.out.println(twoInARowList.size());
        System.out.println("centerValue");
        System.out.println(centerValueList);
        System.out.println(centerValueList.size());
    }

    public static void main(String[] args) {
        ResultReader fileReader = new ResultReader();
        try {
            fileReader.readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fileReader.printResults();
        fileReader.getBestPlayersForEachGeneration();
    }
}
