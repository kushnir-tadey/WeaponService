package io.javabrains.weaponservice.servise;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

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

      if(weapon.getName() == null || weapon.getName().replaceAll("\\s+","").equals("")) {
        logger.error("You haven't specified a name for the weapon or name is invalid");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You haven't specified a name for the weapon or name is invalid");
      }

      if(weapon.getName() != null && weaponRepository.findByName(weapon.getName()) != null) {
        logger.error("Weapon with such name already exists");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Weapon with such name already exists");
      }

      if(weapon.getDamage() < 0) {
        logger.error("Damage has to be >= 0");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Damage has to be >= 0");
      }

      if(weapon.getBandId() != null && weapon.getBandId() < 1) {
        logger.error("You specified an invalid bandId");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You specified an invalid bandId");
      }

      if(weapon.getTaskId() != null && weapon.getTaskId() < 0) {
        logger.error("You specified an invalid taskId");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You specified an invalid taskId");
      }

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
          if(weapon.getName().replaceAll("\\s+","").equals("")) {
            logger.error("You haven't specified a name for the weapon");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You haven't specified a name for the weapon");
          }
          if(weaponRepository.findByName(weapon.getName()) != null) {
            logger.error("Weapon with such name already exists");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Weapon with such name already exists");
          }
          _weapon.setName(weapon.getName());
        }
        if(weapon.getDamage() != 0) {
          logger.info("Changing the damage for the weapon with id {}", Id);
          if(weapon.getDamage() < 0) {
            logger.error("Damage has to be >= 0");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Damage has to be >= 0");
          }
          _weapon.setDamage(weapon.getDamage());
        }
        if(weapon.getTaskId() != null) {
          logger.info("Changing the task_id for the weapon with id {}", Id);
          if(weapon.getTaskId() < 0) {
            logger.error("You specified an invalid taskId");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You specified an invalid taskId");
          }
          _weapon.setTaskId(weapon.getTaskId());
        }
        if(weapon.getBandId() != null) {
          logger.info("Changing the band_id for the weapon with id {}", Id);
          if(weapon.getBandId() < 1) {
            logger.error("You specified an invalid bandId");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You specified an invalid bandId");
          }
          _weapon.setBandId(weapon.getBandId());
        }
        logger.info("Updated weapon: {}", _weapon);
        return new ResponseEntity<>(weaponRepository.save(_weapon), HttpStatus.OK);
      } 
      else {
        logger.error("There is no such weapon with an id {}", Id);
        return new ResponseEntity<String>("There is no weapon with such id", HttpStatus.NOT_FOUND);
      }
    }

    public ResponseEntity<Object> addBand(Long Id, String bandName, HttpServletRequest request) {
      try {
        Optional<Weapon> weaponData = weaponRepository.findById(Id);
        if (weaponData.isPresent()) {
          RestTemplate restTemplate = new RestTemplate();
          String fullUrl = Links.getBandUrl() + "?bandName=" + bandName;
          ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(createHeaders(request.getHeader("Authorization"))), String.class);
          try {
              String bandJSON = new String((response.getBody()).getBytes());
              if (bandJSON.equals("[]")) {
                logger.error("Specified bandName is incorrect");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specified bandName is incorrect");
              }
              JSONObject jsonObject = new JSONObject(bandJSON);
              Long bandId = jsonObject.getLong("id");
              logger.info("Adding band with id {} to the weapon...", bandId);
              return new ResponseEntity<>(updateBandId(Id, bandId), HttpStatus.OK);
          } catch (JSONException e) {
              logger.error("Error has occured: {}", e);
              throw new RuntimeException(e);
          }
        }
        else {
          logger.error("There is no such weapon with an id {}", Id);
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no weapon with such id");
        }
      }
      catch (HttpClientErrorException e){
        logger.error("Client error has occured: {}", e);
        throw new ResponseStatusException(e.getStatusCode());
      }
    }

    public ResponseEntity<Object> addTask(Long Id, String taskName, HttpServletRequest request) {
      try {
        Optional<Weapon> weaponData = weaponRepository.findById(Id);
        if (weaponData.isPresent()) {
          RestTemplate restTemplate = new RestTemplate();
          String fullUrl = Links.getTaskUrl() + "?taskName=" + taskName;
          ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.GET, new HttpEntity<>(createHeaders(request.getHeader("Authorization"))), String.class);
          try {
              String taskJSON = new String((response.getBody()).getBytes());
              if (taskJSON.equals("[]")) {
                logger.error("Specified taskName is incorrect");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specified taskName is incorrect");
              }
              JSONObject jsonObject = new JSONObject(taskJSON);
              Long taskId = jsonObject.getLong("id");
              logger.info("Adding task with id {} to the weapon...", taskId);
              return new ResponseEntity<>(updateTaskId(Id, taskId), HttpStatus.OK);
          } catch (JSONException e) {
              logger.error("Error has occured: {}", e);
              throw new RuntimeException(e);
          }
        }
        else {
          logger.error("There is no such weapon with an id {}", Id);
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no weapon with such id");
        }
      }
      catch (HttpClientErrorException e){
        logger.error("Client error has occured: {}", e);
        throw new ResponseStatusException(e.getStatusCode());
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
          updatedWeapon.setBandId(bandId);
      }
      else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
      return weaponRepository.save(updatedWeapon);
    }

    public Object updateTaskId(Long Id, Long taskId) {
      Optional<Weapon> _weapon = weaponRepository.findById(Id);
      Weapon updatedWeapon = _weapon.get();
      if (_weapon.isPresent()) {
          logger.info("Weapon's task_id has been changed");
          updatedWeapon.setTaskId(taskId);
      }
      else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth!=null && headerAuth.startsWith("Bearer ")) {
          String[] str;
          try {
              str = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(headerAuth.substring(7)).getBody().getSubject().split(" ");
          } catch (Exception e) {
              throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization information is missing or invalid");
          }
          if (str[2].contains("ROLE_BOSS")) {
              logger.info("Authentification token is valid");
              return false;
          } else {
              logger.error("Sorry, this method is forbidden with provided JWT token");
              throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sorry, you don't have right authorities");
          }
        } else {
            logger.error("Unauthorized");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization information is missing or invalid");
        }
    }

    public boolean isTokenValidCreator(HttpServletRequest request){
      String headerAuth = request.getHeader("Authorization");
        if (headerAuth!=null && headerAuth.startsWith("Bearer ")) {
          String[] str;
          try {
              str = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(headerAuth.substring(7)).getBody().getSubject().split(" ");
          } catch (Exception e) {
              throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization information is missing or invalid");
          }
          if (str[2].contains("ROLE_CREATOR")) {
              logger.info("Authentification token is valid");
              return false;
          } else {
              logger.error("Sorry, this method is forbidden with provided JWT token");
              throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sorry, you don't have right authorities");
          }
        } else {
            logger.error("Unauthorized");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization information is missing or invalid");
        }
    }

    public boolean isTokenValidBossOrCreator(HttpServletRequest request){
      String headerAuth = request.getHeader("Authorization");
        if (headerAuth!=null && headerAuth.startsWith("Bearer ")) {
          String[] str;
          try {
              str = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(headerAuth.substring(7)).getBody().getSubject().split(" ");
          } catch (Exception e) {
              throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization information is missing or invalid");
          }
          if (str[2].contains("ROLE_BOSS") || str[2].contains("ROLE_CREATOR")) {
              logger.info("Authentification token is valid");
              return false;
          } else {
              logger.error("Sorry, this method is forbidden with provided JWT token");
              throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sorry, you don't have right authorities");
          }
        } else {
            logger.error("Unauthorized");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization information is missing or invalid");
        }
    }

    public HttpHeaders createHeaders(String jwt) {
      return new HttpHeaders() {{
          set("Authorization", jwt);
      }};
  }
}
