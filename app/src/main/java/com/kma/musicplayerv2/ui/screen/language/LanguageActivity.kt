package com.kma.musicplayerv2.ui.screen.language

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivityLanguageBinding
import com.kma.musicplayerv2.model.LanguageModel
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.ui.screen.main.MainActivity
import com.kma.musicplayerv2.utils.SystemUtil

class LanguageActivity : BaseActivity<ActivityLanguageBinding>(), IClickLanguage {

    private var adapter: LanguageAdapter? = null
    private var model: LanguageModel? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun getContentView(): Int = R.layout.activity_language

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE)
        adapter =
            LanguageAdapter(
                this,
                setLanguageDefault(),
                this
            )
        binding.rclLanguage.adapter = adapter
    }

    override fun onClick(data: LanguageModel) {
        adapter?.setSelectLanguage(data)
        model = data
        ivDone()
    }

    private fun setLanguageDefault(): List<LanguageModel> {
        val lists: MutableList<LanguageModel> = ArrayList()
        lists.add(LanguageModel("Vietnamese", "vi", false, R.drawable.ic_vietnam_flag))
        lists.add(LanguageModel("English", "en", false, R.drawable.ic_english_flag))
        lists.add(LanguageModel("Hindi", "hi", false, R.drawable.ic_hindi_flag))
        lists.add(LanguageModel("Spanish", "es", false, R.drawable.ic_span_flag))
        lists.add(LanguageModel("French", "fr", false, R.drawable.ic_french_flag))
        lists.add(LanguageModel("German", "de", false, R.drawable.ic_german_flag))
        lists.add(LanguageModel("Indonesian", "in", false, R.drawable.ic_indo_flag))
        lists.add(LanguageModel("Portuguese", "pt", false, R.drawable.ic_portuguese_flag))

        val key: String = SystemUtil.getPreLanguage(this)
        val lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0].language
        } else {
            Resources.getSystem().configuration.locale.language
        }
        var count = 0
        for (model: LanguageModel in lists) {
            if (model.isoLanguage == lang) {
                count++
            }
        }
        if (count == 0) {
            model = LanguageModel("Vietnamese", "vi", false, R.drawable.ic_vietnam_flag)
            adapter?.setSelectLanguage(model)
        }
        for (model: LanguageModel in lists) {
            if (model.isoLanguage == SystemUtil.getPreLanguage(this)) {
                this.model = model
            }
        }
        Log.e("", "setLanguageDefault: $key")
        for (i in lists.indices) {
            if (!sharedPreferences!!.getBoolean("nativeLanguage", false)) {
                if (key == lists[i].isoLanguage) {
                    val data = lists[i]
                    data.isCheck = true
                    lists.remove(lists[i])
                    lists.add(0, data)
                    break
                }
            } else {
                if (key == lists[i].isoLanguage) {
                    lists[i].isCheck = true
                }
            }
        }

        return lists
    }

    fun ivDone() {
        if (model != null) {
            SystemUtil.setPreLanguage(this@LanguageActivity, model?.isoLanguage)
            SystemUtil.setLocale(this)
            startNextAct()
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.you_need_to_select_language),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startNextAct() {
        showActivity(MainActivity::class.java, null)
        finish()
    }
}