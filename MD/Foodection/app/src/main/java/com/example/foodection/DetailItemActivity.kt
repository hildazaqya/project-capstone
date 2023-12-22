package com.example.foodection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.dummy.Vegetables
import com.example.foodection.databinding.ActivityDetailItemBinding

class DetailItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailItemBinding

    companion object {
        const val SET = "set"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tujuan = intent.getParcelableExtra<Vegetables>(SET)

        if (tujuan != null) {
            Log.i("TES", tujuan.toString())
            binding.tvSayur.text = tujuan.name
            binding.tvisikandungan.text = tujuan.kandungan
            binding.tvhasildetail.text = tujuan.detail
            Glide.with(this)
                .load(tujuan.photo)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgDetail)
        }
    }
}
