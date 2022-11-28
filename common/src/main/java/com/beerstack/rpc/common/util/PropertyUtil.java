package com.beerstack.rpc.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 肖长佩
 * @date 2022-10-14 15:02
 * @since 1.0.0
 */
public class PropertyUtil {

    private volatile static PropertyUtil instance = null;
    private Properties property;

    public static PropertyUtil getInstance() {
        if (instance == null) {
            synchronized (PropertyUtil.class) {
                if (instance == null) {
                    instance = new PropertyUtil();
                }
            }
        }
        return instance;
    }

    private PropertyUtil() {
        initProperty();
    }

    private void initProperty() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
            property = new Properties();
            property.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(String.format("call PropertyUtil.initProperty, e.getMessage:[%s]", e.getMessage()), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(String.format("call PropertyUtil.initProperty, e.getMessage:[%s]", e.getMessage()), e);
                }
            }
        }
    }

    /**
     * 获取属性值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return this.property.getProperty(key);
    }

    /**
     * 获取属性值
     *
     * @param key
     * @return
     */
    public String getIfNullThrowEx(String key) {
        String value = this.property.getProperty(key);
        if (value == null || value.length() == 0) {
            throw new NullPointerException("load property is null by key : " + key);
        }
        return value;
    }

    /**
     * 获取属性值【为空取默认值】
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getOrDefault(String key, String defaultValue) {
        return StringUtils.defaultIfBlank(get(key), defaultValue);
    }

}
