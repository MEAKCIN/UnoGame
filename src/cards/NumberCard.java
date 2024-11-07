package cards;

public class NumberCard extends Cards {
    public int number;
    public String color;

    public NumberCard(int number, String color) {
        try {
            if (number < 0 || number > 9) {
                throw new IllegalArgumentException("Number must be between 0 and 9");
            }
            else {
                this.number = number;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        try {
            if (!color.equals("red") && !color.equals("blue") && !color.equals("green") && !color.equals("yellow")) {
                throw new IllegalArgumentException("Color must be red, blue, green, or yellow");
            }
            else {
                this.color = color;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        super.score = number;
    }



}
