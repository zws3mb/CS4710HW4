package src;

public class driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LogReg2 log1 = new LogReg2("C:\\Users\\Zachary\\Desktop\\Spring 2015\\CS4710_HW4\\src\\trainingData\\census.names");
		log1.train("C:\\Users\\Zachary\\Desktop\\Spring 2015\\CS4710_HW4\\src\\trainingData\\census.train");
		log1.makePredictions("C:\\Users\\Zachary\\Desktop\\Spring 2015\\CS4710_HW4\\src\\trainingData\\census.train");
		Simpleton simp = new Simpleton("C:\\Users\\Zachary\\Desktop\\Spring 2015\\CS4710_HW4\\src\\trainingData\\census.names");
		simp.makePredictions("C:\\Users\\Zachary\\Desktop\\Spring 2015\\CS4710_HW4\\src\\trainingData\\census.train");
//		LogisticRegression log2 = new LogisticRegression("C:\\Users\\Zachary\\Desktop\\Spring 2015\\CS4710_HW4\\src\\trainingData\\census.names");
//		log2.train("C:\\Users\\Zachary\\Desktop\\Spring 2015\\CS4710_HW4\\src\\trainingData\\census.train");
//		log2.makePredictions("C:\\Users\\Zachary\\Desktop\\Spring 2015\\CS4710_HW4\\src\\trainingData\\census.test");
	}

}
