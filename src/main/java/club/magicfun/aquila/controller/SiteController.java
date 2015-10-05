package club.magicfun.aquila.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import club.magicfun.aquila.common.GlobalManager;
import club.magicfun.aquila.model.Image;
import club.magicfun.aquila.service.ImageService;

@Controller
public class SiteController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(SiteController.class);

	private final int DEFAULT_BUFFER_SIZE = 10240;

	@Autowired
	private ImageService imageService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String indexHandler() {

		logger.debug("SiteController.indexHandler is invoked.");

		return "site/index";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/image/{id}")
	public void getImageHandler(Model model, @PathVariable("id") Integer imageId, HttpServletResponse response)
			throws IOException {

		//logger.debug("SiteController.getImageHandler is invoked.");

		if (imageId == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404
			return;
		}

		Image image = imageService.findImageById(imageId);

		if (image == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404
			return;
		}

		response.reset();
		response.setBufferSize(DEFAULT_BUFFER_SIZE);
		response.setContentType(image.getContentType());
		response.setHeader("Content-Length", String.valueOf(image.getLength()));
		response.setHeader("Content-Disposition", "inline; filename=\"" + image.getName() + "\"");

		BufferedInputStream input = null;
		BufferedOutputStream output = null;

		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(image.getContent());
			input = new BufferedInputStream(bis, DEFAULT_BUFFER_SIZE);
			output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
		} finally {
			GlobalManager.close(output);
			GlobalManager.close(input);
		}
	}

}
