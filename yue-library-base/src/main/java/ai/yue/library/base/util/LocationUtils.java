package ai.yue.library.base.util;

import java.util.ArrayList;
import java.util.List;

import ai.yue.library.base.ipo.LocationIPO;

/**
 * @author  孙金川
 * @version 创建时间：2018年8月1日
 */
public class LocationUtils {

	private static final double EARTH_RADIUS = 6378.137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 通过经纬度获取距离(单位：米)
	 * <p>
	 * 说明（如：高德地图，重庆市政府坐标）<br>
	 * <code>106.550464,29.563761</code><br>
	 * 106.550464 经度<br>
	 * 29.563761 纬度<br>
	 * 注：lng 经度<br>
	 * 注：lat 纬度
	 * @param location1
	 * @param location2
	 * @return 距离
	 */
	public static double getDistance(LocationIPO location1, LocationIPO location2) {
		double lng1 = location1.getLng();
		double lat1 = location1.getLat();
		double lng2 = location2.getLng();
		double lat2 = location2.getLat();

		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000d) / 10000d;
		s = s * 1000;
		return s;
	}
	
	/**
	 * 通过经纬度获取距离(单位：米)
	 * @param location
	 * @param locations
	 * @return 距离数组
	 */
	public static List<Double> getDistance(LocationIPO location, List<LocationIPO> locations) {
		List<Double> list = new ArrayList<>();
		for (LocationIPO locationIPO : locations) {
			list.add(getDistance(location, locationIPO));
		}
		
		return list;
	}
	
	/**
	 * 获得距离当前位置最近的经纬度
	 * <p>
	 * 返回locations数组中最小值的下标
	 * @param location
	 * @param locations
	 * @return minIndex
	 */
	public static int getNearestLngAndLat(LocationIPO locationIPO, List<LocationIPO> locations) {
		int minIndex = 0;
		
		var list = getDistance(locationIPO, locations);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) < list.get(minIndex)) {
				minIndex = i;
			}
		}
		
		return minIndex;
	}
	
}
