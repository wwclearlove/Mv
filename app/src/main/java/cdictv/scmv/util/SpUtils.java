package cdictv.scmv.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cdictv.App;

public class SpUtils {
    private static SharedPreferences sp;
    private static SharedPreferences getSp() {
        if (sp == null) {
            sp = PreferenceManager.getDefaultSharedPreferences(App.instance);
        }
        return sp;
    }

    /**
     * 保存int值
     *

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {

        if (null == datalist || datalist.size() <= 0)
        {
            getSp().edit().remove(tag).apply();
            return;
        }


        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);

        getSp().edit().putString(tag, strJson).apply();
    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public static  <T> List<T> getDataList(String tag,Class<T[]> clazz) {
        List<T> datalist=new ArrayList<>();
        String strJson = getSp().getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        T[] arr = gson.fromJson(strJson, clazz);
        List arrList = new ArrayList(Arrays.asList(arr));
        return arrList;

    }





}
