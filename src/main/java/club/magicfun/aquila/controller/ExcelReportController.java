package club.magicfun.aquila.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import club.magicfun.aquila.model.User;
import club.magicfun.aquila.service.AuthorityService;
import com.rlib.excel.Excel;

@Controller
public class ExcelReportController extends ExcelMultiActionController {

	private static final Logger logger = LoggerFactory.getLogger(ExcelReportController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	private AuthorityService authorityService;

	private final static String USER_REPORT_PROPERTY_CONFIG = "excelreports.info.user_report";

	@RequestMapping(value = "excelreports/user_report", method = RequestMethod.GET)
	public void exportUserReportHandler(HttpServletRequest request, HttpServletResponse response, Locale locale)
			throws Exception {

		logger.debug("ExcelReportController.exportUserReportHandler is invoked.");
		
		List<User> users = authorityService.findAllUsers();

		Map<String, Object> excelData = new HashMap<String, Object>();
		excelData.put("UserIdHeader",
				messageSource.getMessage("excelreports.info.user_report.user_id_header", new String[] {}, locale));
		excelData.put("UserNameHeader",
				messageSource.getMessage("excelreports.info.user_report.user_name_header", new String[] {}, locale));
		excelData.put("GroupDescriptionHeader", messageSource.getMessage(
				"excelreports.info.user_report.group_description_header", new String[] {}, locale));
		excelData.put("CreateDateHeader",
				messageSource.getMessage("excelreports.info.user_report.create_date_header", new String[] {}, locale));
		excelData.put("UpdateDateTimeHeader", messageSource.getMessage(
				"excelreports.info.user_report.update_datetime_header", new String[] {}, locale));
		excelData.put("StatusHeader",
				messageSource.getMessage("excelreports.info.user_report.status_header", new String[] {}, locale));

		excelData.put("UserList", users);

		String excelPrefix = messageSource.getMessage(USER_REPORT_PROPERTY_CONFIG, new String[] {}, locale);

		Excel excel = new Excel(this.getTemplateExcelFilePath(USER_REPORT_PROPERTY_CONFIG), excelData);
		excel.fillTemplateData();

		Workbook wb = excel.exportWorkbook();
		exportExcel(request, response, wb, excelPrefix);
	}
}
