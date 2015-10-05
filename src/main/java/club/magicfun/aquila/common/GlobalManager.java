package club.magicfun.aquila.common;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

public class GlobalManager {

	public static final int DEFAULT_PAGE_SIZE = 5;
	
	public static final int SORT_ORDER_AUTO_INCREMENT = 1;

	public static Map<String, Object> getGlobalPageableMap(Page<?> page) {

		Map<String, Object> pageableMap = new HashMap<String, Object>();

		int current = page.getNumber() + 1;
		int begin = Math.max(1, current - 5);
		int end = Math.min(begin + 10, page.getTotalPages());
		int totalPages = page.getTotalPages();

		pageableMap.put("currentIndex", current);
		pageableMap.put("beginIndex", begin);
		pageableMap.put("endIndex", end);
		pageableMap.put("totalPages", totalPages);

		return pageableMap;
	}
	
	public static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
