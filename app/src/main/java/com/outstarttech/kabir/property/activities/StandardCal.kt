package com.outstarttech.kabir.property.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.inventioncore.kabir.sopfia.BuildConfig
import com.inventioncore.kabir.sopfia.R
import com.simplemobiletools.calculator.extensions.config
import com.simplemobiletools.calculator.extensions.updateViewColors
import com.simplemobiletools.calculator.helpers.*
import com.simplemobiletools.commons.extensions.appLaunched
import com.simplemobiletools.commons.extensions.copyToClipboard
import com.simplemobiletools.commons.extensions.performHapticFeedback
import com.simplemobiletools.commons.extensions.value
import kotlinx.android.synthetic.main.activity_standard_cal.*
import me.grantland.widget.AutofitHelper


class StandardCal : SimpleActivity(), Calculator {
    private var storedTextColor = 0
    private var vibrateOnButtonPress = true

    lateinit var calc: CalculatorImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_standard_cal)
        appLaunched(BuildConfig.APPLICATION_ID)

        calc = CalculatorImpl(this, applicationContext)

        btn_plus.setOnClickListener { calc.handleOperation(PLUS); shakeItBaby(this);  checkHaptic(it) }
        btn_minus.setOnClickListener { calc.handleOperation(MINUS); shakeItBaby(this); checkHaptic(it) }
        btn_multiply.setOnClickListener { calc.handleOperation(MULTIPLY); shakeItBaby(this);  checkHaptic(it) }
        btn_divide.setOnClickListener { calc.handleOperation(DIVIDE); shakeItBaby(this);  checkHaptic(it) }
        btn_percent.setOnClickListener { calc.handleOperation(PERCENT); shakeItBaby(this);  checkHaptic(it) }
        btn_power.setOnClickListener { calc.handleOperation(POWER); shakeItBaby(this);  checkHaptic(it) }
        btn_root.setOnClickListener { calc.handleOperation(ROOT); shakeItBaby(this);  checkHaptic(it) }

        btn_clear.setOnClickListener { calc.handleClear(); shakeItBaby(this); checkHaptic(it) }
        btn_clear.setOnLongClickListener { calc.handleReset(); shakeItBaby(this);  true }

        getButtonIds().forEach {
            it.setOnClickListener { calc.numpadClicked(it.id);  shakeItBaby(this); checkHaptic(it) }
        }

        btn_equals.setOnClickListener { calc.handleEquals();  shakeItBaby(this); checkHaptic(it) }
        formula.setOnLongClickListener {  shakeItBaby(this); copyToClipboard(false) }
        result.setOnLongClickListener {  shakeItBaby(this); copyToClipboard(true) }

        AutofitHelper.create(result)
        AutofitHelper.create(formula)
        storeStateVariables()
        updateViewColors(calculator_holder, config.textColor)
//        checkWhatsNewDialog()
//        checkAppOnSDCard()
    }

    private fun shakeItBaby(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            (context.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            (context.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(30)
        }
    }

    override fun onResume() {
        super.onResume()
        if (storedTextColor != config.textColor) {
            updateViewColors(calculator_holder, config.textColor)
        }

        if (config.preventPhoneFromSleeping) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        vibrateOnButtonPress = config.vibrateOnButtonPress
    }

    override fun onPause() {
        super.onPause()
        storeStateVariables()
        if (config.preventPhoneFromSleeping) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu.findItem(R.id.scientific_calculator).isVisible = false // not implemented yet
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> launchScientific()
//            R.id.about -> launchSettings()
            R.id.scientific_calculator -> launchScientific()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun storeStateVariables() {
        config.apply {
            storedTextColor = textColor
        }
    }

    private fun checkHaptic(view: View) {
        if (vibrateOnButtonPress) {
            view.performHapticFeedback()
        }
    }

    private fun launchSettings() {
        startActivity(Intent(applicationContext, SettingsActivity::class.java))
    }

    private fun launchScientific() {
        startActivity(Intent(applicationContext, ScientificActivity::class.java))
    }

    private fun launchAbout() {
//        val licenses = LICENSE_AUTOFITTEXTVIEW or LICENSE_ROBOLECTRIC or LICENSE_ESPRESSO
//
//        val faqItems = arrayListOf(
//                FAQItem(R.string.faq_1_title, R.string.faq_1_text),
//                FAQItem(R.string.faq_1_title_commons, R.string.faq_1_text_commons),
//                FAQItem(R.string.faq_4_title_commons, R.string.faq_4_text_commons),
//                FAQItem(R.string.faq_2_title_commons, R.string.faq_2_text_commons),
//                FAQItem(R.string.faq_6_title_commons, R.string.faq_6_text_commons)
//        )
//
//        startAboutActivity(R.string.app_name, licenses, BuildConfig.VERSION_NAME, faqItems, true)
    }

    private fun getButtonIds() = arrayOf(btn_decimal, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9)

    private fun copyToClipboard(copyResult: Boolean): Boolean {
        var value = formula.value
        if (copyResult) {
            value = result.value
        }

        return if (value.isEmpty()) {
            false
        } else {
            copyToClipboard(value)
            true
        }
    }

    override fun setValue(value: String, context: Context) {
        result.text = value
    }

    private fun checkWhatsNewDialog() {
//        arrayListOf<Release>().apply {
//            add(Release(18, R.string.release_18))
//            add(Release(28, R.string.release_28))
//            checkWhatsNew(this, BuildConfig.VERSION_CODE)
//        }
    }

    // used only by Robolectric
    override fun setValueDouble(d: Double) {
        calc.setValue(Formatter.doubleToString(d))
        calc.lastKey = DIGIT
    }

    override fun setFormula(value: String, context: Context) {
        formula.text = value
    }
}
