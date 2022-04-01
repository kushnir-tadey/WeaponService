package io.javabrains.weaponservice.servise;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.javabrains.weaponservice.configurations.Links;
import io.javabrains.weaponservice.model.Weapon;
import io.javabrains.weaponservice.repository.WeaponRepository;
import io.javabrains.weaponservice.weapon.WeaponResponse;
import io.jsonwebtoken.Jwts;

@Service
public class WeaponServiceImpl implements WeaponService{
    @Autowired
    WeaponRepository weaponRepository;

    Logger logger = LoggerFactory.getLogger(WeaponServiceImpl.class);

    @Value("${my.app.secret}")
    private String jwtSecret;

    public List<Weapon> getAll() {
        List<Weapon> weaponList = new ArrayList<Weapon>();
        weaponRepository.findAll().forEach(weapon -> weaponList.add(weapon));
        return weaponList;
    }

    public WeaponResponse getAllWeapons() {
      List<Weapon> weaponList = getAll();
      WeaponResponse weaponResponse = new WeaponResponse();
      weaponResponse.setWeapons(weaponList);
      logger.info("All the weapons have been accessed");
      return weaponResponse;      
    }
    
    public Weapon create(Weapon weapon)
    {
      List<Weapon> listOfWeapons = weaponRepository.findAll();
      Long num;
      try {
          num = listOfWeapons.stream().max((o1, o2) -> {
              return (int) (o1.getId() - o2.getId());
          }).get().getId();
      } catch (NoSuchElementException e) {
          num = 0L;
      }
      weapon.setId(++num);
      return weaponRepository.save(weapon);
    }

    public ResponseEntity<?> getById(Long Id) {
      Optional<Weapon> weaponData = weaponRepository.findById(Id);
      if (weaponData.isPresent()) {
        logger.info("Returned the weapon with an id {}", Id);
        return new ResponseEntity<>(weaponData.get(), HttpStatus.OK);
      } 
      else {
        logger.error("There is no such weapon with an id {}", Id);
        return new ResponseEntity<String>("There is no weapon with such id", HttpStatus.NOT_FOUND);
      }
    }

    public ResponseEntity<?> updateById(Long Id, Weapon weapon) {
      Optional<Weapon> weaponData = weaponRepository.findById(Id);
      if (weaponData.isPresent()) {
        logger.info("Updating weapon with id {}", Id);
        Weapon _weapon = weaponData.get();
        if(weapon.getName() != null) {
          logger.info("Changing the name for the weapon with id {}", Id);
          _weapon.setName(weapon.getName());
        }
        if(weapon.getDamage() != 0) {
          logger.info("Changing the damage for the weapon with id {}", Id);
          _weapon.setDamage(weapon.getDamage());
        }
        if(weapon.getTask_id() != null) {
          logger.info("Changing the task_id for the weapon with id {}", Id);
          _weapon.setTask_id(weapon.getTask_id());
        }
        if(weapon.getBand_id() != null) {
          logger.info("Changing the band_id for the weapon with id {}", Id);
          _weapon.setBand_id(weapon.getBand_id());
        }
        logger.info("Updated weapon: {}", _weapon);
        return new ResponseEntity<>(weaponRepository.save(_weapon), HttpStatus.OK);
      } 
      else {
        logger.error("There is no such weapon with an id {}", Id);
        return new ResponseEntity<String>("There is no weapon with such id", HttpStatus.NOT_FOUND);
      }
    }

    public ResponseEntity<Object> addBand(Long Id, String bandName) {
      Optional<Weapon> weaponData = weaponRepository.findById(Id);
      if (weaponData.isPresent()) {
        RestTemplate restTemplate = new RestTemplate();
        String fullUrl = Links.getBandUrl() + "?bandName=" + bandName;
        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.GET, null, String.class);
        try {
            String bandJSON = new String((response.getBody()).getBytes());
            JSONObject jsonObject = new JSONObject(bandJSON);
            Long bandId = jsonObject.getLong("id");
            logger.info("Adding band with id {} to the weapon...", bandId);
            return new ResponseEntity<>(updateBandId(Id, bandId), HttpStatus.OK);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
      }
      else {
        logger.error("There is no such weapon with an id {}", Id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }

    public ResponseEntity<Object> addTask(Long Id, String taskName) {
      Optional<Weapon> weaponData = weaponRepository.findById(Id);
      if (weaponData.isPresent()) {
        RestTemplate restTemplate = new RestTemplate();
        String fullUrl = Links.getTaskUrl() + "?taskName=" + taskName;
        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.GET, null, String.class);
        try {
            String taskJSON = new String((response.getBody()).getBytes());
            JSONObject jsonObject = new JSONObject(taskJSON);
            Long taskId = jsonObject.getLong("id");
            logger.info("Adding task with id {} to the weapon...", taskId);
            return new ResponseEntity<>(updateTaskId(Id, taskId), HttpStatus.OK);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
      }
      else {
        logger.error("There is no such weapon with an id {}", Id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }
    
    public Weapon update(Weapon weapon)
    {
      return weaponRepository.save(weapon);
    }

    public Object updateBandId(Long Id, Long bandId) {
      Optional<Weapon> _weapon = weaponRepository.findById(Id);
      Weapon updatedWeapon = _weapon.get();
      if (_weapon.isPresent()) {
          logger.info("Weapon's band_id has been changed");
          updatedWeapon.setBand_id(bandId);
      }
      return weaponRepository.save(updatedWeapon);
    }

    public Object updateTaskId(Long Id, Long taskId) {
      Optional<Weapon> _weapon = weaponRepository.findById(Id);
      Weapon updatedWeapon = _weapon.get();
      if (_weapon.isPresent()) {
          logger.info("Weapon's task_id has been changed");
          updatedWeapon.setTask_id(taskId);
      }
      return weaponRepository.save(updatedWeapon);
    }

    public ResponseEntity<?> delete(Long Id) {
      Optional<Weapon> weaponData = weaponRepository.findById(Id);
      if (weaponData.isPresent()) {
        weaponRepository.deleteById(Id);
        return new ResponseEntity<String>("Weapon was deleted", HttpStatus.NO_CONTENT);
      }
      else {
        logger.error("There is no such weapon with an id {}", Id);
        return new ResponseEntity<String>("There is no weapon with such id", HttpStatus.NOT_FOUND);
      }
    }

    public boolean isTokenValidBoss(HttpServletRequest request){
      try {
          String headerAuth = request.getHeader("Authorization");
          if (headerAuth!=null && headerAuth.startsWith("Bearer ")) {
              String[] s = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(headerAuth.substring(7)).getBody().getSubject().split(" ");
              return s[2].contains("ROLE_BOSS");
          } else {
              return false;
          }
      } catch (Exception e){
          return false;
      }
    }

    public boolean isTokenValidCreator(HttpServletRequest request){
      try {
          String headerAuth = request.getHeader("Authorization");
          if (headerAuth!=null && headerAuth.startsWith("Bearer ")) {
              String[] s = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(headerAuth.substring(7)).getBody().getSubject().split(" ");
              return s[2].contains("ROLE_CREATOR");
          } else {
              return false;
          }
      } catch (Exception e){
          return false;
      }
    }

    public boolean isTokenValidBossOrCreator(HttpServletRequest request){
      try {
          String headerAuth = request.getHeader("Authorization");
          if (headerAuth!=null && headerAuth.startsWith("Bearer ")) {
              String[] s = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(headerAuth.substring(7)).getBody().getSubject().split(" ");
              return s[2].contains("ROLE_BOSS") || s[2].contains("ROLE_CREATOR");
          } else {
              return false;
          }
      } catch (Exception e){
          return false;
      }
    }
}
