package io.javabrains.weaponservice.servise;

import java.util.List;

import org.springframework.http.ResponseEntity;

import io.javabrains.weaponservice.model.Weapon;
import io.javabrains.weaponservice.weapon.WeaponResponse;

public interface WeaponService {
    List<Weapon> getAll();
    WeaponResponse getAllWeapons();
    Weapon create(Weapon weapon);
    ResponseEntity<?> getById(Long Id);
    ResponseEntity<Object> addBand(Long Id, String bandName);
    ResponseEntity<Object> addTask(Long Id, String taskName);
    Object updateBandId(Long Id, Long bandId);
    Object updateTaskId(Long Id, Long taskId);
    ResponseEntity<?> updateById(Long Id, Weapon weapon);
    Weapon update(Weapon weapon);
    ResponseEntity<?> delete(Long Id);
}