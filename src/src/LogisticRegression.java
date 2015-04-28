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
import Jama.*;



public class LogisticRegression extends Classifier {
	public ArrayList<HashMap<String,Double>> lex;
	public int m;
	public Matrix theta;
	public Matrix records;
	public double alpha=.0001;
	public LogisticRegression(String namesFilepath) {
		super(namesFilepath);
		lex = new ArrayList<HashMap<String,Double>>();
		try {
			createLexicon(readNames(namesFilepath));
			m= lex.size()-1;
			theta = new Matrix(m,1,.5);
//			System.out.println(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}
	private double sigmoid(Matrix x){
//		x.print(x.getRowDimension(), 2);
		double val= 1/(1+Math.pow(Math.E,(double)-1*((theta.transpose().times(x)).get(0, 0))));
		if (Double.isNaN(val)){
//			System.out.print("NaNXXXXXXXXXXXXXXXXXXXXXXX");
			theta.transpose().print(1,2);
			x.print(1, 2);
//			System.out.println((theta.transpose().times(x)).getRowDimension()+" "+(theta.transpose().times(x)).getColumnDimension());
		}
		return val;
	}
	private double cost(double h0, double y){
//		System.out.println("COST FUNC"+h0+" "+y);
//		if(Double.isNaN(h0) || Double.isNaN(y) || h0<=0||h0>1)
//			System.out.println("COST ERROR:"+Double.isNaN(h0)+" "+Double.isNaN(y));
		if(h0==0)
			h0=.00001;
		if(h0==1)
			h0=.99999;
		double val= -y*Math.log(h0)-(1-y)*Math.log(1-h0);
//		System.out.println("COST FUNC"+h0+" "+y+" "+val);
//		if(val<0)
//			System.out.println("Negative");
		if(Double.isInfinite(val) || Double.isNaN(val)){
//			System.out.println("FUCKED UP");
			return .99;
		}

		else
			return val;
	}
	private void createLexicon(ArrayList<ArrayList<String>> names){
//		System.out.println(names);
		lex.add(new HashMap<String,Double>(){{put("1",1.0);}});
		for(ArrayList<String> row : names){
			double ind =0;
			HashMap<String, Double> temp = new HashMap<String,Double>();
			for(String ele : row){
				temp.put(ele, ind);
				ind++;
					
			}
			lex.add(temp);
		}
		HashMap<String, Double> temp = lex.get(1);
		lex.remove(1);
		lex.add(temp);
		
//		System.out.println(lex);
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
	private ArrayList readInput(String filepath, int limit) throws IOException {
        ArrayList<ArrayList<String>> meta= new ArrayList<ArrayList<String>>();
		StringTokenizer st ;
        BufferedReader TSVFile = new BufferedReader(new FileReader(filepath));
       
        String dataRow = TSVFile.readLine(); // Read first line.
        int rowcount=0;
        while (dataRow != null && rowcount<limit){
        	ArrayList<String>dataArray = new ArrayList<String>() ;
            st = new StringTokenizer(dataRow," ");
            dataArray.add("1");
            while(st.hasMoreElements()){
                dataArray.add(st.nextElement().toString());
            }
            meta.add(dataArray);
//            for (String item:dataArray) { 
//                System.out.print(item + "  "); 
//            }
//            System.out.println(); // Print the data line.
            dataRow = TSVFile.readLine(); // Read next line of data.
            rowcount++;
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
			ArrayList<ArrayList<Double>> numData = new ArrayList<ArrayList<Double>>();
			ArrayList<ArrayList<String>> data =readInput(trainingDataFilepath,500);
			for(ArrayList<String> row : data){
				numData.add(translator(row));
			}
			
			double[][] array = new double[numData.size()][];
			for (int i = 0; i < numData.size(); i++) {
			    ArrayList<Double> row = numData.get(i);
			    double[] temp = new double[row.size()];
			    for (int j=0;j<row.size();j++)
			    	temp[j]=(double)row.get(j);
			    array[i] = temp;
			}
			records = new Matrix(array);
//			System.out.println(records.getRowDimension()+" "+records.getColumnDimension());
			float[] adj = new float[theta.getRowDimension()];
			double thresh=1;
			int itercount=0;
			double lastmarg=alpha;
			while(thresh>-1 && itercount<10){
//			System.out.println("Iteration: "+itercount);
			for(int j=0;j<theta.getRowDimension();j++){
				float itercost=0;
				for(int r=0;r<records.getRowDimension();r++){
				Matrix singlerow = records.getMatrix(new int[]{r},new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13});
				//			System.out.println(sigmoid(singlerow.transpose()));
//				System.out.println(sigmoid(singlerow.transpose()));
				itercost+=(sigmoid(singlerow.transpose())-records.get(r, records.getColumnDimension()-1))*records.get(r,j);

				}
//				System.out.println(itercost);
				adj[j]=(float) ((float) ((float) (alpha*((float) 1)/records.getRowDimension())*itercost)+100/records.getRowDimension()*theta.norm1());
				if(Float.isInfinite(adj[j])||Float.isNaN(adj[j]))
					System.out.println(Float.isInfinite(adj[j])+" "+Float.isNaN(adj[j])+"a:"+alpha+" itercost:"+itercost);
			}
			
			for(int q=0;q<theta.getRowDimension(); q++)
				theta.set(q, 0, (theta.get(q, 0)-adj[q]));
			
			double tempsum=0;
			for (double val : adj){
				tempsum+=val;
			}
			thresh=tempsum/adj.length;
//			theta.print(14, 2);
			if(lastmarg>thresh){
				alpha+=.1;	
				}
				else
					alpha*=.5;
				lastmarg=thresh;
			itercount++;
		}
//			theta.print(1, 4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void makePredictions(String testDataFilepath) {
		// TODO Auto-generated method stub
		try {
			ArrayList<ArrayList<Double>> numData = new ArrayList<ArrayList<Double>>();
			ArrayList<ArrayList<String>> data =readInput(testDataFilepath,Integer.MAX_VALUE);
			for(ArrayList<String> row : data){
				numData.add(translator(row));
			}
			
			double[][] array = new double[numData.size()][];
			for (int i = 0; i < numData.size(); i++) {
			    ArrayList<Double> row = numData.get(i);
			    double[] temp = new double[row.size()];
			    for (int j=0;j<row.size();j++)
			    	temp[j]=(double)row.get(j);
			    array[i] = temp;
			}
			Matrix testdata = new Matrix(array);
			int correct=0;
			for(int r=0;r<testdata.getRowDimension();r++){
				Matrix singlerow = testdata.getMatrix(new int[]{r},new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13});
				double pred=sigmoid(singlerow.transpose());
				double guess=0.0;
				if(pred<0.5)
					guess=0.0;
				else
					guess=1.0;
				boolean result = (guess==testdata.get(r,14));
				if(result)
					correct++;
//				System.out.println(pred+" "+guess+" "+(result));
			}
			System.out.println("Correct "+correct+" Out of:"+testdata.getRowDimension());
			System.out.println((double)correct/testdata.getRowDimension());
//			theta.print(14, 2);
//				System.out.println(sigmoid(singlerow.transpose()));
			
	}
	catch (IOException e){
		e.printStackTrace();
	}
	}
}
