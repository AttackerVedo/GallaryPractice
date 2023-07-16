package com.attackervedo.gallarypractice

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.attackervedo.gallarypractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val imageLoadLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){uriList ->
        updateImages(uriList)
    }
    private lateinit var imageAdapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //툴바 지정
        binding.toolBar.apply {
            title = "사진 가져오기"
            setSupportActionBar(this)
        }

    binding.loadImageBtn.setOnClickListener {
        checkPermission()
    }
        initRecyclerView()

        binding.navigateFrameActivityButton.setOnClickListener {
            navigateToFrameActivity()
        }
    }//onCreate

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_add -> {
                checkPermission()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun navigateToFrameActivity(){
        val images = imageAdapter.currentList.filterIsInstance<ImageItems.Image>().map { it.uri.toString() }.toTypedArray()
        val intent = Intent(this, FrameActivity::class.java)
        .putExtra("images", images)
        startActivity(intent)
    }


    @SuppressLint("SuspiciousIndentation")
    private fun initRecyclerView(){
    imageAdapter = ImageAdapter(object:ImageAdapter.ItemClickListener{
        override fun onLoadMoreClick() {
            checkPermission()
        }

    })

        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun checkPermission(){
    when {
        //허가가 되어 있는 상태
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED -> {
            loadImage()
        }
        shouldShowRequestPermissionRationale(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) -> {
            // else 문 탔다가 거절 당한 상태
            showPermissionInfoDialog()
        }
        else -> {
            //처음
            requestReadExternalStorage()
        }
    }


    }

    private fun showPermissionInfoDialog(){
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 위해서, 외부 저장소 읽기 권한이 필요합니다.")
            setNegativeButton("취소",null)
            setPositiveButton("동의"){_,_ ->
                requestReadExternalStorage()
            }
        }.show()
    }

    private fun requestReadExternalStorage(){
        //앱 권한 물어보기
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_READ_EXTERNAL_STORAGE)
    }
    private fun loadImage(){
        imageLoadLauncher.launch("image/*")
    }

    private fun updateImages(uriList:List<Uri>){
        Log.i("updateImages","${uriList}")
        val images = uriList.map{ImageItems.Image(it)}
        val updatedImages =imageAdapter.currentList.toMutableList().apply { addAll(images) }
        imageAdapter.submitList(updatedImages)
    }

    // 다이얼로그에서 동의를 누르면 바로 카메라로 가는 액션
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUEST_READ_EXTERNAL_STORAGE ->{
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_DENIED
                if(resultCode == PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }
            }
        }
    }
    companion object{
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}//class