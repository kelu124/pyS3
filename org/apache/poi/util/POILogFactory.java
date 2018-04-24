package org.apache.poi.util;

import java.util.HashMap;
import java.util.Map;

@Internal
public final class POILogFactory {
    static String _loggerClassName = null;
    private static Map<String, POILogger> _loggers = new HashMap();
    private static final POILogger _nullLogger = new NullLogger();

    private POILogFactory() {
    }

    public static POILogger getLogger(Class<?> theclass) {
        return getLogger(theclass.getName());
    }

    public static POILogger getLogger(String cat) {
        if (_loggerClassName == null) {
            try {
                _loggerClassName = System.getProperty("org.apache.poi.util.POILogger");
            } catch (Exception e) {
            }
            if (_loggerClassName == null) {
                _loggerClassName = _nullLogger.getClass().getName();
            }
        }
        if (_loggerClassName.equals(_nullLogger.getClass().getName())) {
            return _nullLogger;
        }
        POILogger logger = (POILogger) _loggers.get(cat);
        if (logger != null) {
            return logger;
        }
        try {
            logger = (POILogger) Class.forName(_loggerClassName).newInstance();
            logger.initialize(cat);
        } catch (Exception e2) {
            logger = _nullLogger;
            _loggerClassName = _nullLogger.getClass().getName();
        }
        _loggers.put(cat, logger);
        return logger;
    }
}
