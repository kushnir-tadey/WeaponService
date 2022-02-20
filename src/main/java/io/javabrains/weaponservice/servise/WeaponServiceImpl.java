package io.javabrains.weaponservice.servise;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.javabrains.weaponservice.model.Weapon;
import io.javabrains.weaponservice.repository.WeaponRepository;

@Service
public class WeaponServiceImpl implements WeaponService{
    @Autowired
    WeaponRepository weaponRepository;

    public List<Weapon> getAll() {
        List<Weapon> weaponList = new ArrayList<Weapon>();
        weaponRepository.findAll().forEach(weapon -> weaponList.add(weapon));
        return weaponList;
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
    
    public Weapon update(Weapon weapon)
    {
      return weaponRepository.save(weapon);
    }

    public void delete(Long Id)
    {
      weaponRepository.deleteById(Id);
    }
}
