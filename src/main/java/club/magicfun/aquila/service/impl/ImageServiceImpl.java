package club.magicfun.aquila.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.magicfun.aquila.model.Image;
import club.magicfun.aquila.repository.ImageRepository;
import club.magicfun.aquila.service.ImageService;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

	@Autowired
	private ImageRepository imageRepository;

	@Override
	public Image findImageById(Integer id) {
		return imageRepository.findOne(id);
	}

	@Override
	public Image persist(Image image) {
		return imageRepository.save(image);
	}

}
