import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 *
 * @author Marcos
 */
class Main {
    
    public static void main(String[] args) throws IOException {
        
        double[][] tfidf;
        int[] numberOfFilesContainOcurrency;
        int totalDocAmount;
        double[] similaryArray, tfidfLastDoc;
        int[][] dataArray;
        
        Data data = new Data();
        
        //dataArray = data.readFile("3.in");
        dataArray = data.readInput();

        totalDocAmount = dataArray.length-1;
        tfidf = new double[totalDocAmount][26];
        tfidfLastDoc = new double[26];
        similaryArray = new double[totalDocAmount];

        
        /*for (int i = 0; i < dataArray.length; i++) {
            for (int j = 0; j < dataArray[i].length; j++) {
                System.out.print(dataArray[i][j]+" ");
            }
            System.out.println("");
        }*/
        
        numberOfFilesContainOcurrency = data.getNumberOfFilesContainOcurrency();
        
       /* System.out.println("numberOfFilesContainOcurrency ");
        for (int i = 0; i < numberOfFilesContainOcurrency.length; i++) {
            System.out.print(numberOfFilesContainOcurrency[i]+" ");
        }*/
            
        tfidf = tfidfCalculation(dataArray, totalDocAmount, numberOfFilesContainOcurrency);
        
        /*System.out.println("\ntfidf ");
        for (int i = 0; i < tfidf.length; i++) {
            for (int j = 0; j < tfidf[i].length; j++) {
                System.out.print(tfidf[i][j]+" ");
            }
            System.out.println("");
        }*/
        
        for (int i = 0; i < dataArray[totalDocAmount-1].length; i++) {
            tfidfLastDoc[i] = (double) dataArray[totalDocAmount][i];
        }
        
        /*System.out.println("\ntfidfLastDoc ");
        for (int i = 0; i < tfidfLastDoc.length; i++) {
            System.out.print(tfidfLastDoc[i]+" ");
        }*/
        
        similaryArray = similarityCalculation(totalDocAmount, tfidf, tfidfLastDoc);
            
        double max = Double.MIN_VALUE;
        int aux = 0;
        
        for (int i = 0; i < similaryArray.length; i++) {
            max = Double.MIN_VALUE;
            aux = 0;
            for (int j = 0; j < similaryArray.length; j++) {
                if (similaryArray[j] >= max) {
                    max = similaryArray[j];
                    aux = j;
                }
            }
            
            similaryArray[aux] = Double.MIN_VALUE;
            
            aux++;
            
            System.out.println("D"+aux+":"+format(max).replace(",", "."));

        }
        
    }
    
    public static String format(double x) {  
        DecimalFormat df = new DecimalFormat("#0.00");  
        return df.format(x);
    }
    
    public static double[] similarityCalculation (int totalDocAmount, double[][] tfidf, double[] tfidfLastDoc) {
        
        double[] similatiryArray = new double[totalDocAmount];
        double sum = 0;
        double firstSqrt = 0;
        double secondSqrt = 0;
        
        for (int i = 0; i < totalDocAmount; i++) {
            sum = 0;
            firstSqrt = 0;
            secondSqrt = 0;
            for (int j = 0; j < 26; j++) {
                sum = sum + (double) (tfidf[i][j] * tfidfLastDoc[j]);
                firstSqrt = firstSqrt + (double) tfidf[i][j]*tfidf[i][j];
                secondSqrt = secondSqrt + (double) tfidfLastDoc[j]*tfidfLastDoc[j];
            }
            similatiryArray[i] = Math.floor((((double) sum / (double) (Math.sqrt(firstSqrt) * (Math.sqrt(secondSqrt)))))*10000)/100.00;

        }
        
        return similatiryArray;
    }
    
    public static double[][] tfidfCalculation (int[][] data, int totalDocAmount, int[] numberOfFilesContainOcurrency) {
        double[][] tfidf = new double[totalDocAmount][numberOfFilesContainOcurrency.length];
        
        for (int i = 0; i < totalDocAmount; i++) {
            for (int j = 0; j < 26; j++) {
                if (numberOfFilesContainOcurrency[j] == 0) {
                    tfidf[i][j] =  0;
                } else {
                    tfidf[i][j] =  (double) data[i][j] * (double) (totalDocAmount/numberOfFilesContainOcurrency[j]); 
                }
            }
        }
        return tfidf;
    }
    

}

class Data {
    
    private FileReader fileReader;
    private int numberOfDocs;
    private int[] ocurrencyCounter;
    private int[] numberOfFilesContainOcurrency;
    private boolean[] bool = new boolean[26];
    private int data[][];
    private int counter = 0;

    
    public int[][] readFile (String filePath) {

        numberOfFilesContainOcurrency = new int[26];
        ocurrencyCounter = new int[26];
        
        try {
            fileReader = new FileReader(filePath);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            this.numberOfDocs = Integer.parseInt(line);
            data = new int[numberOfDocs][26];
            
            while ((line = bufferedReader.readLine()) != null) {
                String split[] = line.split(" ");
                
                switchMethod(split);
            }

            return data;
        } catch (FileNotFoundException ex) {
            System.out.println("Erro ao ler o arquivo.");
        } catch (IOException ex) {
            System.out.println("Falha ao ler uma nova linha.");
        }
        return null;
    }
    
    public int [][] readInput () throws IOException {
        
        numberOfFilesContainOcurrency = new int[26];
        ocurrencyCounter = new int[26];
        
        BufferedReader reader =  
                   new BufferedReader(new InputStreamReader(System.in)); 
        String line = reader.readLine(); 
        this.numberOfDocs = Integer.parseInt(line);
        data = new int[numberOfDocs][26];
            
        while (counter != numberOfDocs) {
            line = reader.readLine(); 
            String split[] = line.split(" ");
            switchMethod(split);
        }

        return data;
    }
    
    public void switchMethod (String split[]) {
        if (!Character.isAlphabetic(split[0].charAt(0))) {
            if (counter < numberOfDocs-1) {      
                for (int i = 0; i < bool.length; i++) {
                    if (bool[i]) {
                        numberOfFilesContainOcurrency[i]++;
                    }
                } 
            }

            counter++;

            for (int i = 0; i < bool.length; i++) {
                bool[i] = false;
            }

        } else {
            for (int i = 0; i < split.length; i++) {
                switch (split[i].charAt(0)) {
                    case 'A':
                        bool[0] = true;
                        data[counter][0]++;
                        if (counter < numberOfDocs-1) {
                            ocurrencyCounter[0]++;
                        }
                        break;
                    case 'B':
                        bool[1] = true;
                        data[counter][1]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[1]++;
                        }
                        break;
                    case 'C':
                        bool[2] = true;
                        data[counter][2]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[2]++;
                        }
                        break;
                    case 'D':
                        bool[3] = true;
                        data[counter][3]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[3]++;
                        }
                        break;
                    case 'E':
                        bool[4] = true;
                        data[counter][4]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[4]++;
                        }
                        break;
                    case 'F':
                        bool[5] = true;
                        data[counter][5]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[5]++;
                        }
                        break;
                    case 'G':
                        bool[6] = true;
                        data[counter][6]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[6]++;
                        }
                        break;
                    case 'H':
                        bool[7] = true;
                        data[counter][7]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[7]++;
                        }
                        break;
                    case 'I':
                        bool[8] = true;
                        data[counter][8]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[8]++;
                        }
                        break;
                    case 'J':
                        bool[9] = true;
                        data[counter][9]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[9]++;
                        }
                        break;
                    case 'K':
                        bool[10] = true;
                        data[counter][10]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[10]++;
                        }
                        break;
                    case 'L':
                        bool[11] = true;
                        data[counter][11]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[11]++;
                        }
                        break;
                    case 'M':
                        bool[12] = true;
                        data[counter][12]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[12]++;
                        }
                        break;
                    case 'N':
                        bool[13] = true;
                        data[counter][13]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[13]++;
                        }
                        break;
                    case 'O':
                        bool[14] = true;
                        data[counter][14]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[14]++;
                        }
                        break;
                    case 'P':
                        bool[15] = true;
                        data[counter][15]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[15]++;
                        }
                        break;
                    case 'Q':
                        bool[16] = true;
                        data[counter][16]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[16]++;
                        }
                        break;
                    case 'R':
                        bool[17] = true;
                        data[counter][17]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[17]++;
                        }
                        break;
                    case 'S':
                        bool[18] = true;
                        data[counter][18]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[18]++;
                        }
                        break;
                    case 'T':
                        bool[19] = true;
                        data[counter][19]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[19]++;
                        }
                        break;
                    case 'U':
                        bool[20] = true;
                        data[counter][20]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[20]++;
                        }
                        break;
                    case 'V':
                        bool[21] = true;
                        data[counter][21]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[21]++;
                        }
                        break;
                    case 'W':
                        bool[22] = true;
                        data[counter][22]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[22]++;
                        }
                        break;
                    case 'X':
                        bool[23] = true;
                        data[counter][23]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[23]++;
                        }
                        break;
                    case 'Y':
                        bool[24] = true;
                        data[counter][24]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[24]++;
                        }
                        break;
                    case 'Z':
                        bool[25] = true;
                        data[counter][25]++;
                        if (counter < numberOfDocs-1) {                                
                            ocurrencyCounter[25]++;
                        }
                        break;
                }
                

            }

        }
    }

    /**
     * @return the ocurrencyCounter
     */
    public int[] getOcurrencyCounter() {
        return ocurrencyCounter;
    }
    
    public int[] getNumberOfFilesContainOcurrency() {
        return numberOfFilesContainOcurrency;
    }
    
}


