package io.javabrains.weaponservice.controller;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.server.ResponseStatusException;

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

  Logger logger = LoggerFactory.getLogger(WeaponController.class);

  @RequestMapping(method = RequestMethod.POST)
  public Weapon addweapon (@RequestBody Weapon weapon, HttpServletRequest request){
    // if (!weaponService.isTokenValidCreator(request)) {
    //   logger.error("You provided a bad authentication token");
    //   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You provided a bad authentication token");
    // }
    weaponService.isTokenValidCreator(request);
    logger.info("Saving weapon: {}", weapon);
    return weaponService.create(weapon);
  }

  @RequestMapping(method = RequestMethod.GET)
  public WeaponResponse weapons(HttpServletRequest request) {
    // if (!weaponService.isTokenValidBossOrCreator(request)) {
    //   logger.error("You provided a bad authentication token");
    //   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You provided a bad authentication token");
    // }
    weaponService.isTokenValidBossOrCreator(request);
    return weaponService.getAllWeapons();
  }

  @GetMapping("/{Id}")
  public ResponseEntity<?> getWeaponById(@PathVariable("Id") Long Id, HttpServletRequest request) {
    // if (!weaponService.isTokenValidBossOrCreator(request)) {
    //   logger.error("You provided a bad authentication token");
    //   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You provided a bad authentication token");
    // }
    weaponService.isTokenValidBossOrCreator(request);
    return weaponService.getById(Id);
  }
  
  @PatchMapping("/{Id}")
  public ResponseEntity<?> updateWeapon(@PathVariable("Id") Long Id, @RequestBody Weapon weapon, HttpServletRequest request) {
    // if (!weaponService.isTokenValidBossOrCreator(request)) {
    //   logger.error("You provided a bad authentication token");
    //   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You provided a bad authentication token");
    // }
    weaponService.isTokenValidBossOrCreator(request);
    return weaponService.updateById(Id, weapon);
  }

  @RequestMapping(value="/{Id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteWeapon (@PathVariable("Id") Long Id, HttpServletRequest request) {
    // if (!weaponService.isTokenValidCreator(request)) {
    //   logger.error("You provided a bad authentication token");
    //   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You provided a bad authentication token");
    // }
    weaponService.isTokenValidCreator(request);
    logger.info("Deleting the weapon with and id {}", Id);
    return weaponService.delete(Id);
  }
  
  @PatchMapping("/{Id}/addBand")
  public ResponseEntity<Object> updateWeaponsBand(@PathVariable("Id") Long Id, @RequestBody String bandName, HttpServletRequest request) {
    // if (!weaponService.isTokenValidBoss(request)) {
    //   logger.error("You provided a bad authentication token");
    //   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You provided a bad authentication token");
    // }
    weaponService.isTokenValidBoss(request);
    return weaponService.addBand(Id, bandName, request);
  }

  @PatchMapping("/{Id}/addTask")
  public ResponseEntity<Object> updateWeaponsTask(@PathVariable("Id") Long Id, @RequestBody String taskName, HttpServletRequest request) {
    // if (!weaponService.isTokenValidBoss(request)) {
    //   logger.error("You provided a bad authentication token");
    //   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You provided a bad authentication token");
    // }
    weaponService.isTokenValidBoss(request);
    return weaponService.addTask(Id, taskName, request);
  }

}