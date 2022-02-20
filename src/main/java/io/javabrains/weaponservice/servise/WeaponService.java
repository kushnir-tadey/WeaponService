package io.javabrains.weaponservice.servise;

import java.util.List;

import io.javabrains.weaponservice.model.Weapon;

public interface WeaponService {
    List<Weapon> getAll();
    Weapon create(Weapon weapon);
    Weapon update(Weapon weapon);
    void delete(Long Id);
}