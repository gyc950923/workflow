package com.online.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Element;

/**
 * 对Bean进行操作的相关工具方法
 * 
 */
public class BeanUtil {

	public static String[] datePattern = new String[] {
			"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
			"yyyy-MM", "yyyy-MM-dd HH:mm" };

	/**
	 * 将Bean对象转换成Map对象，将忽略掉值为null或size=0的属性
	 * 
	 * @param obj
	 *            对象
	 * @return 若给定对象为null则返回size=0的map对象
	 */
	public static Map<String, Object> toMap(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (obj == null) {
			return map;
		}
		BeanMap beanMap = new BeanMap(obj);
		@SuppressWarnings("unchecked")
		Iterator<String> it = beanMap.keyIterator();
		while (it.hasNext()) {
			String name = it.next();
			Object value = beanMap.get(name);
			// 转换时会将类名也转换成属性，此处去掉
			if (value != null && !name.equals("class")) {
				map.put(name, value);
			}
		}
		return map;
	}

	/**
	 * 该方法将给定的所有对象参数列表转换合并生成一个Map，对于同名属性，依次由后面替换前面的对象属性
	 * 
	 * @param objs
	 *            对象列表
	 * @return 对于值为null的对象将忽略掉
	 */
	public static Map<String, Object> toMap(Object... objs) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Object object : objs) {
			if (object != null) {
				map.putAll(toMap(object));
			}
		}
		return map;
	}

	/**
	 * 获取接口的泛型类型，如果不存在则返回null
	 * 
	 * @param clazz
	 * @return
	 */
	public static Class<?> getGenericClass(Class<?> clazz) {
		Type t = clazz.getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			return ((Class<?>) p[0]);
		}
		return null;
	}

	// 属性复制
	@SuppressWarnings("rawtypes")
	public static Boolean copyFields(Object tar, Object src) {

		if (null == tar || null == src)
			return false;

		Boolean b = true;
		Field tarField = null;
		String fieldName = null;

		Class tarClazz = tar.getClass();// 取得目标class对象
		Class srcClazz = src.getClass();// 取得源class对象
		Field[] srcFields = srcClazz.getDeclaredFields();// 取得目标字段数组

		try {
			for (Field field : srcFields) {
				fieldName = field.getName();
				if (fieldName.equals("serialVersionUID")) { // 跳过序列化的属性
					continue;
				}
				tarField = tarClazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				if (tarField != null
						&& field.getType().equals(tarField.getType())
						&& field.get(src) != null) {
					tarField.setAccessible(true);
					tarField.set(tar, field.get(src));
					;
				} else {
					continue;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean copyFields(Map tar, Object src) {
		if (null == tar || null == src)
			return false;

		Class srcClazz = src.getClass();// 取得源class对象
		Field[] srcFields = srcClazz.getDeclaredFields();// 取得源字段数组

		for (Field field : srcFields) {
			String fieldname = field.getName();// 取得源字段的名字
			String name = capitaliz(fieldname);// 将字段名字首字母大写
			try {
				String getMethodName = "get" + name;
				if ("getSerialVersionUID".equals(getMethodName))
					continue;

				Method merhod = srcClazz.getDeclaredMethod(getMethodName);
				Object val = merhod.invoke(src);

				if (null != val) {// 判断是否和实体继承的基类相等

					tar.put(fieldname, val);
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		return true;
	}

	@SuppressWarnings({ "rawtypes" })
	public static boolean copyFields(Element tar, Object src) {
		if (null == tar || null == src)
			return false;

		Class srcClazz = src.getClass();// 取得源class对象
		Field[] srcFields = srcClazz.getDeclaredFields();// 取得源字段数组

		for (Field field : srcFields) {
			String fieldname = field.getName();// 取得源字段的名字
			String name = capitaliz(fieldname);// 将字段名字首字母大写
			try {
				String getMethodName = "get" + name;
				if ("getSerialVersionUID".equals(getMethodName))
					continue;

				Method merhod = srcClazz.getDeclaredMethod(getMethodName);
				Object val = merhod.invoke(src);

				if (null == val)
					val = "";
				if (val instanceof Date) {
					val = DateFormatUtils.format((Date) val,
							"yyyy-MM-dd HH:mm:ss");
				} else if (val instanceof BigDecimal) {
				}

				tar.addElement(fieldname).setText(String.valueOf(val));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public static boolean copyFields(Object tar, Map src) {

		if (null == tar || null == src)
			return false;

		Class tarClazz = tar.getClass();// 取得目标class对象
		Field[] tarFields = tarClazz.getDeclaredFields();// 取得目标字段数组

		for (Field field : tarFields) {

			String fieldname = field.getName();// 取得目标字段的名字
			String name = capitaliz(fieldname);// 将字段名字首字母大写
			try {

				Class tarFieldType = field.getType();// 取得目标字段的声明类型
				String setMethodName = "set" + name;

				if (!src.containsKey(fieldname) || null == src.get(fieldname))
					continue;
				Object val = src.get(fieldname);
				
				if(val.equals(null))
					continue;
				
				String str = String.valueOf(val);
				if (tarFieldType == Date.class)
					val = DateUtils.parseDate(str, datePattern);
				// val = DateFormatUtil.toDate(str, "yyyy-MM-dd HH:mm");

				else if (tarFieldType == Double.class)
					val = NumberUtils.toDouble(str);

				else if (tarFieldType == Long.class)
					val = NumberUtils.toLong(str);

				else if (tarFieldType == Integer.class)
					val = NumberUtils.toInt(str);

				else if (tarFieldType == BigDecimal.class){
					val = NumberUtils.toDouble(str);
					val = new BigDecimal(String.valueOf(val));
				}else if(tarFieldType == Timestamp.class){
					Timestamp ts = new Timestamp(System.currentTimeMillis());
					val = Timestamp.valueOf(str);
				}

				Method setmerhod = tarClazz.getDeclaredMethod(setMethodName,
						tarFieldType);
				setmerhod.invoke(tar, val);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	public static Object createObject(String objstr) {

		Object o = null;
		if (null == objstr)
			return o;

		try {
			o = Class.forName(objstr).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return o;
	}

	public static void copyProperties(Object tar, Object src) {

		try {
			BeanUtils.copyProperties(tar, src);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将src对象方法中有值且在pMap中也存在的值提取出来，返回一个新的MAP
	 * 
	 * @param pMap
	 * @param src
	 * @return MAP
	
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, String> fillParams(Map<String, String> pMap,
			Object src) {
		Map<String, String> tarMap = new HashMap<String, String>();
		if (null == pMap || null == src)
			return tarMap;

		Class srcClazz = src.getClass();// 取得源class对象
		Field[] srcFields = srcClazz.getDeclaredFields();// 取得源字段数组

		for (Field field : srcFields) {
			String fieldname = field.getName();// 取得源字段的名字
			String name = capitaliz(fieldname);// 将字段名字首字母大写
			try {
				String getMethodName = "get" + name;
				if ("getSerialVersionUID".equals(getMethodName)
						|| !pMap.containsKey(fieldname))
					continue;

				Method merhod = srcClazz.getDeclaredMethod(getMethodName);
				Object val = merhod.invoke(src);
				if (null != val) {
					tarMap.put(pMap.get(fieldname), String.valueOf(val));
				}
			} catch (SecurityException e) {
				e.printStackTrace();

			} catch (IllegalArgumentException e) {
				e.printStackTrace();

			} catch (NoSuchMethodException e) {
				e.printStackTrace();

			} catch (IllegalAccessException e) {
				e.printStackTrace();

			} catch (InvocationTargetException e) {
				e.printStackTrace();

			}
		}

		return tarMap;
	}

	private static String capitaliz(String str) {

		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 提取当前类的方法名称 如：
	 * com.glodon.gjc.buyer.procedure.business.impl.ProcopenServiceImpl.
	 * triggerBidOpening
	 * 
	 * @param obj
	 * @return
	 */
	public static String getCurrentMethodName(Object obj) {
		String clazzname = obj.getClass().getName();
		String methodname = clazzname;
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for (StackTraceElement se : stack) {
			if (clazzname.equals(se.getClassName())) {
				methodname += "." + se.getMethodName();
				break;
			}
		}

		return methodname;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Object> getParameterMap(Map parameterMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String[]> properties = parameterMap;
		String value = "";
		for (Map.Entry<String, String[]> item : properties.entrySet()) {
			Object valueObj = item.getValue();
			if (null == valueObj)
				value = "";
			else if (valueObj instanceof String[]) {
				// value = StringUtil.join(",", (String[]) valueObj);
			} else {
				value = String.valueOf(valueObj);
			}
			result.put(item.getKey(), value);
		}
		return result;
	}

	/**
	 * 对MAP中的变量进行替换
	 * 
	 * @param vmap
	 *            有值的集合
	 * @param paras
	 *            有变量的集合
	 * @return 新的替换变量后的集合
	 */
	public static HashMap<String, Object> replaceVars(
			HashMap<String, Object> vmap, Map<String, Object> paras) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		for (Map.Entry<String, Object> m : paras.entrySet()) {
			String v = m.getValue() + "";
			if (v.indexOf("{") != -1) {// {$!usernamme}a
				String k = v.substring(v.indexOf("{") + 1, v.indexOf("}"));
				String _v = ""
						+ vmap.get(k.replace("$!", "").replace("$", "")
								.replace("user.", ""));
				params.put(m.getKey(), v.replace("{" + k + "}", _v));
			} else if (v.indexOf("$") != -1) {// a$!username
				String k = v.substring(v.indexOf("$"));
				String _v = ""
						+ vmap.get(k.replace("$!", "").replace("$", "")
								.replace("user.", ""));
				params.put(m.getKey(), _v);
			} else {
				params.put(m.getKey(), v);
			}
		}
		return params;
	}

}