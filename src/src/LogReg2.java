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

import src.Classifier;
import Jama.*;



public class LogReg2 extends Classifier {
//	public HashMap<String,Double> lex;
	public ArrayList<String> order;
	public int m;
	public Matrix theta;
	public Matrix records;
	public double alpha=1;
	public double lambda=1;
	public LogReg2(String namesFilepath) {
		super(namesFilepath);
//		lex = new HashMap<String,Double>();
		order = new ArrayList<String>();
		try {
			createLexicon(readNames(namesFilepath));
			System.out.println(order);
			m= order.size()-1;
			theta = new Matrix(m,1,Math.random());
			System.out.println(m);
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
			System.out.print("NaNXXXXXXXXXXXXXXXXXXXXXXX");
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
		if(val<0)
			System.out.println("Negative");
		if(Double.isInfinite(val) || Double.isNaN(val)){
			System.out.println("FUCKED UP");
			return .99;
		}

		else
			return val;
	}
	private void createLexicon(ArrayList<ArrayList<String>> names){
//		System.out.println(names);
		order.add("CONSTANT");
//		lex.put("1",1.0);
		for(ArrayList<String> row : names){
			if(row.size() >2){
				for(int i=1; i<row.size();i++){
//					lex.put(row.get(i), 1.0);
					order.add(row.get(i));
				}			
			}
			else
			{
//				lex.put(row.get(0),0.0);
				order.add("CONTINUOUS");
			}
			}
		String temp= order.get(1);
		order.remove(1);
		order.add("<=50K");
//			System.out.println(lex);
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
//		Set<String> workaround =lex.keySet();
		int index =0;
		for(int i=0;i<order.size();i++){
			boolean added=false;
			for(String ach:dataRow){
				if((order.get(i)).equals(ach)){
						numRow.add(1.0);
						index++;
						added=true;
						break;
					}

			}
			if(!added)
			{
				if((order.get(i)).equals("CONTINUOUS")){
					try{
						double val =Double.parseDouble(dataRow.get(index));
						if(val>100)
							val=100;
						numRow.add(val);
						index++;
					}
					catch(NumberFormatException e){
						System.out.println(e.getMessage() + index+" "+dataRow.get(index)+" "+i+" "+order.get(i));
					}
					
				}
				else if(order.get(i).equals("<=50K")){
					numRow.add(0.0);
					index++;
				}
				else if ((order.get(i)).equals("CONSTANT")){
					numRow.add(1.0);
					index++;
				}
				else
					numRow.add(0.0);
			}
			
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
			System.out.println(records.getRowDimension()+" "+records.getColumnDimension());
			float[] adj = new float[theta.getRowDimension()];
			double thresh=1;
			int itercount=0;
			double lastmarg=alpha;
			while(thresh>-1 && itercount<10){
			System.out.println("Iteration: "+itercount);
			int[] targetvals = new int[m];
			for(int y=0;y<m;y++)
				targetvals[y]=y;

			for(int j=0;j<theta.getRowDimension();j++){
				float itercost=0;
				for(int r=0;r<records.getRowDimension();r++){
				Matrix singlerow = records.getMatrix(new int[]{r},targetvals);
				//			System.out.println(sigmoid(singlerow.transpose()));
//				System.out.println(sigmoid(singlerow.transpose()));
				itercost+=(sigmoid(singlerow.transpose())-records.get(r, records.getColumnDimension()-1))*records.get(r,j);

				}
//				System.out.print("Cost: "+alpha*itercost/records.getRowDimension());
				adj[j]=(float) ((float) ((alpha/records.getRowDimension())*itercost)+lambda/records.getRowDimension()*theta.get(j, 0));
				if(Float.isInfinite(adj[j])||Float.isNaN(adj[j]))
					System.out.println(Float.isInfinite(adj[j])+" "+Float.isNaN(adj[j])+"a:"+alpha+" itercost:"+itercost);
			}
//			System.out.print("Theta:");
			for(int q=0;q<theta.getRowDimension(); q++){
				theta.set(q, 0, (theta.get(q, 0)-adj[q]));
//				System.out.print(theta.get(q,0)+" ");
			}
			
			double tempsum=0;
			for (double val : adj){
				tempsum+=val;
			}
			thresh=tempsum/adj.length;
//			theta.print(14, 2);
			itercount++;
//			System.out.println(alpha+" "+thresh);
			if(lastmarg>thresh){
			alpha+=.1;	
			}
			else
				alpha*=.5;
			lastmarg=thresh;
//			alpha=alpha*(1/((itercount+1)/2));
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
			int[] targetvals = new int[m];
			for(int y=0;y<m-1;y++)
				targetvals[y]=y;
			for(int r=0;r<testdata.getRowDimension();r++){
				Matrix singlerow = testdata.getMatrix(new int[]{r},targetvals);
				double pred=sigmoid(singlerow.transpose());
				double guess=0.0;
				if(pred<0.5)
					guess=0.0;
				else
					guess=1.0;
				boolean result = (guess==testdata.get(r,testdata.getColumnDimension()-1));
				if(result)
					correct++;
				System.out.println(pred+" "+guess+" "+(result));
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
