package com.example.newswave

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var bannerAdView: AdView
    private var interstitialAd: InterstitialAd? = null
    private var nativeAd: NativeAd? = null
    private var rewardedAd: RewardedAd? = null
    private var appOpenAd: AppOpenAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        val newsRecyclerView = findViewById<RecyclerView>(R.id.newsRecyclerView)
        val newsList = generateDummyNews()
        val adapter = NewsAdapter(newsList)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        newsRecyclerView.adapter = adapter

        bannerAdView = findViewById(R.id.bannerAdView)
        val adRequest = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequest)

        loadInterstitialAd()
        loadNativeAd()
        loadRewardedAd()
        loadAppOpenAd()

        findViewById<Button>(R.id.btnShowBanner).setOnClickListener { showBannerAd() }
        findViewById<Button>(R.id.btnShowInterstitial).setOnClickListener { showInterstitialAd() }
        findViewById<Button>(R.id.btnShowNative).setOnClickListener { showNativeAd() }
        findViewById<Button>(R.id.btnShowRewarded).setOnClickListener { showRewardedAd() }
        findViewById<Button>(R.id.btnShowAppOpen).setOnClickListener { showAppOpenAd() }
    }

    private fun generateDummyNews(): List<NewsItem> {
        return listOf(
            NewsItem(getString(R.string.news_title_1), getString(R.string.news_content_1)),
            NewsItem(getString(R.string.news_title_2), getString(R.string.news_content_2)),
            NewsItem(getString(R.string.news_title_3), getString(R.string.news_content_3)),
            NewsItem(getString(R.string.news_title_4), getString(R.string.news_content_4)),
            NewsItem(getString(R.string.news_title_5), getString(R.string.news_content_5))
        )
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-5159036771303945/4173184618", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            })
    }

    private fun showInterstitialAd() {
        interstitialAd?.show(this) ?: loadInterstitialAd()
    }

    private fun loadNativeAd() {
        val builder = com.google.android.gms.ads.AdLoader.Builder(this, "ca-app-pub-5159036771303945/9221744585")
        builder.forNativeAd { ad: NativeAd ->
            nativeAd = ad
        }

        val adLoader = builder.build()
        adLoader.loadAd(AdRequest.Builder().build())
    }


    private fun showNativeAd() {

        android.widget.Toast.makeText(this, getString(R.string.native_ad_shown), android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this, "ca-app-pub-5159036771303945/2860102943", adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
            })
    }

    private fun showRewardedAd() {
        rewardedAd?.show(this) { reward ->

            android.widget.Toast.makeText(this, getString(R.string.reward_received), android.widget.Toast.LENGTH_SHORT).show()
        } ?: loadRewardedAd()
    }

    private fun loadAppOpenAd() {
        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(this, "ca-app-pub-5159036771303945/3562223791", adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                }
            })
    }

    private fun showAppOpenAd() {
        appOpenAd?.show(this) ?: loadAppOpenAd()
    }

    private fun showBannerAd() {
        bannerAdView.visibility = if (bannerAdView.visibility == android.view.View.VISIBLE) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == R.id.menu_privacy_policy) {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}