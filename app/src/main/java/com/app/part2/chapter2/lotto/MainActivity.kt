package com.app.part2.chapter2.lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.app.part2.chapter2.lotto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // viewBinding
    private var didRun = false // 숫자 리스트가 다 채워졌는지 검사
    private val pickNumberSet = hashSetOf<Int>() // 선택한 숫자를 담는 변수
    private val numTextViewList: List<TextView> by lazy { // 숫자를 담을 textView 6개 리스트
        listOf<TextView>(
            findViewById<TextView>(R.id.num1),
            findViewById<TextView>(R.id.num2),
            findViewById<TextView>(R.id.num3),
            findViewById<TextView>(R.id.num4),
            findViewById<TextView>(R.id.num5),
            findViewById<TextView>(R.id.num6)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 45 // numberPicker의 범위 1~45

        initRunButton() // 자동생성버튼
        initAddButton() // 번호추가하기
        initClearButton() // 초기화버튼
        setContentView(view) // Binding된 view 뿌려주기
    }

    private fun initRunButton() { // 6개의 랜덤숫자를 list로 받고 textView로 뿌려줌
        binding.runButton.setOnClickListener {
            val list = getRandomNumber()

            didRun = true // 초기화 예외처리

            // index에 맞게 각각의 textView에 값 할당 후 visible하게 함
            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true
            }
        }
    }

    private fun initAddButton() { // 초기화, 번호이탈, 선택된거 예외처리하고 선택한 숫자 추가
        binding.addButton.setOnClickListener {

            if(didRun) {
                Toast.makeText(this,"초기화를 해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 꽉찼을경우 clickListener 자체를 리턴
            }
            if(pickNumberSet.size >= 6) {
                Toast.makeText(this, "번호는 6개까지만 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pickNumberSet.contains(binding.numberPicker.value)) {
                Toast.makeText(this,"이미 선택한 번호입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val textView = numTextViewList [pickNumberSet.size]
            textView.isVisible = true
            textView.text = binding.numberPicker.value.toString()

            setBackground(binding.numberPicker.value,textView)

            pickNumberSet.add(binding.numberPicker.value)
        }
    }
    private fun setBackground(number:Int, textView: TextView) {
        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
        }
    }
    private fun initClearButton() { // 선택된 숫자들 초기화하고 안 보이게 visible false 로 & didRun false로 바꿈
        binding.clearButton.setOnClickListener {
            pickNumberSet.clear()
            numTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }
    private fun getRandomNumber(): List<Int> { // 6개중에 선택 안된 나머지 숫자 랜덤하게 추가해서 정렬 후 list로 반함
        val numberList = mutableListOf<Int>().apply {
            for(i in 1..45) { // 선택된 숫자 제외하고 numberList에 add 하고 shuffle
                if(pickNumberSet.contains(i)) {
                    continue
                }
                this.add(i)
            }
            this.shuffle()
        }
        // 선택된 숫자들 + 랜덤하게 뽑힌 숫자들 list로 합치고 정렬해서 리턴
        return (pickNumberSet.toList() + numberList.subList(0,6 - pickNumberSet.size)).sorted()
    }
}