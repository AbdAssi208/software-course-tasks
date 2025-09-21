
public class Assignment01Q03 {

	public static void main(String[] args) {
		int numOfOdd = 0;
		int n = Integer.parseInt(args[0]);
		int prev = 1;
		int curr = 1;
		int temp = curr;
		int cnt = n; 
		System.out.println("The first "+ n +" Fibonacci numbers are:");
		while (cnt != 0){
			System.out.print(curr + "" + prev + "");
			curr = prev + curr;
			prev = temp;
			cnt -= 1;
			if ((curr % 2 == 0) || (prev % 2 == 0)){
				numOfOdd += 1;
			}
		}
		System.out.println("The number of odd numbers is: "+numOfOdd);

	}

}
