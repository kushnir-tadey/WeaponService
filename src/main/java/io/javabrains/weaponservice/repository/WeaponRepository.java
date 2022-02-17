package io.javabrains.weaponservice.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import io.javabrains.weaponservice.model.Weapon;

public interface WeaponRepository extends CassandraRepository<Weapon, UUID> {

}
