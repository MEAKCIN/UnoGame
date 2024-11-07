package cards;

public class WildCard extends Cards{
    public String skill;

    public WildCard(String skill) {
        try {
            if (!skill.equals("wild") && !skill.equals("draw4")) {
                throw new IllegalArgumentException("Skill must be wild or draw4");
            }
            else {
                this.skill = skill;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        super.score = 50;
    }
}
