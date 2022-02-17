package io.javabrains.weaponservice.servise;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
      return weaponRepository.save(new Weapon(UUID.randomUUID(), weapon.getName(), weapon.getDamage(), UUID.randomUUID(), UUID.randomUUID()));
    }
    
    public Weapon update(Weapon weapon)
    {
      return weaponRepository.save(weapon);
    }

    public void delete(UUID Id)
    {
      weaponRepository.deleteById(Id);
    }
}
