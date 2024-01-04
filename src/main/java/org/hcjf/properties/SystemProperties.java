package org.hcjf.properties;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.hcjf.log.Log;
import org.hcjf.utils.Introspection;
import org.hcjf.utils.JsonUtils;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class overrides the system properties default implementation adding
 * some default values and properties definitions for the service-oriented platforms
 * works.
 * @author javaito
 */
public final class SystemProperties extends Properties {

    public static final String HCJF_DEFAULT_DATE_FORMAT = "hcjf.default.date.format";
    public static final String HCJF_DEFAULT_DATE_FORMAT_REGEX = "hcjf.default.date.format.regex";
    public static final String HCJF_DEFAULT_LOCAL_DATE_FORMAT_REGEX = "hcjf.default.date.format.regex";
    public static final String HCJF_DEFAULT_NUMBER_FORMAT = "hcjf.default.number.format";
    public static final String HCJF_DEFAULT_SCIENTIFIC_NUMBER_FORMAT = "hcjf.default.scientific.number.format";
    public static final String HCJF_DEFAULT_DECIMAL_SEPARATOR = "hcjf.default.decimal.separator";
    public static final String HCJF_DEFAULT_GROUPING_SEPARATOR = "hcjf.default.grouping.separator";
    public static final String HCJF_DEFAULT_PROPERTIES_FILE_PATH = "hcjf.default.properties.file.path";
    public static final String HCJF_DEFAULT_PROPERTIES_FILE_XML = "hcjf.default.properties.file.xml";
    public static final String HCJF_UUID_REGEX = "hcjf.uuid.regex";
    public static final String HCJF_INTEGER_NUMBER_REGEX = "hcjf.integer.number.regex";
    public static final String HCJF_DECIMAL_NUMBER_REGEX = "hcjf.decimal.number.regex";
    public static final String HCJF_SCIENTIFIC_NUMBER_REGEX = "hcjf.scientific.number.regex";
    public static final String HCJF_MATH_REGULAR_EXPRESSION = "hcjf.math.regular.expression";
    public static final String HCJF_MATH_CONNECTOR_REGULAR_EXPRESSION = "hcjf.math.connector.regular.expression";
    public static final String HCJF_MATH_SPLITTER_REGULAR_EXPRESSION = "hcjf.math.splitter.regular.expression";
    public static final String HCJF_DEFAULT_LRU_MAP_SIZE = "hcjf.default.lru.map.size";
    public static final String HCJF_DEFAULT_EXCEPTION_MESSAGE_TAG = "hcjf.default.exception.message.tag";
    public static final String HCJF_CHECKSUM_ALGORITHM = "hcjf.checksum.algorithm";
    public static final String HCJF_NODE_NAME = "hcjf.node.name";

    public static final class Cryptography{

        public static final String KEY = "hcjf.cryptography.key";
        public static final String ALGORITHM = "hcjf.cryptography.algorithm";
        public static final String OPERATION_MODE = "hcjf.cryptography.operation.mode";
        public static final String PADDING_SCHEME = "hcjf.cryptography.padding.scheme";
        public static final String AAD = "hcjf.cryptography.aad";

        public static final class Random {
            public static final String IV_SIZE = "hcjf.cryptography.random.iv.size";
        }

        public static final class GCM {
            public static final String TAG_BIT_LENGTH = "hcjf.cryptography.gcm.tag.bit.length";
        }
    }

    public static final class Layer {
        public static final String LOG_TAG = "hcjf.layers.log.tag";
        public static final String READABLE_ALL_LAYER_IMPLEMENTATION_NAME = "hcjf.layers.readable.all.layer.implementation.name";
        public static final String READABLE_LAYER_IMPLEMENTATION_NAME =  "hcjf.layers.readable.layer.implementation.name";
        public static final String PLUGIN_THREADING_GRANT = "hcjf.layers.plugin.threading.grant";
        public static final String PLUGIN_FILE_ACCESS_GRANT = "hcjf.layers.plugin.file.access.grant";
        public static final String DISTRIBUTED_LAYER_ENABLED = "hcjf.layers.distributed.layer.enabled";
        public static final String NETWORK_SOCKET_IMPLEMENTATION = "hcjf.layers.distributed.layer.network.socket.implementation";
    }

    public static final class Log {
        public static final String SERVICE_NAME = "hcjf.log.service.name";
        public static final String SERVICE_PRIORITY = "hcjf.log.service.priority";
        public static final String FILE_PREFIX = "hcjf.log.file.prefix";
        public static final String ERROR_FILE = "hcjf.log.error.file";
        public static final String WARNING_FILE = "hcjf.log.warning.file";
        public static final String INFO_FILE = "hcjf.log.info.file";
        public static final String DEBUG_FILE = "hcjf.log.debug.file";
        public static final String LEVEL = "hcjf.log.level";
        public static final String DATE_FORMAT = "hcjf.log.date.format";
        public static final String CONSUMERS = "hcjf.log.consumers";
        public static final String SYSTEM_OUT_ENABLED = "hcjf.log.system.out.enabled";
        public static final String JAVA_STANDARD_LOGGER_ENABLED = "hcjf.log.java.standard.logger.enabled";
        public static final String QUEUE_INITIAL_SIZE = "hcjf.log.queue.initial.size";
        public static final String TRUNCATE_TAG = "hcjf.log.truncate.tag";
        public static final String TRUNCATE_TAG_SIZE = "hcjf.log.truncate.tag.size";
        public static final String LOG_CONSUMERS_SIZE = "hcjf.log.consumers.size";
    }

    public static final class Service {
        public static final String STATIC_VIRTUAL_THREAD_POOL = "hcjf.service.static.virtual.thread.pool";
        public static final String STATIC_THREAD_NAME = "hcjf.service.static.thread.name";
        public static final String STATIC_THREAD_POOL_CORE_SIZE = "hcjf.service.static.thread.pool.core.size";
        public static final String STATIC_THREAD_POOL_MAX_SIZE = "hcjf.service.static.thread.pool.max.size";
        public static final String STATIC_THREAD_POOL_KEEP_ALIVE_TIME = "hcjf.service.static.thread.pool.keep.alive.time";
        public static final String VIRTUAL_THREAD_POOL = "hcjf.service.virtual.thread.pool";
        public static final String THREAD_POOL_CORE_SIZE = "hcjf.service.thread.pool.core.size";
        public static final String THREAD_POOL_MAX_SIZE = "hcjf.service.thread.pool.max.size";
        public static final String THREAD_POOL_KEEP_ALIVE_TIME = "hcjf.service.thread.pool.keep.alive.time";
        public static final String GUEST_SESSION_NAME = "hcjf.service.guest.session.name";
        public static final String SYSTEM_SESSION_NAME = "hcjf.service.system.session.name";
        public static final String SHUTDOWN_TIME_OUT = "hcjf.service.shutdown.time.out";
        public static final String MAX_ALLOCATED_MEMORY_EXPRESSED_IN_PERCENTAGE = "hcjf.service.max.allocated.memory.expressed.in.percentage";
        public static final String MAX_ALLOCATED_MEMORY_FOR_THREAD = "max.allocated.memory.for.thread";
        public static final String MAX_EXECUTION_TIME_FOR_THREAD = "max.execution.time.for.thread";
        public static final String MAX_ALLOCATED_MEMORY_EXCEEDED_THROWS_EXCEPTION = "max.allocated.memory.exceeded.throws.exception";
    }

    private static final String PROPERTY_PACKAGE = "org.hcjf.properties";

    //Java property names
    public static final String FILE_ENCODING = "file.encoding";

    private static final SystemProperties instance;

    static {
        instance = new SystemProperties();
        for (Class clazz : Introspection.getClasses(PROPERTY_PACKAGE)) {
            if (!clazz.equals(DefaultProperties.class) && DefaultProperties.class.isAssignableFrom(clazz)) {
                try {
                    DefaultProperties defaultProperties = (DefaultProperties) clazz.getConstructor().newInstance();
                    instance.putAll(defaultProperties.getDefaults());
                } catch (Exception e) {
                    System.out.println("Unable to load properties from " + clazz.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private final Map<String, Object> instancesCache;
    private final Gson gson;


    private SystemProperties() {
        super(new Properties());
        instancesCache = new HashMap<>();
        gson = new Gson();

        defaults.put(HCJF_DEFAULT_DATE_FORMAT, "yyyy-MM-dd HH:mm:ss");
        defaults.put(HCJF_DEFAULT_DATE_FORMAT_REGEX, "(19|20)\\d\\d([- /.])(0[1-9]|1[012])\\2(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");
        defaults.put(HCJF_DEFAULT_LOCAL_DATE_FORMAT_REGEX, "");
        defaults.put(HCJF_DEFAULT_NUMBER_FORMAT, "0.000");
        defaults.put(HCJF_DEFAULT_SCIENTIFIC_NUMBER_FORMAT, "0.00E00");
        defaults.put(HCJF_DEFAULT_DECIMAL_SEPARATOR, ".");
        defaults.put(HCJF_DEFAULT_GROUPING_SEPARATOR, ",");
        defaults.put(HCJF_DEFAULT_PROPERTIES_FILE_XML, "false");
        defaults.put(HCJF_UUID_REGEX, "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        defaults.put(HCJF_INTEGER_NUMBER_REGEX, "^[-]?[0-9]{1,}$");
        defaults.put(HCJF_DECIMAL_NUMBER_REGEX, "^[-]?[0-9,\\.]{0,}[0-9]{1,}$");
        defaults.put(HCJF_SCIENTIFIC_NUMBER_REGEX, "^[-]?[0-9,\\.]{0,}[0-9]{1,}E[-]?[0-9]{1,}$");
        defaults.put(HCJF_MATH_REGULAR_EXPRESSION, "[-]?(((?<subExpression>¿[\\d]+·)|(?<variable>[a-z A-Z()$_]+)|(?<decimal>[\\d]+\\.[\\d]+)|(?<integer>[\\d]+))(?<operator>[\\-+/*^%=<>! ]?))+");
        defaults.put(HCJF_MATH_CONNECTOR_REGULAR_EXPRESSION, ".*[+\\-*/%=<>!].*");
        defaults.put(HCJF_MATH_SPLITTER_REGULAR_EXPRESSION, "(?<=(\\+|\\-|\\*|/|%|=|>|<|<>|!=|>=|<=))|(?=(\\+|\\-|\\*|/|%|=|>|<|<>|!=|>=|<=))");
        defaults.put(HCJF_DEFAULT_LRU_MAP_SIZE, "1000");
        defaults.put(HCJF_DEFAULT_EXCEPTION_MESSAGE_TAG, "IMPL");
        defaults.put(HCJF_CHECKSUM_ALGORITHM, "MD5");
        defaults.put(HCJF_NODE_NAME, "localhost");

        defaults.put(Cryptography.KEY,"71324dccdb58966a04507b0fe2008632940b87c6dc5cea5f4bdf0d0089524c8e");
        defaults.put(Cryptography.ALGORITHM,"AES");
        defaults.put(Cryptography.OPERATION_MODE,"ECB");
        defaults.put(Cryptography.PADDING_SCHEME,"PKCS5Padding");
        defaults.put(Cryptography.Random.IV_SIZE,"96");
        defaults.put(Cryptography.GCM.TAG_BIT_LENGTH,"128");
        defaults.put(Cryptography.AAD,"HolandaCatalinaCrypt");

        defaults.put(Layer.LOG_TAG, "LAYER");
        defaults.put(Layer.READABLE_ALL_LAYER_IMPLEMENTATION_NAME, "system_layer");
        defaults.put(Layer.READABLE_LAYER_IMPLEMENTATION_NAME, "system_readable_layer");
        defaults.put(Layer.DISTRIBUTED_LAYER_ENABLED, "false");

        defaults.put(Log.SERVICE_NAME, "LogService");
        defaults.put(Log.SERVICE_PRIORITY, "0");
        defaults.put(Log.FILE_PREFIX, "hcjf");
        defaults.put(Log.ERROR_FILE, "false");
        defaults.put(Log.WARNING_FILE, "false");
        defaults.put(Log.INFO_FILE, "false");
        defaults.put(Log.DEBUG_FILE, "false");
        defaults.put(Log.LEVEL, "1");
        defaults.put(Log.DATE_FORMAT, "yyyy-MM-dd HH:mm:ss,SSS");
        defaults.put(Log.CONSUMERS, "[]");
        defaults.put(Log.SYSTEM_OUT_ENABLED, "false");
        defaults.put(Log.JAVA_STANDARD_LOGGER_ENABLED, "false");
        defaults.put(Log.QUEUE_INITIAL_SIZE, "10000");
        defaults.put(Log.TRUNCATE_TAG, "false");
        defaults.put(Log.TRUNCATE_TAG_SIZE, "35");
        defaults.put(Log.LOG_CONSUMERS_SIZE, "50");

        defaults.put(Service.STATIC_VIRTUAL_THREAD_POOL, "true");
        defaults.put(Service.STATIC_THREAD_NAME, "StaticServiceThread");
        defaults.put(Service.STATIC_THREAD_POOL_CORE_SIZE, "2");
        defaults.put(Service.STATIC_THREAD_POOL_MAX_SIZE, "200");
        defaults.put(Service.STATIC_THREAD_POOL_KEEP_ALIVE_TIME, "10");
        defaults.put(Service.VIRTUAL_THREAD_POOL, "true");
        defaults.put(Service.THREAD_POOL_CORE_SIZE, "10");
        defaults.put(Service.THREAD_POOL_MAX_SIZE, "100");
        defaults.put(Service.THREAD_POOL_KEEP_ALIVE_TIME, "10");
        defaults.put(Service.GUEST_SESSION_NAME, "Guest");
        defaults.put(Service.SYSTEM_SESSION_NAME, "System");
        defaults.put(Service.SHUTDOWN_TIME_OUT, "1000");
        defaults.put(Service.MAX_ALLOCATED_MEMORY_EXPRESSED_IN_PERCENTAGE, "true");
        defaults.put(Service.MAX_ALLOCATED_MEMORY_EXCEEDED_THROWS_EXCEPTION, "false");
        defaults.put(Service.MAX_ALLOCATED_MEMORY_FOR_THREAD, "15");
        defaults.put(Service.MAX_EXECUTION_TIME_FOR_THREAD, Long.toString(10*1000*1000*1000));

        Properties system = System.getProperties();
        putAll(system);
        System.setProperties(this);
    }

    /**
     * Put the default value for a property.
     * @param propertyName Property name.
     * @param defaultValue Property default value.
     * @throws NullPointerException Throw a {@link NullPointerException} when the
     * property name or default value are null.
     */
    public static void putDefaultValue(String propertyName, String defaultValue) {
        if(propertyName == null) {
            throw new NullPointerException("Invalid property name null");
        }

        if(defaultValue == null) {
            throw new NullPointerException("Invalid default value null");
        }

        instance.defaults.put(propertyName, defaultValue);
    }

    /**
     * Calls the Hashtable method {@code put}. Provided for
     * parallelism with the getProperty method. Enforces use of
     * strings for property keys and values. The value returned is the
     * result of the Hashtable call to {@code put}.
     *
     * @param key   the key to be placed into this property list.
     * @param value the value corresponding to key.
     * @return the previous value of the specified key in this property
     * list, or {@code null} if it did not have one.
     * @see #getProperty
     * @since 1.2
     */
    @Override
    public synchronized Object setProperty(String key, String value) {
        Object result = super.setProperty(key, value);

        synchronized (instancesCache) {
            instancesCache.remove(key);
        }

        try {
            //TODO: Create listeners
        } catch (Exception ex){}

        return result;
    }

    /**
     * This method return the string value of the system property
     * named like the parameter.
     * @param propertyName Name of the find property.
     * @param validator Property validator.
     * @return Return the value of the property or null if the property is no defined.
     */
    public static String get(String propertyName, PropertyValueValidator<String> validator) {
        String result = System.getProperty(propertyName);

        if(validator != null) {
            if(!validator.validate(result)){
                throw new IllegalPropertyValueException(propertyName + "=" + result);
            }
        }

        return result;
    }

    /**
     * This method return the string value of the system property
     * named like the parameter.
     * @param propertyName Name of the find property.
     * @return Return the value of the property or null if the property is no defined.
     */
    public static String get(String propertyName) {
        return get(propertyName, null);
    }

    /**
     * This method return the value of the system property as boolean.
     * @param propertyName Name of the find property.
     * @return Value of the system property as boolean, or null if the property is not found.
     */
    public static Boolean getBoolean(String propertyName) {
        return getBoolean(propertyName, null);
    }

    /**
     * This method return the value of the system property as boolean.
     * @param propertyName Name of the find property.
     * @param defaultValue If the property value is null then the method returns a default value.
     * @return Value of the system property as boolean, or null if the property is not found.
     */
    public static Boolean getBoolean(String propertyName, Boolean defaultValue) {
        Boolean result = null;

        synchronized (instance.instancesCache) {
            result = (Boolean) instance.instancesCache.get(propertyName);
            if (result == null) {
                String propertyValue = get(propertyName);
                try {
                    if (propertyValue != null) {
                        result = Boolean.valueOf(propertyValue);
                        instance.instancesCache.put(propertyName, result);
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a boolean valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        if(result == null) {
            result = defaultValue;
        }

        return result;
    }

    /**
     * This method return the value of the system property as integer.
     * @param propertyName Name of the find property.
     * @return Value of the system property as integer, or null if the property is not found.
     */
    public static Integer getInteger(String propertyName) {
        return getInteger(propertyName, null);
    }

    /**
     * This method return the value of the system property as integer.
     * @param propertyName Name of the find property.
     * @param defaultValue If the property value is null then the method returns a default value.
     * @return Value of the system property as integer, or null if the property is not found.
     */
    public static Integer getInteger(String propertyName, Integer defaultValue) {
        Integer result = null;

        synchronized (instance.instancesCache) {
            result = (Integer) instance.instancesCache.get(propertyName);
            if (result == null) {
                String propertyValue = get(propertyName);
                try {
                    if (propertyValue != null) {
                        result = Integer.decode(propertyValue);
                        instance.instancesCache.put(propertyName, result);
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a integer valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        if(result == null) {
            result = defaultValue;
        }

        return result;
    }

    /**
     * This method return the value of the system property as long.
     * @param propertyName Name of the find property.
     * @return Value of the system property as long, or null if the property is not found.
     */
    public static Long getLong(String propertyName) {
        return getLong(propertyName, null);
    }

    /**
     * This method return the value of the system property as long.
     * @param propertyName Name of the find property.
     * @param defaultValue If the property value is null then the method returns a default value.
     * @return Value of the system property as long, or null if the property is not found.
     */
    public static Long getLong(String propertyName, Long defaultValue) {
        Long result = null;

        synchronized (instance.instancesCache) {
            result = (Long) instance.instancesCache.get(propertyName);
            if (result == null) {
                String propertyValue = get(propertyName);
                try {
                    if (propertyValue != null) {
                        result = Long.decode(propertyValue);
                        instance.instancesCache.put(propertyName, result);
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a long valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        if(result == null) {
            result = defaultValue;
        }

        return result;
    }

    /**
     * This method return the value of the system property as double.
     * @param propertyName Name of the find property.
     * @return Value of the system property as double, or null if the property is not found.
     */
    public static Double getDouble(String propertyName) {
        return getDouble(propertyName, null);
    }

    /**
     * This method return the value of the system property as double.
     * @param propertyName Name of the find property.
     * @param defaultValue If the property value is null then the method returns a default value.
     * @return Value of the system property as double, or null if the property is not found.
     */
    public static Double getDouble(String propertyName, Double defaultValue) {
        Double result = null;

        synchronized (instance.instancesCache) {
            result = (Double) instance.instancesCache.get(propertyName);
            if(result == null) {
                String propertyValue = get(propertyName);
                try {
                    if (propertyValue != null) {
                        result = Double.valueOf(propertyValue);
                        instance.instancesCache.put(propertyName, result);
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a double valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        if(result == null) {
            result = defaultValue;
        }

        return result;
    }

    /**
     * This method return the value of the system property as {@link UUID} instance.
     * @param propertyName Name of the find property.
     * @return Value of the system property as {@link UUID} instance, or null if the property is not found.
     */
    public static UUID getUUID(String propertyName) {
        UUID result = null;

        synchronized (instance.instancesCache) {
            result = (UUID) instance.instancesCache.get(propertyName);
            if(result == null) {
                String propertyValue = get(propertyName);
                try {
                    if (propertyValue != null) {
                        result = UUID.fromString(propertyValue);
                        instance.instancesCache.put(propertyName, result);
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a UUID valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }

        return result;
    }

    /**
     * This method return the value of the system property as {@link Path} instance.
     * @param propertyName Name of the find property.
     * @return Value of the system property as {@link Path} instance, or null if the property is not found.
     */
    public static Path getPath(String propertyName) {
        Path result = null;

        synchronized (instance.instancesCache) {
            result = (Path) instance.instancesCache.get(propertyName);
            if(result == null) {
                String propertyValue = get(propertyName);
                try {
                    if (propertyValue != null) {
                        result = Paths.get(propertyValue);
                        instance.instancesCache.put(propertyName, result);
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a path valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }

        return result;
    }

    /**
     * This method return the value of the system property as class.
     * @param propertyName Name of the find property.
     * @param <O> Type of the class instance expected.
     * @return Class instance.
     */
    public static <O extends Object> Class<O> getClass(String propertyName) {
        Class<O> result;

        synchronized (instance.instancesCache) {
            result = (Class<O>) instance.instancesCache.get(propertyName);
            if(result == null) {
                String propertyValue = get(propertyName);
                try {
                    if(propertyValue != null) {
                        result = (Class<O>) Class.forName(propertyValue);
                        instance.instancesCache.put(propertyName, result);
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a class name valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }

        return result;
    }

    /**
     * Return the default charset of the JVM instance.
     * @return Default charset.
     */
    public static String getDefaultCharset() {
        return System.getProperty(FILE_ENCODING);
    }

    /**
     * This method return the value of the property as Locale instance.
     * The instance returned will be stored on near cache and will be removed when the
     * value of the property has been updated.
     * @param propertyName Name of the property that contains locale representation.
     * @return Locale instance.
     */
    public static java.util.Locale getLocale(String propertyName) {
        java.util.Locale result;
        synchronized (instance.instancesCache) {
            result = (java.util.Locale) instance.instancesCache.get(propertyName);
            if(result == null) {
                String propertyValue = get(propertyName);
                try {
                    result = java.util.Locale.forLanguageTag(propertyValue);
                    instance.instancesCache.put(propertyName, result);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a locale tag valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        return result;
    }

    /**
     * This method return the valuo of the property called 'hcjf.default.locale' as a locale instance.
     * The instance returned will be stored on near cache and will be removed when the
     * value of the property has been updated.
     * @return Locale instance.
     */
    public static java.util.Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * This method return the value of the property as a DecimalFormat instnace.
     * The instance returned will be stored on near cache and will be removed when the
     * value of the property has been updated.
     * @param propertyName Name of the property that contains decimal pattern.
     * @return DecimalFormat instance.
     */
    public static DecimalFormat getDecimalFormat(String propertyName) {
        DecimalFormat result;
        synchronized (instance.instancesCache) {
            result = (DecimalFormat) instance.instancesCache.get(propertyName);
            if(result == null) {
                String propertyValue = get(propertyName);
                try {
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator(get(HCJF_DEFAULT_DECIMAL_SEPARATOR).charAt(0));
                    symbols.setGroupingSeparator(get(HCJF_DEFAULT_GROUPING_SEPARATOR).charAt(0));
                    result = new DecimalFormat(propertyValue, symbols);
                    instance.instancesCache.put(propertyName, result);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a decimal pattern valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        return result;
    }

    /**
     * This method return the value of the property as a SimpleDateFormat instance.
     * The instance returned will be stored on near cache and will be removed when the
     * value of the property has been updated.
     * @param propertyName Name of the property that contains date representation.
     * @return Simple date format instance.
     */
    public static SimpleDateFormat getDateFormat(String propertyName) {
        SimpleDateFormat result;
        synchronized (instance.instancesCache) {
            result = (SimpleDateFormat) instance.instancesCache.get(propertyName);
            if(result == null) {
                String propertyValue = get(propertyName);
                try {
                    result = new SimpleDateFormat(get(propertyName));
                    instance.instancesCache.put(propertyName, result);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a date pattern valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        return result;
    }

    /**
     * Returns the implementation object expected for the specific object type.
     * @param propertyName Name of the property.
     * @param objectType Object type.
     * @param <O> Expected object instance.
     * @return Object instance.
     */
    public static <O extends Object> O getObject(String propertyName, Class<O> objectType) {
        try {
            return  instance.gson.fromJson(get(propertyName), objectType);
        } catch (Exception ex) {
            throw new IllegalArgumentException("The property value has not a json object valid to create the instance: '"
                    + propertyName + ":" + objectType + "'", ex);
        }
    }

    /**
     * Returns a list of expected object instances for the specific object type.
     * @param propertyName Name of the property.
     * @param objectType Object type.
     * @param <O> Expected object instance.
     * @return Object instances.
     */
    public static <O extends Object> List<O> getObjects(String propertyName, Class<O> objectType) {
        List<O> result = new ArrayList<>();
        try {
            JsonArray array = JsonParser.parseString(get(propertyName)).getAsJsonArray();
            Iterator<JsonElement> iterator = array.iterator();
            while(iterator.hasNext()) {
                result.add(instance.gson.fromJson(iterator.next(), objectType));
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("The property value has not a json object valid to create the instance: '"
                    + propertyName + ":" + objectType + "'", ex);
        }
        return result;
    }

    /**
     * This method return the value of the property as instance of list.
     * @param propertyName Name of the property that contains the json array representation.
     * @return List instance.
     */
    public static List<String> getList(String propertyName) {
        String propertyValue = get(propertyName);
        List<String> result = new ArrayList<>();
        synchronized (instance.instancesCache) {
            if (instance.instancesCache.containsKey(propertyName)) {
                result.addAll((List<? extends String>) instance.instancesCache.get(propertyName));
            } else {
                try {
                    result.addAll((Collection<? extends String>) JsonUtils.createObject(propertyValue));
                    List<String> cachedResult = new ArrayList<>();
                    cachedResult.addAll(result);
                    instance.instancesCache.put(propertyName, cachedResult);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a json array valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        return result;
    }

    /**
     * This method return the value of the property as instance of set.
     * @param propertyName Name of the property that contains the json array representation.
     * @return Set instance.
     */
    public static Set<String> getSet(String propertyName) {
        String propertyValue = get(propertyName);
        Set<String> result = new TreeSet<>();
        synchronized (instance.instancesCache) {
            if (instance.instancesCache.containsKey(propertyName)) {
                result.addAll((List<? extends String>) instance.instancesCache.get(propertyName));
            } else {
                try {
                    result.addAll((Collection<? extends String>) JsonUtils.createObject(propertyValue));
                    List<String> cachedResult = new ArrayList<>();
                    cachedResult.addAll(result);
                    instance.instancesCache.put(propertyName, cachedResult);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a json array valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        return result;
    }

    /**
     * This method return the value of the property as instance of map.
     * @param propertyName The name of the property that contains the json object representation.
     * @return Map instance.
     */
    public static Map<String, String> getMap(String propertyName) {
        String propertyValue = get(propertyName);
        Map<String, String> result = new HashMap<>();
        synchronized (instance.instancesCache) {
            if (instance.instancesCache.containsKey(propertyName)) {
                result.putAll((Map<String, String>) instance.instancesCache.get(propertyName));
            } else {
                try {
                    result.putAll((Map<? extends String, ? extends String>) JsonUtils.createObject(propertyValue));
                    Map<String, String> cachedResult = new HashMap<>();
                    cachedResult.putAll(result);
                    instance.instancesCache.put(propertyName, cachedResult);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a json object valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        return result;
    }

    public static Pattern getPattern(String propertyName) {
        return getPattern(propertyName, 0);
    }

    /**
     * Return the compiled pattern from the property value.
     * @param propertyName Name of the property.
     * @param flags Regex flags.
     * @return Compiled pattern.
     */
    public static Pattern getPattern(String propertyName, int flags) {
        String propertyValue = get(propertyName);
        Pattern result;
        synchronized (instance.instancesCache) {
            if(instance.instancesCache.containsKey(propertyName)) {
                result = (Pattern) instance.instancesCache.get(propertyName);
            } else {
                try {
                    result = Pattern.compile(propertyValue, flags);
                    instance.instancesCache.put(propertyName, result);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("The property value has not a regex valid format: '"
                            + propertyName + ":" + propertyValue + "'", ex);
                }
            }
        }
        return result;
    }

}
