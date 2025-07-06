package com.example.greenpath.Repositories;

import com.example.greenpath.Models.Lieu;
import net.snowflake.client.jdbc.internal.google.j2objc.annotations.J2ObjCIncompatible;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LieuRepository extends JpaRepository<Lieu, Long> {
    List<Lieu> findByRegion(String region);

}
