package com.moge.gege.ui.helper;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.moge.gege.config.Constants;
import com.moge.gege.model.*;
import com.moge.gege.util.EnviromentUtil;
import com.moge.gege.util.FileUtil;

import java.io.File;
import java.util.List;

public class DataCacheHelper
{
    private static Gson mGson = new Gson();
    private static String mFileNameSuffix;

    static
    {
        new DataCacheHelper();
    }

    private DataCacheHelper()
    {
        if (Constants.config == Constants.Config.RELEASE)
        {
            mFileNameSuffix = "_release";
        }
        else
        {
            mFileNameSuffix = "_debug";
        }
    }

    private static String getFeedsFileName(Context context)
    {

        return EnviromentUtil.getCacheDirectory(context) + File.separator
                + "feeds" + mFileNameSuffix;
    }

    public static boolean saveFeedsData(Context context,
            TopicListModel feeds)
    {
        if (feeds == null)
        {
            return false;
        }

        String jsonData = mGson.toJson(feeds);
        return FileUtil.writeFile(jsonData.getBytes(),
                getFeedsFileName(context));
    }

    public static TopicListModel loadFeedsData(Context context)
    {
        String jsonData = FileUtil.readFile(context,
                getFeedsFileName(context));
        if (TextUtils.isEmpty(jsonData))
        {
            return null;
        }

        try
        {
            return mGson.fromJson(jsonData, TopicListModel.class);
        }
        catch (JsonSyntaxException e)
        {
            return null;
        }
    }

    private static String getIndexRecommendPlaceFileName(Context context)
    {
        return EnviromentUtil.getCacheDirectory(context) + File.separator
                + "index_recommend" + mFileNameSuffix;
    }

    public static boolean saveIndexRecommendPlaceData(Context context,
            IndexRecommendPlaceModel recommendPlaceData)
    {
        if (recommendPlaceData == null)
        {
            return false;
        }

        String jsonData = mGson.toJson(recommendPlaceData);
        return FileUtil.writeFile(jsonData.getBytes(),
                getIndexRecommendPlaceFileName(context));
    }

    public static IndexRecommendPlaceModel loadIndexRecommendPlaceData(
            Context context)
    {
        String jsonData = FileUtil.readFile(context,
                getIndexRecommendPlaceFileName(context));
        if (TextUtils.isEmpty(jsonData))
        {
            return null;
        }

        try
        {
            return mGson.fromJson(jsonData, IndexRecommendPlaceModel.class);
        }
        catch (JsonSyntaxException e)
        {
            return null;
        }
    }

    private static String getMyBoardFileName(Context context, String userid)
    {
        return EnviromentUtil.getCacheDirectory(context) + File.separator
                + userid + "_myboards" + mFileNameSuffix;
    }

    public static boolean saveMyBoardListData(Context context, String userid,
            MyBoardListModel myBoardListData)
    {
        if (myBoardListData == null)
        {
            return false;
        }

        String jsonData = mGson.toJson(myBoardListData);
        return FileUtil.writeFile(jsonData.getBytes(),
                getMyBoardFileName(context, userid));
    }

    public static MyBoardListModel loadMyBoardListData(Context context,
            String userid)
    {
        String jsonData = FileUtil.readFile(context,
                getMyBoardFileName(context, userid));
        if (TextUtils.isEmpty(jsonData))
        {
            return null;
        }

        try
        {
            return mGson.fromJson(jsonData, MyBoardListModel.class);
        }
        catch (JsonSyntaxException e)
        {
            return null;
        }
    }

    private static String getNearBoardFileName(Context context, String userid)
    {
        return EnviromentUtil.getCacheDirectory(context) + File.separator
                + userid + "_nearboards" + mFileNameSuffix;
    }

    public static boolean saveNearBoardListData(Context context, String userid,
            BoardListModel nearBoardListData)
    {
        if (nearBoardListData == null)
        {
            return false;
        }

        String jsonData = mGson.toJson(nearBoardListData);
        return FileUtil.writeFile(jsonData.getBytes(),
                getNearBoardFileName(context, userid));
    }

    public static BoardListModel loadNearBoardListData(Context context,
            String userid)
    {
        String jsonData = FileUtil.readFile(context,
                getNearBoardFileName(context, userid));
        if (TextUtils.isEmpty(jsonData))
        {
            return null;
        }

        try
        {
            return mGson.fromJson(jsonData, BoardListModel.class);
        }
        catch (JsonSyntaxException e)
        {
            return null;
        }
    }

    private static String getTradingsFileName(Context context)
    {
        return EnviromentUtil.getCacheDirectory(context) + File.separator
                + "tradings" + mFileNameSuffix;
    }

    public static boolean saveTradingListData(Context context,
            TradingListModel tradingListData)
    {
        if (tradingListData == null)
        {
            return false;
        }

        String jsonData = mGson.toJson(tradingListData);
        return FileUtil.writeFile(jsonData.getBytes(),
                getTradingsFileName(context));
    }

    public static TradingListModel loadTradingListData(Context context)
    {
        String jsonData = FileUtil.readFile(context,
                getTradingsFileName(context));
        if (TextUtils.isEmpty(jsonData))
        {
            return null;
        }

        try
        {
            return mGson.fromJson(jsonData, TradingListModel.class);
        }
        catch (JsonSyntaxException e)
        {
            return null;
        }
    }

    private static String getTradingPromotionFileName(Context context)
    {
        return EnviromentUtil.getCacheDirectory(context) + File.separator
                + "tradings_promotion" + mFileNameSuffix;
    }

    public static boolean saveTradingPromotionListData(Context context,
            TradingPromotionListModel tradingPromotionListData)
    {
        if (tradingPromotionListData == null)
        {
            return false;
        }

        String jsonData = mGson.toJson(tradingPromotionListData);
        return FileUtil.writeFile(jsonData.getBytes(),
                getTradingPromotionFileName(context));
    }

    public static TradingPromotionListModel loadTradingPromotionListData(
            Context context)
    {
        String jsonData = FileUtil.readFile(context,
                getTradingPromotionFileName(context));
        if (TextUtils.isEmpty(jsonData))
        {
            return null;
        }

        try
        {
            return mGson.fromJson(jsonData, TradingPromotionListModel.class);
        }
        catch (JsonSyntaxException e)
        {
            return null;
        }
    }


    private static String getTradingCategoryFileName(Context context)
    {
        return EnviromentUtil.getCacheDirectory(context) + File.separator
                + "tradings_category" + mFileNameSuffix;
    }

    public static boolean saveTradingCategoryListData(Context context,
                                                      CategoryListModel tradingCategory)
    {
        if (tradingCategory == null)
        {
            return false;
        }

        String jsonData = mGson.toJson(tradingCategory);
        return FileUtil.writeFile(jsonData.getBytes(),
                getTradingCategoryFileName(context));
    }

    public static CategoryListModel loadTradingCategoryListData(
            Context context)
    {
        String jsonData = FileUtil.readFile(context,
                getTradingCategoryFileName(context));
        if (TextUtils.isEmpty(jsonData))
        {
            return null;
        }

        try
        {
            return mGson.fromJson(jsonData, CategoryListModel.class);
        }
        catch (JsonSyntaxException e)
        {
            return null;
        }
    }

}
