package playerclass;

import ability.Ability;
import ability.AbilityNoclip;
import entity.Player;
import render.Camera;
import util.Window;
import world.World;

public abstract class ClassTemplate {
	
	protected String nameUnlocalized;
	protected String nameLocalized;
	protected Player player;
	
	protected Ability ability1;
	protected Ability ability2;
	protected Ability ability3;
	protected Ability abilityUlt;
	
	public ClassTemplate(Player p) {
		player = p;
		nameUnlocalized = "class0";
	}

	public abstract void update(float delta, Window win, Camera camera, World world);
	
	public void useAbility(float delta, Window win, Camera camera, World world, Ability ability) {
		if(ability.isCooled()) {
			ability.use(delta, win, camera, world);
			ability.setLastUse();
//			Game.ConsoleSend("Used \"" + ability.getName() + "\"");
		}
	}
	public String getName() {
		String n;
		if(nameLocalized == null) {
			n = nameUnlocalized;
		} else {
			n = nameLocalized;
		}
		return n;
	}
	public String getUnlocalizedName() {
		return nameUnlocalized;
	}
	public Ability getAbility1() { return ability1; }
	public Ability getAbility2() { return ability2; }
	public Ability getAbility3() { return ability3; }
	public Ability getAbilityUlt() { return abilityUlt; }
}
