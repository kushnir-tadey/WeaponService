package io.javabrains.weaponservice.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

import io.javabrains.weaponservice.model.Weapon;

public interface WeaponRepository extends CassandraRepository<Weapon, Long> {
    @Query("select * from weapon where name=:name allow filtering")
    public Weapon findByName(@Param("name") String name);
}
