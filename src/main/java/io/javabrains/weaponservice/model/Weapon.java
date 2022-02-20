package io.javabrains.weaponservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;


@Table(value = "weapon")
public class Weapon {

    @Id 
    @PrimaryKeyColumn(name = "Id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Long Id;

    @Column("name")
    @CassandraType(type = Name.TEXT)
    private String name;
    
    @Column("damage")
    @CassandraType(type = Name.INT)
    private int damage;

    @Column("task_id")
    private Long task_id;

    @Column("band_id")
    private Long band_id;

    public Weapon() {
    }

    public Weapon(Long Id, String name, int damage, Long task_id, Long band_id) {
        this.Id = Id;
        this.name = name;
        this.damage = damage;
        this.task_id = task_id;
        this.band_id = band_id;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public Long getBand_id() {
        return band_id;
    }

    public void setBand_id(Long band_id) {
        this.band_id = band_id;
    }

    @Override
    public String toString() {
        return "Weapon [Id=" + Id + ", name=" + name + ", damage=" + damage + ", task_id=" + task_id + ", band_id=" + band_id + "]";
    }

}
