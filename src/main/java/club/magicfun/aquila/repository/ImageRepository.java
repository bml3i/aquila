package club.magicfun.aquila.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.magicfun.aquila.model.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {

}
