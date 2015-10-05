package club.magicfun.aquila.service;

import club.magicfun.aquila.model.Image;

public interface ImageService {

	Image findImageById(Integer id);

	Image persist(Image image);

}