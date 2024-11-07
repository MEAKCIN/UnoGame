package cards;

public class ActionCards extends Cards {
    public String skill;
    public String color;

    public ActionCards(String skill, String color) {
        try {
            if (!skill.equals("skip") && !skill.equals("reverse") && !skill.equals("draw2")) {
                throw new IllegalArgumentException("Skill must be skip, reverse, draw2");
            }
            else {
                this.skill = skill;
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
        super.score = 20;
    }

}
