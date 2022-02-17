package io.javabrains.weaponservice.weapon;

import java.util.List;

import io.javabrains.weaponservice.model.Weapon;

public class WeaponResponse {

    private List<Weapon> weapons;

    public List<Weapon> getWeapons() {
      return weapons;
    }
  
    public void setWeapons(List<Weapon> weaponList) {
      this.weapons = weaponList;
    }
}
