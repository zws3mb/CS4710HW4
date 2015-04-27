package src;

public class driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LogisticRegression log1 = new LogisticRegression("C:\\Users\\Zachary\\Desktop\\hw4_MachineLearning\\CS4710_HW4\\src\\trainingData\\census.names");
		log1.train("C:\\Users\\Zachary\\Desktop\\hw4_MachineLearning\\CS4710_HW4\\src\\trainingData\\census.train");
	}

}
