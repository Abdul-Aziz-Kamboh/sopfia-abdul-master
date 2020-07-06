package com.outstarttech.kabir.property.activities

import android.os.Bundle
import com.inventioncore.kabir.sopfia.BuildConfig
import com.inventioncore.kabir.sopfia.R
import com.simplemobiletools.calculator.extensions.config
import com.simplemobiletools.calculator.extensions.updateViewColors
import com.simplemobiletools.commons.extensions.appLaunched
import kotlinx.android.synthetic.main.activity_standard_cal.*

class ScientificActivity : SimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scientific)
        appLaunched(BuildConfig.APPLICATION_ID)
        updateViewColors(calculator_holder, config.textColor)
    }
}
