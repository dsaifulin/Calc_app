package com.example.calc_app

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import com.example.calc_app.databinding.ActivitySettingsBinding
import java.lang.Exception


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val resList: MutableList<Any?> = mutableListOf(null, null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinnerAdapterLang: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_data_en,
            android.R.layout.simple_spinner_item
        )
        val spinnerAdapterDiv: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.divisions_mods,
            android.R.layout.simple_spinner_item
        )

        spinnerAdapterLang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAdapterDiv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.languages.adapter = spinnerAdapterLang
        binding.divMode.adapter = spinnerAdapterDiv

        if (intent.getBooleanExtra("isDarkTheme", false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val context = applicationContext
        binding.languages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedValue: String = parent?.getItemAtPosition(position).toString()
                if (selectedValue == "Russian") {
                    binding.textView2.text = context.getString(R.string.settings_ru)
                    binding.textView6.text = context.getString(R.string.lang_ru)
                    binding.textView4.text = context.getString(R.string.settings_ru)
                    binding.textView5.text = context.getString(R.string.div_ru)
                    binding.buttonSave.text = context.getString(R.string.button_ru)
                    Log.d("tag", "Выбран Русский")
                } else {
                    binding.textView2.text = context.getString(R.string.settings_en)
                    binding.textView6.text = context.getString(R.string.lang_en)
                    binding.textView4.text = context.getString(R.string.settings_en)
                    binding.textView5.text = context.getString(R.string.div_en)
                    binding.buttonSave.text = context.getString(R.string.button_en)
                    Log.d("tag", "Выбран Английский")
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.textView2.text = context.getString(R.string.settings_en)
                binding.textView6.text = context.getString(R.string.lang_en)
                binding.textView4.text = context.getString(R.string.settings_en)
                binding.textView5.text = context.getString(R.string.div_en)
                binding.buttonSave.text = context.getString(R.string.button_en)
            }
        }

        binding.divMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedValue: String = parent?.getItemAtPosition(position).toString()
                val context = applicationContext
                resList[1] =  selectedValue
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //Nothing
            }
        }

        binding.buttonSave.setOnClickListener {
            try {
                val curAccur = binding.editAccuracy.text.toString().toInt()
                if (curAccur in 0..5) {
                    resList[0] = curAccur
                } else errorShow(this, "invalid value")
            } catch (e: Exception) {
                errorShow(this, "invalid value")
            }
        }

        binding.closeButton.setOnClickListener{
           val resultIntent = Intent()
            val result = if (null !in resList) resList.toString() else -1
           resultIntent.putExtra("resultKey", result)
           setResult(Activity.RESULT_OK, resultIntent)
           finish()
        }

    }
}

fun errorShow(context: Context, message: String) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Ошибка")
        .setMessage("Вниание: ${message}")
        .setPositiveButton("ОК") { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}