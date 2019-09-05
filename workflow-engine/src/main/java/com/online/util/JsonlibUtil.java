package com.online.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/***
 * JSON 工具类 json-lib
 * 
 * @author ZJL
 * 
 */
public class JsonlibUtil {

	/***
	 * 获取json中 指定key值的json字符串，只能获取外层
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static String getString(String json, String key) {
		return json.indexOf(key) == -1 ? "{}" : JSONObject.fromObject(json)
				.getString(key);
	}

	/**
	 * 
	 * @param json
	 *            Json字符串
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getJsonArrayToList(String json, Class<T> clazz) {
		JSONArray jsonarray = JSONArray.fromObject(json);
		List<T> list = (List<T>) JSONArray.toCollection(jsonarray, clazz);
		return list;

	}

	/**
	 * 
	 * @param json
	 *            json字符串
	 * @param key
	 *            key值
	 * @return
	 */
	public static String getJsonString(String json, String key) {
		JSONObject result = JSONObject.fromObject(json);
		if (result.has(key)) {
			return result.getString(key);
		}
		for (Object table : result.keySet()) {
			if (result.get(table) instanceof JSONObject) {
				if (result.has(key)) {
					return result.getString(key);
				}

			} else if (result.get(table) instanceof JSONArray) {
				for (Object o : result.getJSONArray((String) table)) {

					if (o instanceof JSONObject) {
						// JSONObject object = new JSONObject();
						JSONObject object = JSONObject.fromObject(toString(o));
						// object.put(table, o);
						if (object.has(key)) {
							return object.get(key).toString();
						}

					}
				}

			}

		}
		return null;
	}

	/**
	 * 将json转化为object对象
	 * 
	 * @param json
	 * @return
	 * @author liuxin
	 */
	public static Object toBean(String json) {
		return JSONObject.toBean(JSONObject.fromObject(json));
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public static List toList(String json) {
		return JSONArray.toList(JSONArray.fromObject(json));
	}

	/***
	 * 将对象转换成字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		return JSONObject.fromObject(obj).toString().replaceAll("\"", "'");
	}

	/***
	 * 将List对象序列化为JSON文本
	 */
	public static <T> String toJSONString(List<T> list) {
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray.toString().replaceAll("\"", "'");
	}

	/***
	 * 将json 字符串转换成map
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> toMap(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 解析最外层的json
		JSONObject json = JSONObject.fromObject(jsonStr);
		Object value = null;
		for (Object key : json.keySet()) {
			value = json.get(key);
			// 如果内层是数组的话 ，继续解析
			if (value instanceof JSONArray) {
				JSONObject jsonObject = null;
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = JSONArray.fromObject(value)
						.iterator();
				while (it.hasNext()) {
					jsonObject = it.next();
					list.add(toMap(jsonObject.toString()));
				}
				map.put(key.toString(), list);
			} else {
				map.put(key.toString(), value);
			}
		}
		return map;
	}

	/**
	 * 将json转化为Map对象,只转化最外面
	 * 
	 * @param jsonString
	 *            json字符串
	 * @param datePattern
	 *            日期格式
	 * @return
	 * @author liuxin
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getMapFromJson(String jsonString, String[] datePattern) {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(datePattern));// 调用DateMorpherEx，defaultValue为null
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator keyIter = jsonObject.keys();

		Map valueMap = new HashMap();

		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			Object value = jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}

	/**
	 * 
	 * @param jsonString
	 * @return
	 * @author liuxin
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getMapFromJson(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator keyIter = jsonObject.keys();
		Map valueMap = new HashMap();
		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			Object value = jsonObject.get(key);
			valueMap.put(key, value);
		}
		return valueMap;
	}

	/**
	 * 将json转化为字符串数组
	 * 
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String[] getStringArrayFromJson(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); ++i) {
			stringArray[i] = jsonArray.getString(i);
		}

		return stringArray;
	}

	// public static void main(String[] args) {
	// String MapString="{\"name\":\"{type:'numberbox'}\"}";
	// String
	// MapString2="{\"columns\":[{\"width\":\"500\",\"title\":\"productid\",\"field\":\"productid\",\"rowspan\":\"\",\"colspan\":\"\",\"align\":\"center\",\"halign\":\"center\",\"sortable\":\"true\",\"order\":\"\",\"resizable\":\"false\",\"fixed\":\"true\",\"hidden\":\"false\",\"frozen\":\"true\",\"checkbox\":\"true\",\"formatter\":\"\",\"styler\":\"\",\"sorter\":\"\",\"editor\":\"\"},{\"width\":\"500\",\"title\":\"productname\",\"field\":\"productname\",\"rowspan\":\"\",\"colspan\":\"\",\"align\":\"center\",\"halign\":\"center\",\"sortable\":\"false\",\"order\":\"\",\"resizable\":\"false\",\"fixed\":\"false\",\"hidden\":\"false\",\"frozen\":\"false\",\"checkbox\":\"false\",\"formatter\":\"\",\"styler\":\"\",\"sorter\":\"\",\"editor\":\"\"},{\"width\":\"500\",\"title\":\"unitcost\",\"field\":\"unitcost\",\"rowspan\":\"\",\"colspan\":\"\",\"align\":\"center\",\"halign\":\"center\",\"sortable\":\"true\",\"order\":\"asc\",\"resizable\":\"false\",\"fixed\":\"false\",\"hidden\":\"false\",\"frozen\":\"false\",\"checkbox\":\"false\",\"formatter\":\"function(value,row,index){\\nreturn value;\\n}\",\"styler\":\"function(value,row,index){\\nreturn 'background-color:#ffee00;color:red;';\\n\\t\\t\\t}\",\"sorter\":\"\",\"editor\":\"{type:'numberbox'}\"}],\"tools\":[],\"booleans\":{\"fitColumns\":\"false\",\"autoRowHeight\":\"true\",\"striped\":\"false\",\"nowrap\":\"false\",\"pagination\":\"true\",\"rownumbers\":\"true\",\"singleSelect\":\"false\",\"ctrlSelect\":\"false\",\"checkOnSelect\":\"true\",\"selectOnCheck\":\"true\",\"multiSort\":\"false\",\"remoteSort\":\"false\",\"showHeader\":\"true\",\"showFooter\":\"false\",\"fit\":\"false\",\"border\":\"true\",\"doSize\":\"true\",\"noheader\":\"false\",\"collapsible\":\"true\",\"minimizable\":\"true\",\"maximizable\":\"true\",\"closable\":\"true\",\"collapsed\":\"false\",\"minimized\":\"false\",\"maximized\":\"false\",\"closed\":\"false\",\"cache\":\"true\"},\"strings\":{\"id\":\"itisid\",\"title\":\"grid的标题\",\"hrefaddr\":\"/lx/lllxxx.grid\",\"url\":\"/portal/test/datagrid_data.json\",\"method\":\"post\",\"resizeHandle\":\"right\",\"loadMsg\":\"Processing, please wait …\",\"pagePosition\":\"bottom\",\"pageList\":\"[10,20,30,40,50]\",\"queryParams\":\"{}\",\"sortOrder\":\"asc\"},\"ints\":{\"pageNumber\":\"1\",\"pageSize\":\"10\",\"scrollbarSize\":\"18\",\"width\":\"auto\",\"height\":\"auto\",\"openDuration\":\"400\",\"closeDuration\":\"400\"},\"event\":{\"onBeforeOpen\":\"function(){\\nalert('before open')\\n}\",\"onOpen\":\"function(){\\nalert('open')\\n}\",\"onBeforeClose\":\"function(){\\nalert('before close')\\n}\",\"onClose\":\"function(){\\nalert('close')\\n}\",\"onBeforeCollapse\":\"function(){\\nalert('befor collapse')\\n}\",\"onCollapse\":\"function(){\\nalert('collapse')\\n}\",\"onBeforeExpand\":\"function(){\\nalert('befor expand')\\n}\",\"onResize\":\"function(){\\nalert('resize')\\n}\",\"onMove\":\"function(){\\nalert('Move')\\n}\",\"onMaximize\":\"function(){\\nalert('Max')\\n}\",\"onRestore\":\"function(){\\nalert('restore')\\n}\",\"onMinimize\":\"function(){\\nalert('Min')\\n}\",\"onLoadSuccess\":\"function(){\\nalert('load success')\\n}\",\"onBeforeLoad\":\"function(){\\nalert('before load')\\n}\",\"onDblClickRow\":\"function(index, field, value){\\n$(this).datagrid('beginEdit',index)\\n}\",\"onDblClickCell\":\"function(){\\nalert('cell db click')\\n}\",\"onBeforeSortColumn\":\"function(){\\nalert('befor sort ')\\n}\",\"onSortColumn\":\"function(){\\nalert(' sort ')\\n}\",\"onResizeColumn\":\"function(){\\nalert('resize column')\\n}\",\"onBeforeSelect\":\"function(){\\nalert('befor select ')\\n}\",\"onSelect\":\"function(){\\nalert('select ')\\n}\",\"onBeforeUnselect\":\"function(){\\nalert('befor unselect ')\\n}\",\"onUnselect\":\"function(){\\nalert(' unselect ')\\n}\",\"onSelectAll\":\"function(){\\nalert(' selectAll ')\\n}\",\"onUnselectAll\":\"function(){\\nalert(' unselectAll ')\\n}\",\"onBeforeCheck\":\"function(){\\nalert(' before check')\\n}\",\"onCheck\":\"function(){\\nalert('check')\\n}\",\"onBeforeUncheck\":\"function(){\\nalert(' before uncheck')\\n}\",\"onUncheck\":\"function(){\\nalert('uncheck')\\n}\",\"onCheckAll\":\"function(){\\nalert('checkAll')\\n}\",\"onUncheckAll\":\"function(){\\nalert('uncheckAll')\\n}\",\"onBeforeEdit\":\"function(){\\nalert('before edit')\\n}\",\"onBeginEdit\":\"function(){\\nalert('begin edit')\\n}\",\"onEndEdit\":\"function(){\\nalert('end edit')\\n}\",\"onAfterEdit\":\"function(){\\nalert('after edit')\\n}\",\"onCancelEdit\":\"function(){\\nalert('cancel edit')\\n}\",\"onHeaderContextMenu\":\"function(e, field){\\n$(this).datagrid('cancelEdit',2);\\n}\",\"onRowContextMenu\":\"function(e, index, row){\\n$(this).datagrid('endEdit',index);\\n}\"},\"html\":\"\",\"searchhtml\":\"\",\"formtools\":[],\"id\":\"itisid\",\"title\":\"grid的标题\",\"hrefaddr\":\"/lx/lllxxx.grid\"}";
	// System.out.println("--------------------------以下的为jsonlib的解析------------------------------------");
	// //System.out.println(MapString);
	// JsonConfig config=new JsonConfig();
	// Map<String,Object> map = JsonlibUtil.getMap4Json(MapString2);
	// System.out.println(map);
	// System.out.println("--------------------------以下的为jackson的解析------------------------------------");
	// try {
	// Map<String, Object> map2 = JacksonUtil.json2map(MapString2);
	// System.out.println(map2);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
