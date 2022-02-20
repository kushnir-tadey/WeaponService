package io.javabrains.weaponservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.weaponservice.weapon.WeaponResponse;
import io.javabrains.weaponservice.model.Weapon;
import io.javabrains.weaponservice.repository.WeaponRepository;
import io.javabrains.weaponservice.servise.WeaponService;


@RestController
@RequestMapping("/api/v1/weapons")
@CrossOrigin()
public class WeaponController {

  @Autowired
  WeaponService weaponService;

  @Autowired
  WeaponRepository weaponRepository;

  @RequestMapping(method = RequestMethod.GET)
  public WeaponResponse weapons (){
    List<Weapon> weaponList = weaponService.getAll();
    WeaponResponse weaponResponse = new WeaponResponse();
    weaponResponse.setWeapons(weaponList);
    return weaponResponse;
  }

  @GetMapping("/{Id}")
  public ResponseEntity<Weapon> getWeaponById(@PathVariable("Id") Long Id) {
    Optional<Weapon> weaponData = weaponRepository.findById(Id);
    if (weaponData.isPresent()) {
      return new ResponseEntity<>(weaponData.get(), HttpStatus.OK);
    } 
    else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(method = RequestMethod.POST)
  public Weapon addweapon (@RequestBody Weapon weapon){
    return weaponService.create(weapon);
  }

  @PatchMapping("/{Id}")
  public ResponseEntity<Weapon> updateWeapon(@PathVariable("Id") Long Id, @RequestBody Weapon weapon){
    Optional<Weapon> weaponData = weaponRepository.findById(Id);
    if (weaponData.isPresent()) {
      Weapon _weapon = weaponData.get();
      if(weapon.getName() != null) {
        _weapon.setName(weapon.getName());
      }
      if(weapon.getDamage() != 0) {
        _weapon.setDamage(weapon.getDamage());
      }
      if(weapon.getTask_id() != null) {
        _weapon.setTask_id(weapon.getTask_id());
      }
      if(weapon.getBand_id() != null) {
        _weapon.setBand_id(weapon.getBand_id());
      }
      return new ResponseEntity<>(weaponRepository.save(_weapon), HttpStatus.OK);
    } 
    else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value="/{Id}", method = RequestMethod.DELETE)
  public void deleteWeapon (@PathVariable("Id") Long Id){
    weaponService.delete(Id);
  }

}