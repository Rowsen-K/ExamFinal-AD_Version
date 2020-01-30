package com.rowsen.mytools;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;
import com.rowsen.examfinal.Myapp;
import com.xiaomi.ad.common.pojo.AdType;

import static android.view.View.GONE;


public class BannerAD {
    Myapp app = Myapp.getInstance();
/*    //穿山甲
    TTAdNative mTTAdNative;
    TTNativeExpressAd mTTAd;*/

    //GDT老版banner
    BannerView bv;
    //GDT -banner 2.0
    UnifiedBannerView banner;

    //Mi
    IAdWorker mBannerAd;

    public BannerAD() {
    }

    public BannerAD(final Context context, String GDT_posId, final ViewGroup GDT_banner, final String Mi_posId, final ViewGroup Mi_banner) {
        banner = new UnifiedBannerView((Activity) context, Myapp.GDT_APPID, GDT_posId, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + adError.getErrorCode());
                banner_Mi(context, GDT_banner, Mi_posId, Mi_banner);
            }

            @Override
            public void onADReceive() {
                Log.i("AD_DEMO", "ONBannerReceive");
                Mi_banner.setVisibility(GONE);
                GDT_banner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onADExposure() {

            }

            @Override
            public void onADClosed() {
                banner_Mi(context, GDT_banner, Mi_posId, Mi_banner);
            }

            @Override
            public void onADClicked() {
            }

            @Override
            public void onADLeftApplication() {

            }

            @Override
            public void onADOpenOverlay() {

            }

            @Override
            public void onADCloseOverlay() {

            }
        });
        try {
            mBannerAd = AdWorkerFactory.getAdWorker(context, Mi_banner, new MimoAdListener() {
                @Override
                public void onAdPresent() {
                    //Log.e("展示", "onAdPresent");
                    GDT_banner.setVisibility(GONE);
                    Mi_banner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdClick() {
                    //Log.e("点击", "onAdClick");
                }

                @Override
                public void onAdDismissed() {
                    banner_GDT2(context, GDT_banner);
                }

                @Override
                public void onAdFailed(String s) {
                    //Log.e("失败", s);
                    banner_GDT2(context, GDT_banner);
                }

                @Override
                public void onAdLoaded(int size) {
                    GDT_banner.setVisibility(GONE);
                    Mi_banner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onStimulateSuccess() {
                }
            }, AdType.AD_BANNER);
        } catch (Exception e) {
            e.printStackTrace();
            banner_GDT2(context, GDT_banner);
        }
    }

    //显示穿山甲广告
  /*  public void banner_TT(final Context context, String TT_posId, final ViewGroup TT_banner, final String GDT_posId, final ViewGroup GDT_banner,final String Mi_posId, final ViewGroup Mi_banner) {
        mTTAdNative = app.ttAdManager.createAdNative(context);
        TT_banner.removeAllViews();
        float expressViewWidth = 350;
        float expressViewHeight = 350;
        try{
            expressViewWidth = context.getResources().getDisplayMetrics().widthPixels;
            expressViewHeight = 60;
        }catch (Exception e){
            expressViewHeight = 0; //高度设置为0,则高度会自适应
        }
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(TT_posId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(3) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth,expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640,320 )//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e("load error : " , code + ", " + message);
                TT_banner.removeAllViews();
                banner_GDT(context,GDT_posId, TT_banner, GDT_banner, Mi_posId, Mi_banner);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0){
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30*1000);
                bindAdListener(context,mTTAd,TT_banner,GDT_posId,GDT_banner,Mi_posId,Mi_banner);
                mTTAd.render();
            }
        });
    }

    private void bindAdListener(final Context context, TTNativeExpressAd ad, final ViewGroup TT_banner, final String GDT_posId, final ViewGroup GDT_banner, final String Mi_posId, final ViewGroup Mi_banner) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {

            }

            @Override
            public void onAdShow(View view, int type) {

            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView","render fail:");
                TT_banner.removeAllViews();
                banner_GDT(context,GDT_posId,TT_banner,GDT_banner,Mi_posId,Mi_banner);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //Log.e("ExpressView","render suc:");
                TT_banner.removeAllViews();
                TT_banner.addView(view);
            }
        });
    }
*/
    //显示GDT老版横幅广告
    public void banner_GDT(final Context context, String GDT_posId, final ViewGroup GDT_banner, final String Mi_posId, final ViewGroup Mi_banner) {
        bv = new BannerView((Activity) context, ADSize.BANNER, app.GDT_APPID, GDT_posId);
        // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
        // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
        bv.setRefresh(30);
        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onADClicked() {
                super.onADClicked();
            }

            @Override
            public void onADClosed() {
                super.onADClosed();
            }

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("Banner onNoAD，eCode = %d, eMsg = %s", error.getErrorCode(),
                                error.getErrorMsg()));
                banner_Mi(context, GDT_banner, Mi_posId, Mi_banner);
            }

            @Override
            public void onADReceiv() {
                //TT_banner.setVisibility(GONE);
                Mi_banner.setVisibility(GONE);
                GDT_banner.setVisibility(View.VISIBLE);
                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        GDT_banner.addView(bv);
        bv.loadAD();
    }

    //GDT—banner2.0
    public void banner_GDT2(final Context context, final ViewGroup GDT_banner) {
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(30);
        GDT_banner.removeAllViews();
        GDT_banner.addView(banner, getUnifiedBannerLayoutParams(context));
        /* 发起广告请求，收到广告数据后会展示数据     */
        banner.loadAD();
    }

    private FrameLayout.LayoutParams getUnifiedBannerLayoutParams(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        return new FrameLayout.LayoutParams(width, Math.round(width / 6.4F));
    }

    //显示Mi横幅广告
    public void banner_Mi(final Context context, final ViewGroup GDT_banner, String Mi_posId, final ViewGroup Mi_banner) {
        try {
            if (mBannerAd == null)
                mBannerAd = AdWorkerFactory.getAdWorker(context, Mi_banner, new MimoAdListener() {
                    @Override
                    public void onAdPresent() {
                        //Log.e("展示", "onAdPresent");
                        GDT_banner.setVisibility(GONE);
                        Mi_banner.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdClick() {
                        //Log.e("点击", "onAdClick");
                    }

                    @Override
                    public void onAdDismissed() {
                        banner_GDT2(context, GDT_banner);
                    }

                    @Override
                    public void onAdFailed(String s) {
                        //Log.e("失败", s);
                        banner_GDT2(context, GDT_banner);
                    }

                    @Override
                    public void onAdLoaded(int size) {
                        GDT_banner.setVisibility(GONE);
                        Mi_banner.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onStimulateSuccess() {
                    }
                }, AdType.AD_BANNER);
            mBannerAd.loadAndShow(Mi_posId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
