package club.magicfun.aquila.util;

import java.util.HashMap;
import java.util.Map;

public class ExcelTemplateNameUtil {
    
    static Map<String, String> reportTemplateNameMap = new HashMap<String, String>();
    
    static {
        reportTemplateNameMap.put("excelreports.info.user_report", "UserReportTemplate");
    }
    
    public static String getReportTemplateNameValue(String name) {
        return reportTemplateNameMap.get(name);
    }
    
}
