package src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Simpleton extends Classifier {

	public Simpleton(String namesFilepath) {
		super(namesFilepath);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void train(String trainingDataFilpath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void makePredictions(String testDataFilepath){
		// TODO Auto-generated method stub
		try{
		StringTokenizer st ;
        BufferedReader TSVFile = new BufferedReader(new FileReader(testDataFilepath));
       
        String dataRow = TSVFile.readLine(); // Read first line.
        int rowcount=0;
        int correct=0;
        String last="";
        while (dataRow != null){
        	st = new StringTokenizer(dataRow," ");
            
            while(st.hasMoreElements()){
                last=st.nextElement().toString();
            }
//            for (String item:dataArray) { 
//                System.out.print(item + "  "); 
//            }
//            System.out.println(); // Print the data line.
            boolean res=last.equals("<=50K");
//        	System.out.println("1 "+res);
            if(res)
            	correct++;
            dataRow = TSVFile.readLine(); // Read next line of data.
            rowcount++;
        }
        // Close the file once all data has been read.
        TSVFile.close();
        
        System.out.println("Correct "+correct+" Out of:"+rowcount);
        System.out.println((double)correct/rowcount);
		}
		catch(IOException e){
			System.out.println(e.getStackTrace()+"ERROR");
		}
	}

}
