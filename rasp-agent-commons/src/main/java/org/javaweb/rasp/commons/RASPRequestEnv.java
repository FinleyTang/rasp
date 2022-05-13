package org.javaweb.rasp.commons;

import org.javaweb.rasp.commons.config.RASPAppProperties;
import org.javaweb.rasp.commons.config.RASPPropertiesConfiguration;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.*;

import static org.javaweb.rasp.commons.log.RASPLogger.getLoggerName;
import static org.javaweb.rasp.commons.log.RASPLogger.hasLogger;
import static org.javaweb.rasp.commons.config.RASPConfiguration.RASP_APP_CONFIG_DIRECTORY;
import static org.javaweb.rasp.commons.config.RASPConfiguration.getWebApplicationConfig;
import static org.javaweb.rasp.commons.loader.AgentConstants.ATTACK_LOGGER_PREFIX;
import static org.javaweb.rasp.commons.utils.URLUtils.getStandardContextPath;

public class RASPRequestEnv {

	private final String documentName;

	private final RASPPropertiesConfiguration<RASPAppProperties> appConfig;

	public static final Set<Class<?>> API_CLASS_LIST = new HashSet<Class<?>>();

	public static final Class<?>[] ARG_TYPES = new Class[]{Map.class, RASPRequestEnv.class};

	public static final Map<String, Method> API_METHOD_MAP = new HashMap<String, Method>();

	public RASPRequestEnv(RASPPropertiesConfiguration<RASPAppProperties> appConfig) {
		this.appConfig = appConfig;
		String configFileName = appConfig.getConfigFile().getName();
		this.documentName = configFileName.substring(0, configFileName.lastIndexOf("."));
	}

	public String getDocumentName() {
		return documentName;
	}

	public RASPPropertiesConfiguration<RASPAppProperties> getAppConfig() {
		return appConfig;
	}

	/**
	 * 获取Web应用的RASP配置对象
	 *
	 * @return 应用配置列表
	 */
	public static List<RASPPropertiesConfiguration<RASPAppProperties>> getAppConfigList() {
		List<RASPPropertiesConfiguration<RASPAppProperties>> configList =
				new ArrayList<RASPPropertiesConfiguration<RASPAppProperties>>();

		File[] apps = RASP_APP_CONFIG_DIRECTORY.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".properties");
			}
		});

		if (apps == null) return configList;

		for (File app : apps) {
			// 截取web应用名称
			String appName     = app.getName().substring(0, app.getName().lastIndexOf('.'));
			String contextPath = getStandardContextPath(appName);
			String loggerName  = getLoggerName(ATTACK_LOGGER_PREFIX, contextPath);

			if (hasLogger(loggerName)) {
				configList.add(getWebApplicationConfig(contextPath));
			}
		}

		return configList;
	}

}
