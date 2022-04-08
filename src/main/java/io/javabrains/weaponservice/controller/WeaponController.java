package io.javabrains.weaponservice.controller;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import io.swagger.annotations.ApiImplicitParam;
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

  // @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
  @RequestMapping(method = RequestMethod.POST)
  public Weapon addweapon (@RequestBody Weapon weapon, HttpServletRequest request){
    weaponService.isTokenValidCreator(request);
    logger.info("Saving weapon: {}", weapon);
    return weaponService.create(weapon);
  }

  // @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
  @RequestMapping(method = RequestMethod.GET)
  public WeaponResponse weapons(HttpServletRequest request) {
    weaponService.isTokenValidBossOrCreator(request);
    return weaponService.getAllWeapons();
  }

  // @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
  @GetMapping("/{Id}")
  public ResponseEntity<?> getWeaponById(@PathVariable("Id") Long Id, HttpServletRequest request) {
    weaponService.isTokenValidBossOrCreator(request);
    return weaponService.getById(Id);
  }
  
  // @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
  @PatchMapping("/{Id}")
  public ResponseEntity<?> updateWeapon(@PathVariable("Id") Long Id, @RequestBody Weapon weapon, HttpServletRequest request) {
    weaponService.isTokenValidBossOrCreator(request);
    return weaponService.updateById(Id, weapon);
  }

  // @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
  @RequestMapping(value="/{Id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteWeapon (@PathVariable("Id") Long Id, HttpServletRequest request) {
    weaponService.isTokenValidCreator(request);
    logger.info("Deleting the weapon with and id {}", Id);
    return weaponService.delete(Id);
  }
  
  // @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
  @PatchMapping("/{Id}/addBand")
  public ResponseEntity<Object> updateWeaponsBand(@PathVariable("Id") Long Id, @RequestBody String bandName, HttpServletRequest request) {
    weaponService.isTokenValidBoss(request);
    return weaponService.addBand(Id, bandName, request);
  }

  // @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
  @PatchMapping("/{Id}/addTask")
  public ResponseEntity<Object> updateWeaponsTask(@PathVariable("Id") Long Id, @RequestBody String taskName, HttpServletRequest request) {
    weaponService.isTokenValidBoss(request);
    return weaponService.addTask(Id, taskName, request);
  }

}
