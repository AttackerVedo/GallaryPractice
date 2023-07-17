package com.attackervedo.gallarypractice

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.attackervedo.gallarypractice.databinding.ActivityFrameBinding
import com.google.android.material.tabs.TabLayoutMediator

class FrameActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFrameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 툴바 커스텀마이징
        binding.toolBar.apply {
            title = "나만의 앨범"
            setSupportActionBar(this)
        }
        // 툴바에 뒤로가기 버튼 속성 부여(버튼동작 구현은 아직임 그냥 UI만 보여지게 속성 부여
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val images = (intent.getStringArrayExtra("images") ?: emptyArray()).map { uriString -> FrameItem(Uri.parse(uriString)) }
        val frameAdapter = FrameAdapter(images)

        binding.viewPager.adapter = frameAdapter

        // 탭레이아웃 연결
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ){
            tab, position ->
            binding.viewPager.currentItem = tab.position
        }.attach()
    }

    //뒤로가기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            //홈이 뒤로가기버튼임
            android.R.id.home ->{
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}