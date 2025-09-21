

public class Assignment01Q02 {

	public static void main(String[] args) {
		// do not change this part below
		double piEstimation = 0.0;
		for (String number : args ){
			int  i  = -1;
			int x = 1;
			double y = i * (1/x);
			int far = Integer.parseInt(number);
				while (far != 0){
					piEstimation += y;
					far -= 1;
					x += 2;
					i= i * (-1);
					y = i* (1/x);
				}
 			piEstimation *= 4;
		
		// do not change this part below
		System.out.println(piEstimation + " " + Math.PI);

		}

	}
		

}
