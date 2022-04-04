package io.javabrains.weaponservice.servise;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import io.javabrains.weaponservice.model.Weapon;
import io.javabrains.weaponservice.weapon.WeaponResponse;

public interface WeaponService {
    List<Weapon> getAll();
    WeaponResponse getAllWeapons();
    Weapon create(Weapon weapon);
    ResponseEntity<?> getById(Long Id);
    ResponseEntity<Object> addBand(Long Id, String bandName, HttpServletRequest request);
    ResponseEntity<Object> addTask(Long Id, String taskName, HttpServletRequest request);
    Object updateBandId(Long Id, Long bandId);
    Object updateTaskId(Long Id, Long taskId);
    ResponseEntity<?> updateById(Long Id, Weapon weapon);
    Weapon update(Weapon weapon);
    ResponseEntity<?> delete(Long Id);
    boolean isTokenValidBoss(HttpServletRequest request);
    boolean isTokenValidBossOrCreator(HttpServletRequest request);
    boolean isTokenValidCreator(HttpServletRequest request);
    HttpHeaders createHeaders(String jwt);
}