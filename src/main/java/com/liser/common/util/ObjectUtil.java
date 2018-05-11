package com.liser.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ObjectUtil
 * @Description: 对象工具类
 * @Company: com.yinhai
 * @author huangyh
 * @date 2013-10-8
 * @version 1.0
 */
public class ObjectUtil {
	/**
	 * @Description: 判断对象是否为空
	 * @update: 2014年4月23日 by huangyh
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object) {
		if (object == null) {
			return true;
		}
		/**
		 * if(object instanceof JSONNull){ return true; }
		 */
		if (object instanceof StringBuffer) {
			object = object.toString();
		}
		if ((object instanceof String)) {
			if (((String) object).trim().length() == 0 || "null".equals(((String) object).trim().toLowerCase()) || "undefined".equals(((String) object).trim().toLowerCase())) {
				return true;
			}
		} else if ((object instanceof Collection)) {
			if (((Collection) object).size() == 0) {
				return true;
			}
		} else if ((object instanceof Map) && ((Map) object).size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 对象是否支持序列化
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isSupportSerializable(Object value) {
		if (value == null) {
			return true;
		}
		return Serializable.class.isAssignableFrom(value.getClass());
	}

	/**
	 * 对象转换为Byte[]
	 * 
	 * @param o
	 * @return
	 */
	public static byte[] ObjectToByte(Object o) {
		if (o == null) {
			return null;
		}
		byte[] bytes = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.flush();
			bytes = baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Java对象不能序列化！'" + o + "'", e);
		}
		return bytes;
	}

	/*public static byte[] BlobToByte(Object o) {
		if (o == null) {
			return null;
		}
		byte[] bytes = null;
		try {
			if (o instanceof oracle.sql.BLOB) {
				oracle.sql.BLOB blob = (oracle.sql.BLOB) o;
				bytes = blob.getBytes(1, (int) blob.length());
			} else if (o instanceof java.sql.Blob) {
				java.sql.Blob blob = (java.sql.Blob) o;
				bytes = blob.getBytes(1, (int) blob.length());
			}
		} catch (Exception e) {
			throw new RuntimeException("数据库Blob对象不能序列化！'" + o + "'", e);
			// return null;
		}
		return bytes;
	}*/

	public static String ClobToString(Clob clob) {
		String reString = "";
		if (clob == null)
			return reString;
		Reader is = null;
		try {
			is = clob.getCharacterStream();
			// 得到流
			BufferedReader br = new BufferedReader(is);
			String s = null;
			try {
				s = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			StringBuffer sb = new StringBuffer();
			while (s != null) {
				// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
				sb.append(s);
				try {
					s = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			reString = sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reString;
	}

	/**
	 * byte[]转换为Object
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object ByteToObject(byte[] bytes) {
		try {
			if (bytes == null) {
				return null;
			}
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);
			Object object = oi.readObject();
			bi.close();
			oi.close();
			return object;
		} catch (Exception e) {
			throw new RuntimeException("不能反序列化！", e);
		}
	}

	/**
	 * @Description: Map对象内的Clob对象全部转换为String
	 * @update: 2014年4月23日 by huangyh
	 * @param resource
	 */
	public static void convertClobToString(Map<String, Object> resource) {
		if (!ObjectUtil.isEmpty(resource)) {
			for (String key : resource.keySet()) {
				Object value = resource.get(key);
				if (value instanceof Clob) {
					resource.put(key, ClobToString((Clob) value));
				}
			}
		}
	}

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * 
	 * @param type
	 *            要转化的类型
	 * @param map
	 *            包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 */
/*	public static Object map2Bean(Class type, Map<String, Object> map) {
		if (ObjectUtil.isEmpty(map)) {
			return null;
		}
		// Domain预处理
		map.remove("PK");
		map.remove("key");
		map.remove("domainObjectName");
		map.remove("metadata");
		map.remove("objid");
		String propertyName = null;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
			Object obj = type.newInstance(); // 创建 JavaBean 对象
			// 给 JavaBean 对象的属性赋值
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				propertyName = descriptor.getName();
				if (map.containsKey(propertyName)) {
					if (isEmpty(map.get(propertyName))) {
						continue;
					}
					Object[] params = descriptor.getWriteMethod().getParameterTypes();
					Object[] args = new Object[1];
					String clazz = ((Class) params[0]).getName();

					if (Long.class.getName().equals(clazz) || "long".equals(clazz)) {
						args[0] = Long.valueOf(String.valueOf(map.get(propertyName)));
					} else if (Integer.class.getName().equals(clazz) || "int".equals(clazz)) {
						args[0] = Integer.valueOf(String.valueOf(map.get(propertyName)));
					} else if (Date.class.getName().equals(clazz)) {
						args[0] = DateUtil.str2Timestamp(String.valueOf(map.get(propertyName)));
					} else {
						args[0] = String.valueOf(map.get(propertyName));
					}
					descriptor.getWriteMethod().invoke(obj, args);
				}
			}
			return obj;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("属性值" + propertyName + "转换出错");
		}
		return null;
	}*/

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 */
	public static Map<String, Object> bean2Map(Object bean) {
		String propertyName = null;
		try {
			Class type = bean.getClass();
			Map<String, Object> returnMap = new HashMap<String, Object>();
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					if (result != null) {
						returnMap.put(propertyName, result);
					} else {
						returnMap.put(propertyName, "");
					}
				}
			}
			return returnMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("属性值" + propertyName + "转换出错");
		}
		return null;
	}

	public static Object deepClone(Object src) {
		Object dest = null;
		try {
			// 将对象写到流里
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(src);
			// 从流里读回来
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bis);
			dest = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return dest;
		}
	}
	public static boolean isString(Object obj){
		if (obj instanceof String){
			return true;
		}
		return false;
	}
}
