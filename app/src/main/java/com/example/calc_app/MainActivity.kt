package com.example.calc_app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import com.example.calc_app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var settings: Any? = -1

    private val resultLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                settings = data?.getStringExtra("resultKey")
            }
        }

    private lateinit var binding: ActivityMainBinding

    private var isDarkTheme = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.ACButton.setOnClickListener { setTextField("AC") }
        binding.BackButton.setOnClickListener { setTextField("BackOne") }
        binding.NullButton.setOnClickListener { setTextField("0") }
        binding.OneButton.setOnClickListener { setTextField("1") }
        binding.TwoButton.setOnClickListener { setTextField("2") }
        binding.ThreeButton.setOnClickListener { setTextField("3") }
        binding.FourButton.setOnClickListener { setTextField("4") }
        binding.FiveButton.setOnClickListener { setTextField("5") }
        binding.SixButton.setOnClickListener { setTextField("6") }
        binding.SevenButton.setOnClickListener { setTextField("7") }
        binding.EightButton.setOnClickListener { setTextField("8") }
        binding.NineButton.setOnClickListener { setTextField("9") }
        binding.DotButton.setOnClickListener { setTextField(".") }
        binding.PlusButton.setOnClickListener { setTextField(" + ") }
        binding.MinusButton.setOnClickListener { setTextField(" - ") }
        binding.DivideButton.setOnClickListener { setTextField(" / ") }
        binding.MultiplyButton.setOnClickListener { setTextField(" * ") }
        binding.PercentButton.setOnClickListener { setTextField(" % ") }
        binding.EqualButton.setOnClickListener { calculationResult(binding.operationField.text.toString()) }
        binding.BackButton.setOnClickListener { Log.d("TAG", "Текущая настройка: $settings") }
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            isDarkTheme = isChecked
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        binding.settingsButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("isdarkTheme", isDarkTheme)
            resultLauncher.launch(intent)
        }
    }



    private fun setTextField(str: String){
        val operationField = binding.operationField
        operationField.text = when (str){
            "AC"  -> ""
            "BackOne" -> operationField.text.toString().substring(0, binding.operationField.text.toString().length - 1)
            in listOf(" + ", " - ", " / ", " * ", " % ", ".") -> {
                val currentString = binding.operationField.text.toString()
                var resultOp = ""
                if (currentString == "") resultOp = "0$str"

                else if (currentString.length >= 3 && currentString.substring(currentString.length - 3, currentString.length) in listOf(" + ", " - ", " / ", " * ", " % ", "."))
                    resultOp = currentString.substring(0, currentString.length - 3) + str

                else resultOp = (binding.operationField.text.toString() + str)

                resultOp
            }
            else -> (binding.operationField.text.toString() + str)
        }
    }

    private fun calculationResult(str: String){
        var membersList = str.split(" ")
        if (membersList[membersList.size - 1] == "" && membersList[membersList.size - 2] in listOf("+", "-", "/", "*", "%", ".")) {
            membersList = membersList.subList(0, membersList.size - 2)
            binding.operationField.text = str.substring(0, str.length-3)
        }
        var result = membersList[0].toFloat()
        for (memberIndex in 1 until membersList.size step 2) {
            result = when (membersList[memberIndex]) {
                "+" -> result + membersList[memberIndex + 1].toFloat()
                "-" -> result - membersList[memberIndex + 1].toFloat()
                "*" -> result * membersList[memberIndex + 1].toFloat()
                "/" -> result / membersList[memberIndex + 1].toFloat()
                "%" -> result / membersList[memberIndex + 1].toFloat() * 100
                else -> result + 0
            }
        }
        binding.rersultText.text = result.toString()
    }

//    fun settingBuild(settings: String?): List<Any> {
//        val settingsList = settings.toList()
//    }

}

