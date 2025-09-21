
public class Assignment01Q01 {

	public static void main(String[] args) {
		for (String sentence : args) {//תעשבה לכל משפט שמקבלים
			String[] words = sentence.split(" ");//פצל אותו למילים
			
			for (String word : words) {//לכל אחת מהמלים
				char  charcter = word.charAt(0);//קח את האוט הראשון
				int asciiValue = (int) charcter;//קח את ההצגה שלו כמפסר ב(ASCII)

				if (asciiValue % 5 == 0){//בדוק אם הוא מתחלר ב 5
					System.out.print(charcter);//אם כן אז תדפיס
				}
			}

		}
		
	}

}
