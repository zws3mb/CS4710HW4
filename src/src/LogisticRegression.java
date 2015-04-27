package src;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;



public class LogisticRegression extends Classifier {
	public ArrayList<HashMap<String,Double>> lex;
	
	public LogisticRegression(String namesFilepath) {
		super(namesFilepath);
		lex = new ArrayList<HashMap<String,Double>>();
		try {
			createLexicon(readNames(namesFilepath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}
	private void createLexicon(ArrayList<ArrayList<String>> names){
		System.out.println(names);
		for(ArrayList<String> row : names){
			double ind =0;
			HashMap<String, Double> temp = new HashMap<String,Double>();
			for(String ele : row){
				temp.put(ele, ind);
				ind++;
					
			}
			lex.add(temp);
		}
		HashMap<String, Double> temp = lex.get(0);
		lex.remove(0);
		lex.add(temp);
		
		System.out.println(lex);
	}
	private ArrayList readNames(String filepath) throws IOException {
        ArrayList<ArrayList<String>> meta= new ArrayList<ArrayList<String>>();
		StringTokenizer st ;
        BufferedReader TSVFile = new BufferedReader(new FileReader(filepath));
        
        String dataRow = TSVFile.readLine(); // Read first line.

        while (dataRow != null){
        	ArrayList<String>dataArray = new ArrayList<String>() ;
            st = new StringTokenizer(dataRow.replaceAll("\t"," ")," ");
            while(st.hasMoreElements()){
                dataArray.add(st.nextElement().toString());
            }
            meta.add(dataArray);
//            for (String item:dataArray) { 
//                System.out.print(item + "  "); 
//            }
//            System.out.println(); // Print the data line.
            dataRow = TSVFile.readLine(); // Read next line of data.
        }
        // Close the file once all data has been read.
        TSVFile.close();
        return meta;
	}
	private ArrayList readInput(String filepath) throws IOException {
        ArrayList<ArrayList<String>> meta= new ArrayList<ArrayList<String>>();
		StringTokenizer st ;
        BufferedReader TSVFile = new BufferedReader(new FileReader(filepath));
       
        String dataRow = TSVFile.readLine(); // Read first line.

        while (dataRow != null){
        	ArrayList<String>dataArray = new ArrayList<String>() ;
            st = new StringTokenizer(dataRow," ");
            while(st.hasMoreElements()){
                dataArray.add(st.nextElement().toString());
            }
            meta.add(dataArray);
//            for (String item:dataArray) { 
//                System.out.print(item + "  "); 
//            }
//            System.out.println(); // Print the data line.
            dataRow = TSVFile.readLine(); // Read next line of data.
        }
        // Close the file once all data has been read.
        TSVFile.close();
        return meta;
	}
	private ArrayList translator(ArrayList<String> dataRow){
		ArrayList<Double> numRow = new ArrayList<Double>();
		for(int i=0;i<dataRow.size();i++){
			boolean discrete=false;
			Set<String> workaround =lex.get(i).keySet();
			for(String key : workaround){
				if(key.equals(dataRow.get(i))){
					numRow.add(lex.get(i).get(key));
					discrete=true;
				}
			}
			if(!discrete)
				numRow.add(Double.parseDouble(dataRow.get(i)));
		}
		return numRow;
	}
	@Override
	public void train(String trainingDataFilepath) {
		// TODO Auto-generated method stub
		try {
			ArrayList<ArrayList<String>> data =readInput(trainingDataFilepath);
			for(ArrayList<String> row : data){
				System.out.println(translator(row));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void makePredictions(String testDataFilepath) {
		// TODO Auto-generated method stub

	}

}
