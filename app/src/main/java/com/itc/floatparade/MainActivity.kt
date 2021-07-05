package com.itc.floatparade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itc.floatparade.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.map.setMapImage(R.mipmap.map)

        binding.tvAddSign.setOnClickListener {
            val list = mutableListOf<SignBean>()
            list.add(SignBean("美食城", "正常", 0.2f, 0.4f))
            list.add(SignBean("恐龙危机", "正常", 0.5f, 0.5f))
            list.add(SignBean("海盗船", "正常", 0.7f, 0.6f))
            list.add(SignBean("魔法城堡", "正常", 0.4f, 0.8f))
            binding.map.setSignData(list)
        }
        binding.btnGetSign.setOnClickListener {
            val list = binding.map.getMoveSignData()
            binding.tvSignList.text = list.toString()
        }

        binding.btnRoute.setOnClickListener {
            binding.map.createRoute()
        }
    }

}