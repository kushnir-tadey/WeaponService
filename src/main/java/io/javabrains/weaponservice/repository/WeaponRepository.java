package io.javabrains.weaponservice.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
// import org.springframework.stereotype.Repository;

import io.javabrains.weaponservice.model.Weapon;

// @Repository
public interface WeaponRepository extends CassandraRepository<Weapon, Long> {

}
